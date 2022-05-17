package cn.sliew.scaleph.plugin.framework.property;

import java.util.Objects;

public class AllowableValue implements DescribedValue {

    private final String value;
    private final String name;
    private final String description;

    public AllowableValue(final String value) {
        this(value, value);
    }

    public AllowableValue(final String value, final String name) {
        this(value, name, null);
    }

    public AllowableValue(final String value, final String name, final String description) {
        this.value = Objects.requireNonNull(value);
        this.name = Objects.requireNonNull(name);
        this.description = description;
    }

    @Override
    public String getValue() {
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