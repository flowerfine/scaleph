package cn.sliew.scaleph.plugin.framework.core;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
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
     * @param properties the path to the root directory for the plugin
     * @return the plugin info
     * @throws IOException if an I/O exception occurred reading the plugin descriptor
     */
    public static PluginInfo readFromProperties(Properties properties) throws IOException {

        final Map<String, String> propsMap = properties.stringPropertyNames().stream()
            .collect(Collectors.toMap(Function.identity(), properties::getProperty));

        final String name = propsMap.remove("name");
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("property [name] is missing");
        }
        final String description = propsMap.remove("description");
        if (description == null) {
            throw new IllegalArgumentException(
                "property [description] is missing for plugin [" + name + "]");
        }
        final String version = propsMap.remove("version");
        if (version == null) {
            throw new IllegalArgumentException(
                "property [version] is missing for plugin [" + name + "]");
        }

        final String classname = propsMap.remove("classname");

        if (propsMap.isEmpty() == false) {
            throw new IllegalArgumentException(
                "Unknown properties for plugin [" + name + "] in plugin descriptor: " +
                    propsMap.keySet());
        }

        return new PluginInfo(name, description, version, classname);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PluginInfo that = (PluginInfo) o;
        return name.equals(that.name) && version.equals(that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, version);
    }
}
