package cn.sliew.scaleph.service.dto.meta;

import cn.sliew.scaleph.service.dto.BaseDTO;
import cn.sliew.scaleph.service.vo.DictVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author gleiyu
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "元数据-表信息", description = "元数据-表信息")
public class TableMetaDTO extends BaseDTO {
    private static final long serialVersionUID = -5777242055646416476L;

    private Long id;

    private Long dataSourceId;
    /**
     * 表所属的catalog 可能为null
     */
    private String tableCatalog;
    /**
     * 表所属的 schema
     */
    private String tableSchema;
    /**
     * 表名称
     */
    private String tableName;
    /**
     * 表的类型 TABLE/VIEW/
     * 调整为枚举类型，统一各种数据源
     */
    private DictVO tableType;
    /**
     * 表空间
     */
    private String tableSpace;
    /**
     * 表的备注
     */
    private String tableComment;
    /**
     * 数据量大小，非精确数据量
     */
    private Long tableRows;
    /**
     * 数据占用存储大小
     */
    private Long dataBytes;
    /**
     * 索引占用存储大小
     */
    private Long indexBytes;
    /**
     * 创建时间
     */
    private Date tableCreateTime;
    /**
     * 最后ddl时间
     */
    private Date lastDdlTime;
    /**
     * 最后数据查看时间
     */
    private Date lastAccessTime;
    /**
     * 生命周期 单位天
     */
    private Integer lifeCycle;
    /**
     * 是否分区表
     */
    private DictVO isPartitioned;
    /**
     * 扩展属性
     */
    private Map<String, String> attrs;
    /**
     * 字段
     */
    List<TableColumnMetaDTO> columns;
}
