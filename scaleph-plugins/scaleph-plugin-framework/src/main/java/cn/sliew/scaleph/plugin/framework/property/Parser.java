package cn.sliew.scaleph.plugin.framework.property;

@FunctionalInterface
public interface Parser<S, T> {

    T parse(S value);
}
