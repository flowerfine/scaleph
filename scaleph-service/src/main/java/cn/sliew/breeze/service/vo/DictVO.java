package cn.sliew.breeze.service.vo;

import cn.sliew.breeze.common.constant.Constants;
import cn.sliew.breeze.service.cache.DictCache;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gleiyu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "数据字典", description = "数据字典对象，用来前后端枚举值交互")
public class DictVO {
    private String value;
    private String label;

    public static DictVO toVO(String dictTypeCode, String dictCode) {
        String dictValue = DictCache.getValueByKey(dictTypeCode + Constants.SEPARATOR + dictCode);
        return new DictVO(dictCode, dictValue);
    }
}
