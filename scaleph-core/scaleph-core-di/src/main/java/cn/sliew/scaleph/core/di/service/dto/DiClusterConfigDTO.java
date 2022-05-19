package cn.sliew.scaleph.core.di.service.dto;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.common.dto.BaseDTO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 数据集成-集群配置
 * </p>
 *
 * @author liyu
 * @since 2022-04-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "DiClusterConfig对象", description = "数据集成-集群配置")
public class DiClusterConfigDTO extends BaseDTO {

    private static final long serialVersionUID = -7439123852299668659L;

    @NotBlank
    @Length(max = 128)
    @ApiModelProperty(value = "集群名称")
    private String clusterName;

    @NotNull
    @ApiModelProperty(value = "集群类型")
    private DictVO clusterType;

    @ApiModelProperty(value = "集群home文件目录地址")
    private String clusterHome;

    @ApiModelProperty(value = "集群版本")
    private String clusterVersion;

    @NotBlank
    @ApiModelProperty(value = "配置信息json格式")
    private String clusterConf;

    @ApiModelProperty(value = "备注")
    private String remark;

    public Map<String, String> getConfig() {
        Map<String, String> map = new HashMap<>();
        if (StrUtil.isNotEmpty(this.clusterConf)) {
            String[] lines = this.clusterConf.split("\n");
            for (String line : lines) {
                String[] kv = line.split("=");
                if (kv.length == 2 && StrUtil.isAllNotBlank(kv)) {
                    map.put(kv[0], kv[1]);
                }
            }
        }
        return map;
    }

}
