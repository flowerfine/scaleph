package cn.sliew.scaleph.plugin.framework.property;

import cn.sliew.milky.common.primitives.*;

import java.util.function.Function;

public enum Parsers {
    ;

    public static final Parser<String, Boolean> BOOLEAN_PARSER = value -> Booleans.parseBoolean(value);

    public static final Parser<String, Integer> INTEGER_PARSER = value -> Integers.parseInteger(value);

    public static final Parser<String, Long> LONG_PARSER = value -> Longs.parseLong(value);

    public static final Parser<String, Float> FLOAT_PARSER = value -> Floats.parseFloat(value);

    public static final Parser<String, Double> DOUBLE_PARSER = value -> Doubles.parseDouble(value);

    public static final Parser<String, String> STRING_PARSER = (Parser<String, String>) Function.identity();
}
