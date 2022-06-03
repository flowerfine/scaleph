/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.plugin.framework.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import cn.sliew.scaleph.plugin.framework.property.Property;
import cn.sliew.scaleph.plugin.framework.property.PropertyContext;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.ValidationResult;
import cn.sliew.scaleph.plugin.framework.property.Validator;

public abstract class AbstractPlugin implements Plugin {

    protected Properties properties;
    protected PropertyContext propertyContext;

    @Override
    public void configure(Properties properties) {
        this.properties = properties;
        this.propertyContext = PropertyContext.fromProperties(properties);
    }

    @Override
    public PropertyDescriptor getPropertyDescriptor(String name) {
        final PropertyDescriptor specDescriptor =
            new PropertyDescriptor.Builder().name(name).validateAndBuild();
        return getPropertyDescriptor(specDescriptor);
    }

    private PropertyDescriptor getPropertyDescriptor(final PropertyDescriptor specDescriptor) {
        PropertyDescriptor descriptor = getSupportedPropertyDescriptor(specDescriptor);
        if (descriptor != null) {
            return descriptor;
        }

        if (descriptor == null) {
            descriptor = new PropertyDescriptor.Builder().fromPropertyDescriptor(specDescriptor)
                .addValidator(Validator.INVALID).validateAndBuild();
        }
        return descriptor;
    }

    private PropertyDescriptor getSupportedPropertyDescriptor(
        final PropertyDescriptor specDescriptor) {
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
    public Collection<ValidationResult> validate(PropertyContext properties) {
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
}
