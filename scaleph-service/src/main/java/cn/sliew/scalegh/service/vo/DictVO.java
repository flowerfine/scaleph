package cn.sliew.scalegh.service.vo;

import cn.sliew.scalegh.common.constant.Constants;
import cn.sliew.scalegh.service.cache.DictCache;
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
