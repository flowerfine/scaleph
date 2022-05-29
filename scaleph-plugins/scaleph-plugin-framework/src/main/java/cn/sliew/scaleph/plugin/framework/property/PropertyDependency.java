package cn.sliew.scaleph.plugin.framework.property;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import lombok.Getter;

@Getter
public class PropertyDependency<T> {
    private final String propertyName;
    private final Set<T> dependentValues;

    public PropertyDependency(final String propertyName) {
        this.propertyName = Objects.requireNonNull(propertyName);
        this.dependentValues = null;
    }

    public PropertyDependency(final String propertyName, final Set<T> dependentValues) {
        this.propertyName = Objects.requireNonNull(propertyName);
        this.dependentValues =
            Collections.unmodifiableSet(new HashSet<>(Objects.requireNonNull(dependentValues)));
    }
}