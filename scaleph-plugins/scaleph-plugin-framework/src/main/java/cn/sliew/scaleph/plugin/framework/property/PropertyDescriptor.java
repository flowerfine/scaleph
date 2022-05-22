package cn.sliew.scaleph.plugin.framework.property;

import lombok.Getter;

import java.util.*;
import java.util.function.Function;

@Getter
public class PropertyDescriptor<T> implements Comparable<PropertyDescriptor> {

    private final String name;
    private final Function<PropertyDescriptor<T>, T> defaultValue;
    private final String description;
    private final Parser<Object, T> parser;
    private final List<AllowableValue<T>> allowableValues;
    private final EnumSet<Property> properties;
    private final List<Validator> validators;
    private final Optional<PropertyDescriptor<T>> fallbackProperty;
    private final Set<PropertyDependency> dependencies;

    protected PropertyDescriptor(final Builder builder) {
        this.name = builder.name;
        this.defaultValue = builder.defaultValue;
        this.description = builder.description;
        this.parser = builder.parser;
        this.allowableValues = builder.allowableValues == null ? null : Collections.unmodifiableList(new ArrayList<>(builder.allowableValues));
        this.properties = builder.properties;
        this.validators = Collections.unmodifiableList(new ArrayList<>(builder.validators));
        this.fallbackProperty = builder.fallbackProperty;
        this.dependencies = builder.dependencies == null ? Collections.emptySet() : Collections.unmodifiableSet(new HashSet<>(builder.dependencies));
    }

    public ValidationResult validate(final T input) {
        ValidationResult lastResult = Validator.INVALID.validate(this.name, input);

        if (allowableValues != null && !allowableValues.isEmpty()) {
            final ConstrainedSetValidator csValidator = new ConstrainedSetValidator(allowableValues);
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
        private Function<PropertyDescriptor<T>, T> defaultValue = null;
        private String description = "";
        private Parser<Object, T> parser;
        private List<AllowableValue<T>> allowableValues = null;
        private EnumSet<Property> properties = EnumSet.noneOf(Property.class);
        private List<Validator> validators = new ArrayList<>();
        private Optional<PropertyDescriptor<T>> fallbackProperty = Optional.empty();
        private Set<PropertyDependency> dependencies = null;


        public Builder fromPropertyDescriptor(final PropertyDescriptor specDescriptor) {
            this.name = specDescriptor.name;
            this.defaultValue = specDescriptor.defaultValue;
            this.description = specDescriptor.description;
            this.parser = specDescriptor.parser;
            this.allowableValues = specDescriptor.allowableValues == null ? null : new ArrayList<>(specDescriptor.allowableValues);
            this.properties = EnumSet.copyOf(specDescriptor.properties);
            this.validators = new ArrayList<>(specDescriptor.validators);
            this.fallbackProperty = specDescriptor.fallbackProperty;
            this.dependencies = new HashSet<>(specDescriptor.dependencies);
            return this;
        }

        public Builder name(final String name) {
            if (null != name) {
                this.name = name;
            }
            return this;
        }

        public Builder defaultValue(final Function<PropertyDescriptor<T>, T> value) {
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

        public Builder parser(final Parser<Object, T> parser) {
            if (null != parser) {
                this.parser = parser;
            }
            return this;
        }

        public Builder allowableValues(final String... values) {
            if (null != values) {
                this.allowableValues = new ArrayList<>();
                for (final String value : values) {
                    final Object parsed = parser.parse(value);
                    allowableValues.add(new AllowableValue(parsed));
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

        public Builder allowableValues(final AllowableValue<T>... values) {
            if (null != values) {
                this.allowableValues = Arrays.asList(values);
            }
            return this;
        }

        private boolean isValueAllowed(final T value) {
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

        public Builder fallbackProperty(final PropertyDescriptor<T> fallbackProperty) {
            if (fallbackProperty != null) {
                this.fallbackProperty = Optional.of(fallbackProperty);
            }
            return this;
        }

        public Builder dependsOn(final PropertyDescriptor property, final AllowableValue<T>... dependentValues) {
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
            final PropertyDescriptor<T> propertyDescriptor = new PropertyDescriptor(this);

            if (defaultValue != null && !isValueAllowed(defaultValue.apply(propertyDescriptor))) {
                throw new IllegalStateException("Default value [" + defaultValue + "] is not in the set of allowable values");
            }

            return new PropertyDescriptor(this);
        }
    }

    private static final class ConstrainedSetValidator<T> implements Validator<T> {

        private static final String POSITIVE_EXPLANATION = "Given value found in allowed set";
        private static final String NEGATIVE_EXPLANATION = "Given value not found in allowed set '%1$s'";
        private static final String VALUE_DEMARCATOR = ", ";
        private final String validStrings;
        private final Collection<T> validValues;

        /**
         * Constructs a validator that will check if the given value is in the
         * given set.
         *
         * @param validValues values which are acceptible
         * @throws NullPointerException if the given validValues is null
         */
        private ConstrainedSetValidator(final Collection<AllowableValue<T>> validValues) {
            String validVals = "";
            if (!validValues.isEmpty()) {
                final StringBuilder valuesBuilder = new StringBuilder();
                for (final AllowableValue value : validValues) {
                    valuesBuilder.append(value).append(VALUE_DEMARCATOR);
                }
                validVals = valuesBuilder.substring(0, valuesBuilder.length() - VALUE_DEMARCATOR.length());
            }
            validStrings = validVals;

            this.validValues = new ArrayList<>(validValues.size());
            for (final AllowableValue<T> value : validValues) {
                this.validValues.add(value.getValue());
            }
        }

        @Override
        public ValidationResult validate(final String subject, final T input) {
            final ValidationResult.Builder builder = new ValidationResult.Builder();
            builder.input(input);
            builder.subject(subject);
            if (validValues.contains(input)) {
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
