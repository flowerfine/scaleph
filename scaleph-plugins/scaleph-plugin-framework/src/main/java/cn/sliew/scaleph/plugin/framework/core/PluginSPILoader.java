package cn.sliew.scaleph.plugin.framework.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

public class PluginSPILoader<C extends Plugin> {

    private final Class<C> clazz;

    private volatile Map<PluginInfo, C> services = Collections.emptyMap();
    private volatile Set<PluginInfo> pluginInfos = Collections.emptySet();

    public PluginSPILoader(Class<C> clazz, ClassLoader classLoader) {
        this.clazz = clazz;
        final ClassLoader clazzClassloader = clazz.getClassLoader();
        if (classLoader == null) {
            classLoader = clazzClassloader;
        }
        load(classLoader);
    }

    public static <T extends Plugin> T newInstance(Class<T> clazz, Map<String, String> args) {
        try {
            return clazz.getConstructor(Map.class).newInstance(args);
        } catch (InvocationTargetException ite) {
            final Throwable cause = ite.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new RuntimeException(
                "Unexpected checked exception while calling constructor of " + clazz.getName(),
                cause);
        } catch (ReflectiveOperationException e) {
            throw new UnsupportedOperationException(
                "Plugin "
                    + clazz.getName()
                    +
                    " cannot be instantiated. This is likely due to missing Map<String,String> constructor.",
                e);
        }
    }

    public void load(ClassLoader classLoader) {
        Objects.requireNonNull(classLoader, "classLoader must not be null");
        final LinkedHashMap<PluginInfo, C> services = new LinkedHashMap<>(this.services);
        final LinkedHashSet<PluginInfo> pluginInfos = new LinkedHashSet<>(this.pluginInfos);

        final Spliterator<C> spliterator = ServiceLoader.load(clazz, classLoader).spliterator();
        StreamSupport.stream(spliterator, false)
            .forEachOrdered(
                service -> {
                    final PluginInfo pluginInfo = service.getPluginInfo();
                    if (!services.containsKey(pluginInfo)) {
                        services.put(pluginInfo, service);
                        pluginInfos.add(pluginInfo);
                    }
                });
        this.services = new HashMap<>(services);
        this.pluginInfos = new HashSet<>(pluginInfos);
    }

    public Set<PluginInfo> availableServices() {
        return pluginInfos;
    }

    public Map<PluginInfo, C> getServices() {
        return Collections.unmodifiableMap(services);
    }

    public Optional<C> getPlugin(PluginInfo pluginInfo) {
        return Optional.ofNullable(services.get(pluginInfo));
    }


}
