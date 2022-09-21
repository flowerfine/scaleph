package cn.sliew.scaleph.system.dict.job;

import cn.sliew.scaleph.system.dict.DictDefinition;
import cn.sliew.scaleph.system.dict.DictInstance;
import cn.sliew.scaleph.system.dict.DictType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskResult implements DictInstance {

    SUCCESS("success", "成功"),
    FAILURE("failure", "失败"),
    ;

    @JsonValue
    @EnumValue
    private String code;
    private String value;

    TaskResult(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public DictDefinition getDefinition() {
        return DictType.TASK_RESULT;
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
