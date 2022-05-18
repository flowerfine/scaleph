package cn.sliew.scaleph.plugin.framework.core;

import cn.sliew.scaleph.plugin.framework.property.Property;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.ValidationResult;
import cn.sliew.scaleph.plugin.framework.property.Validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public abstract class AbstractPlugin implements Plugin {

    @Override
    public PropertyDescriptor getPropertyDescriptor(String name) {
        final PropertyDescriptor specDescriptor = new PropertyDescriptor.Builder().name(name).build();
        return getPropertyDescriptor(specDescriptor);
    }

    private PropertyDescriptor getPropertyDescriptor(final PropertyDescriptor specDescriptor) {
        PropertyDescriptor descriptor = getSupportedPropertyDescriptor(specDescriptor);
        if (descriptor != null) {
            return descriptor;
        }

        if (descriptor == null) {
            descriptor = new PropertyDescriptor.Builder().fromPropertyDescriptor(specDescriptor).addValidator(Validator.INVALID).build();
        }
        return descriptor;
    }

    private PropertyDescriptor getSupportedPropertyDescriptor(final PropertyDescriptor specDescriptor) {
        final List<PropertyDescriptor> requiredPropertyDescriptors = getSupportedProperties();
        if (requiredPropertyDescriptors != null) {
            for (final PropertyDescriptor desc : requiredPropertyDescriptors) { //find actual descriptor
                if (specDescriptor.equals(desc)) {
                    return desc;
                }
            }
        }
        return null;
    }


    @Override
    public Collection<ValidationResult> validate(Properties properties) {
        final Collection<ValidationResult> results = new ArrayList<>();

        for (final PropertyDescriptor descriptor : getSupportedProperties()) {
            String value = properties.getProperty(descriptor.getName());
            if (value == null) {
                value = descriptor.getDefaultValue();
            }
            
            if (value == null && descriptor.getProperties().contains(Property.Required)) {
                ValidationResult.Builder builder = new ValidationResult.Builder()
                        .valid(false)
                        .subject(descriptor.getName())
                        .input(null)
                        .explanation(descriptor.getName() + " is required");
                results.add(builder.build());
                continue;
            } else if (value == null) {
                continue;
            }

            final ValidationResult result = descriptor.validate(value);
            if (!result.isValid()) {
                results.add(result);
            }
        }

        return results;
    }
}
