package cn.sliew.scaleph.plugin.framework.property;

@FunctionalInterface
public interface Parser<T> {

    T parse(String value);
}
