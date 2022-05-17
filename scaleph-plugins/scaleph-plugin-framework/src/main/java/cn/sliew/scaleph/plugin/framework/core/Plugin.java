package cn.sliew.scaleph.plugin.framework.core;

import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.ValidationResult;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * todo environment variables
 */
public interface Plugin {

    PluginInfo getPluginInfo();

    PropertyDescriptor getPropertyDescriptor(String name);

    List<PropertyDescriptor> getRequiredProperties();

    List<PropertyDescriptor> getOptionalProperties();

    Collection<ValidationResult> validate(Properties properties);

}
