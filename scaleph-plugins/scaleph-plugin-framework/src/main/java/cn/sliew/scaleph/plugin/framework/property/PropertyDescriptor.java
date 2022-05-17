package cn.sliew.scaleph.plugin.framework.property;

import lombok.Getter;

import java.util.*;

@Getter
public class PropertyDescriptor implements Comparable<PropertyDescriptor> {

    private final String name;
    private final String defaultValue;
    private final String description;
    private final List<AllowableValue> allowableValues;
    private final EnumSet<Property> properties;
    private final List<Validator> validators;
    private final Set<PropertyDependency> dependencies;

    protected PropertyDescriptor(final Builder builder) {
        this.name = builder.name;
        this.defaultValue = builder.defaultValue;
        this.description = builder.description;
        this.allowableValues = builder.allowableValues == null ? null : Collections.unmodifiableList(new ArrayList<>(builder.allowableValues));
        this.properties = builder.properties;
        this.validators = Collections.unmodifiableList(new ArrayList<>(builder.validators));
        this.dependencies = builder.dependencies == null ? Collections.emptySet() : Collections.unmodifiableSet(new HashSet<>(builder.dependencies));
    }

    @Override
    public int compareTo(final PropertyDescriptor o) {
        if (o == null) {
            return -1;
        }
        return getName().compareTo(o.getName());
    }

    public static final class Builder {

        private String name = null;
        private String defaultValue = null;
        private String description = "";
        private List<AllowableValue> allowableValues = null;
        private EnumSet<Property> properties = EnumSet.noneOf(Property.class);
        private List<Validator> validators = new ArrayList<>();
        private Set<PropertyDependency> dependencies = null;


        public Builder fromPropertyDescriptor(final PropertyDescriptor specDescriptor) {
            this.name = specDescriptor.name;
            this.defaultValue = specDescriptor.defaultValue;
            this.description = specDescriptor.description;
            this.allowableValues = specDescriptor.allowableValues == null ? null : new ArrayList<>(specDescriptor.allowableValues);
            this.properties = EnumSet.copyOf(specDescriptor.properties);
            this.validators = new ArrayList<>(specDescriptor.validators);
            this.dependencies = new HashSet<>(specDescriptor.dependencies);
            return this;
        }

        public Builder name(final String name) {
            if (null != name) {
                this.name = name;
            }
            return this;
        }

        public Builder defaultValue(final String value) {
            if (null != value) {
                this.defaultValue = value;
            }
            return this;
        }

        public Builder description(final String description) {
            if (null != description) {
                this.description = description;
            }
            return this;
        }

        public Builder allowableValues(final String... values) {
            if (null != values) {
                this.allowableValues = new ArrayList<>();
                for (final String value : values) {
                    allowableValues.add(new AllowableValue(value, value));
                }
            }
            return this;
        }

        public Builder allowableValues(final Set<String> values) {
            if (null != values) {
                this.allowableValues = new ArrayList<>();

                for (final String value : values) {
                    this.allowableValues.add(new AllowableValue(value));
                }
            }
            return this;
        }

        public <E extends Enum<E>> Builder allowableValues(final E[] values) {
            if (null != values) {
                this.allowableValues = new ArrayList<>();
                for (final E value : values) {
                    allowableValues.add(new AllowableValue(value.name(), value.name()));
                }
            }
            return this;
        }

        public <E extends Enum<E> & DescribedValue> Builder allowableValues(final Class<E> enumClass) {
            this.allowableValues = new ArrayList<>();
            for (E enumValue : enumClass.getEnumConstants()) {
                this.allowableValues.add(new AllowableValue(enumValue.getValue(), enumValue.getName(), enumValue.getDescription()));
            }
            return this;
        }

        public Builder allowableValues(final AllowableValue... values) {
            if (null != values) {
                this.allowableValues = Arrays.asList(values);
            }
            return this;
        }

        public Builder properties(final Property... properties) {
            if (null != properties) {
                this.properties = EnumSet.copyOf(Arrays.asList(properties));
            }
            return this;
        }

        public Builder addValidator(final Validator validator) {
            if (validator != null) {
                validators.add(validator);
            }
            return this;
        }

        private boolean isValueAllowed(final String value) {
            if (allowableValues == null || value == null) {
                return true;
            }

            for (final AllowableValue allowableValue : allowableValues) {
                if (allowableValue.getValue().equals(value)) {
                    return true;
                }
            }

            return false;
        }

        public Builder dependsOn(final PropertyDescriptor property, final AllowableValue... dependentValues) {
            if (dependencies == null) {
                dependencies = new HashSet<>();
            }

            if (dependentValues.length == 0) {
                dependencies.add(new PropertyDependency(property.getName()));
            } else {
                final Set<String> dependentValueSet = new HashSet<>();
                for (final AllowableValue value : dependentValues) {
                    dependentValueSet.add(value.getValue());
                }

                dependencies.add(new PropertyDependency(property.getName(), dependentValueSet));
            }

            return this;
        }

        public Builder dependsOn(final PropertyDescriptor property, final String firstDependentValue, final String... additionalDependentValues) {
            final AllowableValue[] dependentValues = new AllowableValue[additionalDependentValues.length + 1];
            dependentValues[0] = new AllowableValue(firstDependentValue);
            int i = 1;
            for (final String additionalDependentValue : additionalDependentValues) {
                dependentValues[i++] = new AllowableValue(additionalDependentValue);
            }

            return dependsOn(property, dependentValues);
        }

        public PropertyDescriptor build() {
            if (name == null) {
                throw new IllegalStateException("Must specify a name");
            }
            if (!isValueAllowed(defaultValue)) {
                throw new IllegalStateException("Default value [" + defaultValue + "] is not in the set of allowable values");
            }

            return new PropertyDescriptor(this);
        }
    }
}
