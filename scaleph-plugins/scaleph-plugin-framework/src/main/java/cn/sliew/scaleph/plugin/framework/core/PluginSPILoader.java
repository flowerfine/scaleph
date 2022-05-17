package cn.sliew.scaleph.plugin.framework.core;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.StreamSupport;

public class PluginSPILoader<C extends Plugin> {

    private final Class<C> clazz;

    private volatile Map<String, Class<? extends C>> services = Collections.emptyMap();
    private volatile Set<String> originalNames = Collections.emptySet();

    public PluginSPILoader(Class<C> clazz, ClassLoader classLoader) {
        this.clazz = clazz;
        final ClassLoader clazzClassloader = clazz.getClassLoader();
        if (classLoader == null) {
            classLoader = clazzClassloader;
        }
        load(classLoader);
    }

    public void load(ClassLoader classLoader) {
        Objects.requireNonNull(classLoader, "classLoader must not be null");
        final LinkedHashMap<String, Class<? extends C>> services = new LinkedHashMap<>(this.services);
        final LinkedHashSet<String> originalNames = new LinkedHashSet<>(this.originalNames);

        final Spliterator<C> spliterator = ServiceLoader.load(clazz, classLoader).spliterator();
        StreamSupport.stream(spliterator, false)
                .forEachOrdered(
                        service -> {
                            String originalName = service.getPluginInfo().getName();
                            String name = originalName.toLowerCase(Locale.ROOT);
                            if (!services.containsKey(name)) {
                                services.put(name, (Class<? extends C>) service.getClass());
                                originalNames.add(originalName);
                            }
                        });
        this.services = new HashMap<>(services);
        this.originalNames = new HashSet<>(originalNames);
    }

    public Set<String> availableServices() {
        return originalNames;
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
            throw new RuntimeException("Unexpected checked exception while calling constructor of " + clazz.getName(), cause);
        } catch (ReflectiveOperationException e) {
            throw new UnsupportedOperationException(
                    "Plugin "
                            + clazz.getName()
                            + " cannot be instantiated. This is likely due to missing Map<String,String> constructor.",
                    e);
        }
    }


}
