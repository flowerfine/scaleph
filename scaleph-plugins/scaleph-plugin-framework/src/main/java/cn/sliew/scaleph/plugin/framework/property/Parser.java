package cn.sliew.scaleph.plugin.framework.property;

import java.util.function.Function;

@FunctionalInterface
public interface Parser<T> extends Function<String, T> {

    @Override
    T apply(String s);
}
