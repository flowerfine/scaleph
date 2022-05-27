package cn.sliew.scaleph.plugin.framework.core;

import cn.sliew.scaleph.plugin.framework.lifecycle.LifeCycle;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.ValidationResult;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * todo environment variables
 */
public interface Plugin extends LifeCycle {

    PluginInfo getPluginInfo();

    PropertyDescriptor getPropertyDescriptor(String name);

    List<PropertyDescriptor> getSupportedProperties();

    Collection<ValidationResult> validate(Properties properties);
}
