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

package cn.sliew.scaleph.plugin.framework.property;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.util.*;

/**
 * todo add category for frontend display.
 * todo springmvc to PropertyDescriptor to mybatis TypeHandler ?
 */
@Getter
public class PropertyDescriptor<T> implements Comparable<PropertyDescriptor> {

    private final String name;
    private final String description;
    private final PropertyType type;
    @JsonIgnore
    private final Parser<T> parser;
    private final List<AllowableValue<T>> allowableValues;
    private final T defaultValue;
    private final EnumSet<Property> properties;
    @JsonIgnore
    private final List<Validator> validators;
    private final Optional<PropertyDescriptor<T>> fallbackProperty;
    private final Set<PropertyDependency> dependencies;

    protected PropertyDescriptor(final Builder<T> builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.type = builder.type;
        this.parser = builder.parser;
        this.allowableValues = builder.allowableValues == null ? null :
                Collections.unmodifiableList(new ArrayList<>(builder.allowableValues));
        this.defaultValue = builder.defaultValue;
        this.properties = builder.properties;
        this.validators = Collections.unmodifiableList(new ArrayList<>(builder.validators));
        this.fallbackProperty = builder.fallbackProperty;
        this.dependencies = builder.dependencies == null ? Collections.emptySet() :
                Collections.unmodifiableSet(new HashSet<>(builder.dependencies));
    }

    public ValidationResult validate(final String input) {
        ValidationResult lastResult = Validator.INVALID.validate(this.name, input);

        if (allowableValues != null && !allowableValues.isEmpty()) {
            final ConstrainedSetValidator csValidator =
                    new ConstrainedSetValidator(parser, allowableValues);
            final ValidationResult csResult = csValidator.validate(this.name, input);

            if (csResult.isValid()) {
                lastResult = csResult;
            } else {
                return csResult;
            }
        }

        for (final Validator validator : validators) {
            lastResult = validator.validate(this.name, input);
            if (!lastResult.isValid()) {
                break;
            }
        }

        return lastResult;
    }

