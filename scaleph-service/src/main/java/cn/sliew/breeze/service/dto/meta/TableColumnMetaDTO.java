package cn.sliew.breeze.service.dto.meta;

import cn.sliew.breeze.service.dto.BaseDTO;
import cn.sliew.breeze.service.vo.DictVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lenovo
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "元数据-列信息", description = "元数据-列信息")
public class TableColumnMetaDTO extends BaseDTO {
    private static final long serialVersionUID = -6282945619070309858L;
    private Long id;

    private Long tableId;

    private String columnName;

    private String dataType;

    private Long dataLength;

    private Integer dataPrecision;

    private Integer dataScale;

    private DictVO nullable;

    private String dataDefault;

    private String lowValue;

    private String highValue;

    private Integer columnOrdinal;

    private String columnComment;

    private DictVO isPrimaryKey;
}
