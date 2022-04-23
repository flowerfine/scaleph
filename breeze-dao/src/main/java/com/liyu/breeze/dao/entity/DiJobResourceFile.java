package com.liyu.breeze.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 数据集成-作业资源
 * </p>
 *
 * @author liyu
 * @since 2022-04-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("di_job_resource_file")
@ApiModel(value="DiJobResourceFile对象", description="数据集成-作业资源")
public class DiJobResourceFile extends BaseDO {

    private static final long serialVersionUID = -1673520716698785012L;

    @ApiModelProperty(value = "作业id")
    private Long jobId;

    @ApiModelProperty(value = "资源id")
    private Long resourceFileId;


}