    @Override
    public int compareTo(final PropertyDescriptor o) {
        if (o == null) {
            return -1;
        }
        return getName().compareTo(o.getName());
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof PropertyDescriptor)) {
            return false;
        }
        if (this == other) {
            return true;
        }

        final PropertyDescriptor desc = (PropertyDescriptor) other;
        return this.name.equals(desc.name);
    }

    @Override
    public int hashCode() {
        return 287 + this.name.hashCode() * 47;
    }

    public static final class Builder<T> {

        private String name = null;
        private String description = "";
        private PropertyType type;
        private Parser<T> parser;
        private List<AllowableValue<T>> allowableValues = null;
        private T defaultValue = null;
        private EnumSet<Property> properties = EnumSet.noneOf(Property.class);
        private List<Validator> validators = new ArrayList<>();
        private Optional<PropertyDescriptor<T>> fallbackProperty = Optional.empty();
        private Set<PropertyDependency> dependencies = null;

        private List<String> allowableValueStrings = null;

        public Builder fromPropertyDescriptor(final PropertyDescriptor<T> specDescriptor) {
            this.name = specDescriptor.name;
            this.description = specDescriptor.description;
            this.type = specDescriptor.type;
            this.parser = specDescriptor.parser;
            this.allowableValues = specDescriptor.allowableValues == null ? null :
                    new ArrayList<>(specDescriptor.allowableValues);
            this.defaultValue = specDescriptor.defaultValue;
            this.properties = EnumSet.copyOf(specDescriptor.properties);
            this.validators = new ArrayList<>(specDescriptor.validators);
            this.fallbackProperty = specDescriptor.fallbackProperty;
            this.dependencies = new HashSet<>(specDescriptor.dependencies);
            return this;
        }

        public Builder name(final String name) {
            if (name != null) {
                this.name = name;
            }
            return this;
        }

        public Builder description(final String description) {
            if (description != null) {
                this.description = description;
            }
            return this;
        }

        public Builder type(final PropertyType type) {
            if (type != null) {
                this.type = type;
            }
            return this;
        }

        public Builder parser(final Parser<T> parser) {
            if (parser != null) {
                this.parser = parser;
            }
            return this;
        }

        public Builder defaultValue(final T value) {
            if (value != null) {
                this.defaultValue = value;
            }
            return this;
        }

        public Builder allowableValues(final String... values) {
            if (values != null && values.length > 0) {
                this.allowableValueStrings = new ArrayList<>();
                for (final String value : values) {
                    allowableValueStrings.add(value);
                }
            }
            return this;
        }

        public Builder allowableValues(final Set<String> values) {
            if (values != null && values.size() > 0) {
                this.allowableValueStrings = new ArrayList<>();
                for (final String value : values) {
                    this.allowableValueStrings.add(value);
                }
            }
            return this;
        }

        public <E extends Enum<E>> Builder allowableValues(final E[] values) {
            if (values != null && values.length > 0) {
                this.allowableValues = new ArrayList<>();
                for (final E value : values) {
                    allowableValues.add(new AllowableValue(value, value.name()));
                }
            }
            return this;
        }

        public <E extends Enum<E> & DescribedValue> Builder allowableValues(
                final Class<E> enumClass) {
            this.allowableValues = new ArrayList<>();
            for (E enumValue : enumClass.getEnumConstants()) {
                this.allowableValues.add(
                        new AllowableValue(enumValue.getValue(), enumValue.getName(),
                                enumValue.getDescription()));
            }
            return this;
        }

        public Builder allowableValues(final AllowableValue<T>... values) {
            if (values != null && values.length > 0) {
                this.allowableValues = Arrays.asList(values);
            }
            return this;
        }

        private boolean isValueAllowed(final T value) {
            if (value == null) {
                return false;
            }

            if (allowableValues == null) {
                return true;
            }

            for (final AllowableValue allowableValue : allowableValues) {
                if (allowableValue.getValue().equals(value)) {
                    return true;
                }
            }

            return false;
        }

        public Builder properties(final Property... properties) {
            if (properties != null) {
                this.properties = EnumSet.copyOf(Arrays.asList(properties));
            }
            return this;
        }

        public Builder addValidator(final Validator... validators) {
            if (validators != null) {
                this.validators.addAll(Arrays.asList(validators));
            }
            return this;
        }

        public Builder fallbackProperty(final PropertyDescriptor<T> fallbackProperty) {
            if (fallbackProperty != null) {
                this.fallbackProperty = Optional.of(fallbackProperty);
            }
            return this;
        }

        public Builder dependsOn(final PropertyDescriptor property,
                                 final AllowableValue<T>... dependentValues) {
            if (dependencies == null) {
                dependencies = new HashSet<>();
            }

            if (dependentValues.length == 0) {
                dependencies.add(new PropertyDependency(property.getName()));
            } else {
                final Set<T> dependentValueSet = new HashSet<>();
                for (final AllowableValue<T> value : dependentValues) {
                    dependentValueSet.add(value.getValue());
                }

                dependencies.add(new PropertyDependency(property.getName(), dependentValueSet));
            }

            return this;
        }

        public Builder dependsOn(final PropertyDescriptor property,
                                 final String firstDependentValue,
                                 final String... additionalDependentValues) {
            final AllowableValue[] dependentValues =
                    new AllowableValue[additionalDependentValues.length + 1];
            dependentValues[0] = new AllowableValue(firstDependentValue);
            int i = 1;
            for (final String additionalDependentValue : additionalDependentValues) {
                dependentValues[i++] = new AllowableValue(additionalDependentValue);
            }

            return dependsOn(property, dependentValues);
        }

        public PropertyDescriptor validateAndBuild() {
            if (name == null) {
                throw new IllegalStateException("Must specify a name");
            }
            if (type == null) {
                throw new IllegalStateException("Must specify property type for " + name);
            }
            if (parser == null) {
                throw new IllegalStateException("Must specify a parser for " + name);
            }

            if (allowableValueStrings != null && allowableValues == null) {
                allowableValues = new ArrayList<>(allowableValueStrings.size());
                for (String value : allowableValueStrings) {
                    allowableValues.add(new AllowableValue<>(parser.apply(value)));
                }
            }
            final PropertyDescriptor<T> propertyDescriptor = new PropertyDescriptor(this);

            if (defaultValue != null) {
                if (!isValueAllowed(defaultValue)) {
                    throw new IllegalStateException("Default value [" + defaultValue +
                            "] is not in the set of allowable values");
                }
            }

            return propertyDescriptor;
        }
    }

    private static final class ConstrainedSetValidator<T> implements Validator {

        private static final String POSITIVE_EXPLANATION = "Given value found in allowed set";
        private static final String NEGATIVE_EXPLANATION =
                "Given value not found in allowed set '%1$s'";
        private static final String VALUE_DEMARCATOR = ", ";
        private final String validStrings;
        private final Parser<T> parser;
        private final Collection<T> validValues;

        private ConstrainedSetValidator(final Parser<T> parser,
                                        final Collection<AllowableValue<T>> validValues) {
            this.parser = parser;
            String validVals = "";
            if (!validValues.isEmpty()) {
                final StringBuilder valuesBuilder = new StringBuilder();
                for (final AllowableValue value : validValues) {
                    valuesBuilder.append(value).append(VALUE_DEMARCATOR);
                }
                validVals =
                        valuesBuilder.substring(0, valuesBuilder.length() - VALUE_DEMARCATOR.length());
            }
            validStrings = validVals;

            this.validValues = new ArrayList<>(validValues.size());
            for (final AllowableValue<T> value : validValues) {
                this.validValues.add(value.getValue());
            }
        }

        @Override
        public ValidationResult validate(final String subject, final String input) {
            final ValidationResult.Builder builder = new ValidationResult.Builder();
            builder.input(input);
            builder.subject(subject);
            final T parsedValue = parser.apply(input);
            if (validValues.contains(parsedValue)) {
                builder.valid(true);
                builder.explanation(POSITIVE_EXPLANATION);
            } else {
                builder.valid(false);
                builder.explanation(String.format(NEGATIVE_EXPLANATION, validStrings));
            }
            return builder.build();
        }
    }
}
