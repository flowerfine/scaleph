package cn.sliew.scaleph.system.dict.job;

import cn.sliew.scaleph.system.dict.DictDefinition;
import cn.sliew.scaleph.system.dict.DictInstance;
import cn.sliew.scaleph.system.dict.DictType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DataType implements DictInstance {

    INT("int", "INT"),
    BIGINT("bigint", "BIGINT"),
    FLOAT("float", "FLOAT"),
    DOUBLE("double", "DOUBLE"),
    STRING("string", "STRING"),
    DATE("date", "DATE"),
    TIMESTAMP("timestamp", "TIMESTAMP"),
    ;

    @JsonValue
    @EnumValue
    private String code;
    private String value;

    DataType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public DictDefinition getDefinition() {
        return DictType.DATA_TYPE;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }
}
