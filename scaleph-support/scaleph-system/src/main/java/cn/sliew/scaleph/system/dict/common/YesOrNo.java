package cn.sliew.scaleph.system.dict.common;

import cn.sliew.scaleph.system.dict.DictDefinition;
import cn.sliew.scaleph.system.dict.DictInstance;
import cn.sliew.scaleph.system.dict.DictType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum YesOrNo implements DictInstance {

    NO("0", "否"),
    YES("1", "是"),
    ;

    @JsonValue
    @EnumValue
    private String code;
    private String value;

    YesOrNo(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public DictDefinition getDefinition() {
        return DictType.YES_OR_NO;
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
