package cn.sliew.scaleph.plugin.framework.property;

import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
public class PropertyDependency {
    private final String propertyName;
    private final Set<String> dependentValues;

    public PropertyDependency(final String propertyName) {
        this.propertyName = Objects.requireNonNull(propertyName);
        this.dependentValues = null;
    }

    public PropertyDependency(final String propertyName, final Set<String> dependentValues) {
        this.propertyName = Objects.requireNonNull(propertyName);
        this.dependentValues = Collections.unmodifiableSet(new HashSet<>(Objects.requireNonNull(dependentValues)));
    }
}