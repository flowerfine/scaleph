package cn.sliew.scaleph.plugin.framework.property;

import cn.sliew.milky.common.primitives.*;

import java.util.function.Function;

public enum Parsers {
    ;

    public static final Parser<Boolean> BOOLEAN_PARSER = value -> Booleans.parseBoolean(value);

    public static final Parser<Integer> INTEGER_PARSER = value -> Integers.parseInteger(value);

    public static final Parser<Long> LONG_PARSER = value -> Longs.parseLong(value);

    public static final Parser<Float> FLOAT_PARSER = value -> Floats.parseFloat(value);

    public static final Parser<Double> DOUBLE_PARSER = value -> Doubles.parseDouble(value);

    public static final Parser<String> STRING_PARSER = (Parser<String>) Function.identity();
}
