package cn.sliew.scaleph.system.dict.job;

import cn.sliew.scaleph.system.dict.DictDefinition;
import cn.sliew.scaleph.system.dict.DictInstance;
import cn.sliew.scaleph.system.dict.DictType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RuntimeState implements DictInstance {

    STOP("1", "停止"),
    RUNNING("2", "运行中"),
    WAITING("3", "等待"),
    ;

    @JsonValue
    @EnumValue
    private String code;
    private String value;

    RuntimeState(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public DictDefinition getDefinition() {
        return DictType.RUNTIME_STATE;
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
