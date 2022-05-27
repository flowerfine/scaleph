package cn.sliew.scaleph.plugin.framework.property;

import java.util.Objects;

public class AllowableValue<T> implements DescribedValue<T> {

    private final T value;
    private final String name;
    private final String description;

    public AllowableValue(final T value) {
        this(value, value.toString());
    }

    public AllowableValue(final T value, final String name) {
        this(value, name, null);
    }

    public AllowableValue(final T value, final String name, final String description) {
        this.value = Objects.requireNonNull(value);
        this.name = Objects.requireNonNull(name);
        this.description = description;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}