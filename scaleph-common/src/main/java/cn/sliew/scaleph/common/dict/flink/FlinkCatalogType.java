package cn.sliew.scaleph.common.dict.flink;

import cn.sliew.scaleph.common.dict.DictInstance;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum FlinkCatalogType implements DictInstance {
    GENERIC("0", "INMEMORY"),
    HIVE("1", "HIVE"),
    JDBC("2", "JDBC"),
    ;

    @JsonCreator
    public static FlinkCatalogType of(String value) {
        return Arrays.stream(values())
                .filter(instance -> instance.getValue().equals(value))
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(FlinkCatalogType.class, value));
    }

    @EnumValue
    private String value;
    private String label;

    FlinkCatalogType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
