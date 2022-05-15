package cn.sliew.scaleph.dao.entity.master.system;

import cn.sliew.scaleph.common.constant.Constants;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据字典表
 * </p>
 *
 * @author liyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_dict", resultMap = "DictMap")
@ApiModel(value = "Dict对象", description = "数据字典表")
public class Dict extends BaseDO {

    private static final long serialVersionUID = -4136245238746831595L;

    @ApiModelProperty(value = "字典类型编码")
    private String dictTypeCode;

    @ApiModelProperty(value = "字典编码")
    private String dictCode;

    @ApiModelProperty(value = "字典值")
    private String dictValue;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否有效")
    private String isValid;

    @TableField(exist = false)
    private DictType dictType;

    public String getKey() {
        return this.getDictType().getDictTypeCode() + Constants.SEPARATOR + this.getDictCode();
    }
}
