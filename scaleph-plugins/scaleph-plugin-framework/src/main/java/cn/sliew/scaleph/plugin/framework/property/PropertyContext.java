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

import lombok.extern.log4j.Log4j2;

import java.util.*;
import java.util.function.Function;

import static cn.sliew.milky.common.check.Ensures.checkNotNull;

@Log4j2
public class PropertyContext implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    protected final HashMap<String, Object> confData;

    public PropertyContext() {
        this.confData = new HashMap<>();
    }

    public PropertyContext(PropertyContext other) {
        this.confData = new HashMap<>(other.confData);
    }

    public static PropertyContext fromMap(Map<String, Object> map) {
        final PropertyContext context = new PropertyContext();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != null) {
                context.setString(entry.getKey(), entry.getValue());
            }
        }
        return context;
    }

    public static PropertyContext fromProperties(Properties props) {
        final PropertyContext context = new PropertyContext();
        props.forEach((property, value) -> context.setString((String) property, value));
        return context;
    }

    static <E extends Enum<?>> E convertToEnum(Object o, Class<E> clazz) {
        if (o.getClass().equals(clazz)) {
            return (E) o;
        }

        return Arrays.stream(clazz.getEnumConstants())
                .filter(e -> e.toString().toUpperCase(Locale.ROOT)
                        .equals(o.toString().toUpperCase(Locale.ROOT)))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Could not parse value for enum %s. Expected one of: [%s]",
                                clazz, Arrays.toString(clazz.getEnumConstants()))));
    }

    static String toString(Object o) {
        return Objects.toString(o, null);
    }

    public String getString(PropertyDescriptor<String> descriptor) {
        return getOptional(descriptor).orElseGet(() -> getDefaultValue(descriptor));
    }

    public String getString(PropertyDescriptor<String> descriptor, String defaultValue) {
        return getOptional(descriptor).orElse(defaultValue);
    }

    public void setString(String key, String value) {
        setValueInternal(key, value);
    }

    public void setString(String key, Object value) {
        setValueInternal(key, value);
    }

    public void setString(PropertyDescriptor<String> descriptor, String value) {
        setValueInternal(descriptor.getName(), value);
    }

    public int getInteger(PropertyDescriptor<Integer> descriptor) {
        return getOptional(descriptor).orElseGet(() -> getDefaultValue(descriptor));
    }

    public int getInteger(PropertyDescriptor<Integer> descriptor, int defaultValue) {
        return getOptional(descriptor).orElse(defaultValue);
    }

    public void setInteger(String key, int value) {
        setValueInternal(key, value);
    }

    public void setInteger(PropertyDescriptor<Integer> descriptor, int value) {
        setValueInternal(descriptor.getName(), value);
    }

    public long getLong(PropertyDescriptor<Long> descriptor) {
        return getOptional(descriptor).orElseGet(() -> getDefaultValue(descriptor));
    }

    public long getLong(PropertyDescriptor<Long> descriptor, long defaultValue) {
        return getOptional(descriptor).orElse(defaultValue);
    }

    public void setLong(String key, long value) {
        setValueInternal(key, value);
    }

    public void setLong(PropertyDescriptor<Long> descriptor, long value) {
        setValueInternal(descriptor.getName(), value);
    }

    public boolean getBoolean(PropertyDescriptor<Boolean> descriptor) {
        return getOptional(descriptor).orElseGet(() -> getDefaultValue(descriptor));
    }

    public boolean getBoolean(PropertyDescriptor<Boolean> descriptor, boolean defaultValue) {
        return getOptional(descriptor).orElse(defaultValue);
    }

    public void setBoolean(String key, boolean value) {
        setValueInternal(key, value);
    }

    public void setBoolean(PropertyDescriptor<Boolean> descriptor, boolean value) {
        setValueInternal(descriptor.getName(), value);
    }

    public float getFloat(PropertyDescriptor<Float> descriptor) {
        return getOptional(descriptor).orElseGet(() -> getDefaultValue(descriptor));
    }

    public float getFloat(PropertyDescriptor<Float> descriptor, float defaultValue) {
        return getOptional(descriptor).orElse(defaultValue);
    }

    public void setFloat(String key, float value) {
        setValueInternal(key, value);
    }

    public void setFloat(PropertyDescriptor<Float> descriptor, float value) {
        setValueInternal(descriptor.getName(), value);
    }

    public double getDouble(PropertyDescriptor<Double> descriptor) {
        return getOptional(descriptor).orElseGet(() -> getDefaultValue(descriptor));
    }

    public double getDouble(PropertyDescriptor<Double> descriptor, double defaultValue) {
        return getOptional(descriptor).orElse(defaultValue);
    }

    public void setDouble(String key, double value) {
        setValueInternal(key, value);
    }

    public void setDouble(PropertyDescriptor<Double> descriptor, double value) {
        setValueInternal(descriptor.getName(), value);
    }

    public String getValue(PropertyDescriptor<?> descriptor) {
        return Optional.ofNullable(
                        getRawValueFromOption(descriptor).orElseGet(() -> getDefaultValue(descriptor)))
                .map(String::valueOf)
                .orElse(null);
    }

    // --------------------------------------------------------------------------------------------

    public <T extends Enum<T>> T getEnum(
            final Class<T> enumClass, final PropertyDescriptor<String> descriptor) {
        checkNotNull(enumClass, () -> "enumClass must not be null");
        checkNotNull(descriptor, () -> "configOption must not be null");

        Object rawValue =
                getRawValueFromOption(descriptor).orElseGet(() -> getDefaultValue(descriptor));
        try {
            return convertToEnum(rawValue, enumClass);
        } catch (IllegalArgumentException ex) {
            final String errorMessage =
                    String.format(
                            "Value for config option %s must be one of %s (was %s)",
                            descriptor.getName(),
                            Arrays.toString(enumClass.getEnumConstants()),
                            rawValue);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public void addAll(PropertyContext other) {
        synchronized (this.confData) {
            synchronized (other.confData) {
                this.confData.putAll(other.confData);
            }
        }
    }

    public void addAll(PropertyContext other, String prefix) {
        final StringBuilder bld = new StringBuilder();
        bld.append(prefix);
        final int pl = bld.length();

        synchronized (this.confData) {
            synchronized (other.confData) {
                for (Map.Entry<String, Object> entry : other.confData.entrySet()) {
                    bld.setLength(pl);
                    bld.append(entry.getKey());
                    this.confData.put(bld.toString(), entry.getValue());
                }
            }
        }
    }

    public void addAllToProperties(Properties props) {
        synchronized (this.confData) {
            props.putAll(this.confData);
        }
    }

    public Map<String, Object> toMap() {
        synchronized (this.confData) {
            Map<String, Object> ret = new HashMap<>(this.confData.size());
            ret.putAll(confData);
            return ret;
        }
    }

    @Override
    public PropertyContext clone() {
        PropertyContext config = new PropertyContext();
        config.addAll(this);

        return config;
    }

    public boolean containsKey(String key) {
        synchronized (this.confData) {
            return this.confData.containsKey(key);
        }
    }

    public boolean contains(PropertyDescriptor<?> descriptor) {
        synchronized (this.confData) {
            final Function<String, Optional<Boolean>> applier =
                    (key) -> {
                        if (this.confData.containsKey(key)) {
                            return Optional.of(true);
                        }
                        return Optional.empty();
                    };
            return applyWithOption(descriptor, applier).orElse(false);
        }
    }

    private <T> Optional<T> applyWithOption(
            PropertyDescriptor<?> descriptor, Function<String, Optional<T>> applier) {
        final Optional<T> valueFromExactKey = applier.apply(descriptor.getName());
        if (valueFromExactKey.isPresent()) {
            return valueFromExactKey;
        } else if (descriptor.getFallbackProperty().isPresent()) {
            // try the fallback keys
            final PropertyDescriptor<?> fallbackKey = descriptor.getFallbackProperty().get();
            final Optional<T> valueFromFallbackKey = applier.apply(fallbackKey.getName());
            if (valueFromFallbackKey.isPresent()) {
                loggingFallback(fallbackKey, descriptor);
                return valueFromFallbackKey;
            }

        }
        return Optional.empty();
    }

    public <T> T get(PropertyDescriptor<T> descriptor) {
        return getOptional(descriptor).orElseGet(() -> getDefaultValue(descriptor));
    }

    public <T> Optional<T> getOptional(PropertyDescriptor<T> descriptor) {
        Optional<Object> rawValue = getRawValueFromOption(descriptor);

        try {
            return rawValue.map(value -> descriptor.getParser().apply(toString(value)));
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format("Could not parse value '%s' for key '%s'.",
                            rawValue.map(Object::toString).orElse(""), descriptor.getName()),
                    e);
        }
    }

    public <T> PropertyContext set(PropertyDescriptor<T> descriptor, T value) {
        setValueInternal(descriptor.getName(), value);
        return this;
    }

    // --------------------------------------------------------------------------------------------

    public <T> boolean removeConfig(PropertyDescriptor<T> descriptor) {
        synchronized (this.confData) {
            final Function<String, Optional<Boolean>> applier =
                    (key) -> {
                        if (this.confData.remove(key) != null) {
                            return Optional.of(true);
                        }
                        return Optional.empty();
                    };
            return applyWithOption(descriptor, applier).orElse(false);
        }
    }

    <T> void setValueInternal(String key, T value) {
        if (key == null) {
            throw new NullPointerException("Key must not be null.");
        }
        if (value == null) {
            throw new NullPointerException("Value must not be null.");
        }

        synchronized (this.confData) {
            this.confData.put(key, value);
        }
    }

    private Optional<Object> getRawValue(String key) {
        if (key == null) {
            throw new NullPointerException("Key must not be null.");
        }

        synchronized (this.confData) {
            final Object valueFromExactKey = this.confData.get(key);
            return Optional.ofNullable(valueFromExactKey);
        }
    }

    private Optional<Object> getRawValueFromOption(PropertyDescriptor<?> descriptor) {
        return applyWithOption(descriptor, this::getRawValue);
    }

    private <T> T getDefaultValue(PropertyDescriptor<T> descriptor) {
        return descriptor.getDefaultValue();
    }

    private void loggingFallback(PropertyDescriptor fallbackKey, PropertyDescriptor<?> descriptor) {
        if (fallbackKey.getProperties().contains(Property.Deprecated)) {
            log.warn("Config uses deprecated configuration key '{}' instead of proper key '{}'",
                    fallbackKey.getName(),
                    descriptor.getName());
        } else {
            log.info("Config uses fallback configuration key '{}' instead of key '{}'",
                    fallbackKey.getName(),
                    descriptor.getName());
        }
    }

    // --------------------------------------------------------------------------------------------

    @Override
    public int hashCode() {
        int hash = 0;
        for (String s : this.confData.keySet()) {
            hash ^= s.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof PropertyContext) {
            Map<String, Object> otherConf = ((PropertyContext) obj).confData;

            for (Map.Entry<String, Object> e : this.confData.entrySet()) {
                Object thisVal = e.getValue();
                Object otherVal = otherConf.get(e.getKey());

                if (!thisVal.getClass().equals(byte[].class)) {
                    if (!thisVal.equals(otherVal)) {
                        return false;
                    }
                } else if (otherVal.getClass().equals(byte[].class)) {
                    if (!Arrays.equals((byte[]) thisVal, (byte[]) otherVal)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.confData.toString();
    }
}