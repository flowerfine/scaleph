package cn.sliew.scaleph.plugin.framework.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@ToString
@EqualsAndHashCode
public class PluginInfo {

    public static final String PLUGIN_PROPERTIES = "plugin.properties";

    private final String name;
    private final String description;
    private final String version;
    private final String classname;

    public PluginInfo(String name, String description, String version, String classname) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.classname = classname;

    }

    /**
     * Reads the plugin descriptor file.
     *
     * @param path the path to the root directory for the plugin
     * @return the plugin info
     * @throws IOException if an I/O exception occurred reading the plugin descriptor
     */
    public static PluginInfo readFromProperties(final Path path) throws IOException {
        final Path descriptor = path.resolve(PLUGIN_PROPERTIES);

        final Map<String, String> propsMap;
        {
            final Properties props = new Properties();
            try (InputStream stream = Files.newInputStream(descriptor)) {
                props.load(stream);
            }
            propsMap = props.stringPropertyNames().stream().collect(Collectors.toMap(Function.identity(), props::getProperty));
        }

        final String name = propsMap.remove("name");
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("property [name] is missing in [" + descriptor + "]");
        }
        final String description = propsMap.remove("description");
        if (description == null) {
            throw new IllegalArgumentException("property [description] is missing for plugin [" + name + "]");
        }
        final String version = propsMap.remove("version");
        if (version == null) {
            throw new IllegalArgumentException("property [version] is missing for plugin [" + name + "]");
        }

        final String classname = propsMap.remove("classname");

        if (propsMap.isEmpty() == false) {
            throw new IllegalArgumentException("Unknown properties for plugin [" + name + "] in plugin descriptor: " + propsMap.keySet());
        }

        return new PluginInfo(name, description, version, classname);
    }
}
