package cn.sliew.scaleph.system.dict.common;

import cn.sliew.scaleph.system.dict.DictDefinition;
import cn.sliew.scaleph.system.dict.DictInstance;
import cn.sliew.scaleph.system.dict.DictType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum IsDeleted implements DictInstance {

    NO(YesOrNo.NO.getCode(), YesOrNo.NO.getValue()),
    YES(YesOrNo.YES.getCode(), YesOrNo.YES.getValue()),
    ;

    @JsonValue
    @EnumValue
    private String code;
    private String value;

    IsDeleted(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public DictDefinition getDefinition() {
        return DictType.IS_DELETED;
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
