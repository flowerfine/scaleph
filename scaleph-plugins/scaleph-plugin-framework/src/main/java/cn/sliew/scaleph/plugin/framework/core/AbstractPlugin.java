package cn.sliew.scaleph.plugin.framework.core;

import cn.sliew.scaleph.plugin.framework.property.*;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public abstract class AbstractPlugin implements Plugin {

    protected MeterRegistry meterRegistry;

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

        PropertyContext propertyContext = PropertyContext.fromProperties(properties);

        for (final PropertyDescriptor descriptor : getSupportedProperties()) {
            final String value = propertyContext.getValue(descriptor);
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

    @Override
    public void setMeterRegistry(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
}
