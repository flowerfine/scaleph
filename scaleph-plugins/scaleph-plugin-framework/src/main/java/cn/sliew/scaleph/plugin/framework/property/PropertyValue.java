package cn.sliew.scaleph.plugin.framework.property;

import java.util.concurrent.TimeUnit;

public interface PropertyValue {

    String getValue();

    Boolean asBoolean();

    Integer asInteger();

    Long asLong();

    Float asFloat();

    Double asDouble();

    Long asTimePeriod(TimeUnit timeUnit);

    boolean isEmpty();

}