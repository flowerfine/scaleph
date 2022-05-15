package cn.sliew.scaleph.service.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gleiyu
 */
@Data
public class BaseDTO implements Serializable {

    private static final long serialVersionUID = -3170630380110141492L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 创建人
     */
    @TableField(value = "creator", fill = FieldFill.INSERT)
    private String creator;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 修改人
     */
    @TableField(value = "editor", fill = FieldFill.INSERT_UPDATE)
    private String editor;
    /**
     * 修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
