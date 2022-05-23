package cn.sliew.scaleph.plugin.framework.property;

public interface DescribedValue<T> {

    T getValue();

    String getName();

    String getDescription();
}