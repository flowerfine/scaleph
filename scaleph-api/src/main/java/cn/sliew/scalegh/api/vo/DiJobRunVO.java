package cn.sliew.scalegh.api.vo;

import cn.sliew.scalegh.service.dto.di.DiJobDTO;
import cn.sliew.scalegh.service.vo.DictVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(value = "作业启动参数", description = "作业启动参数")
public class DiJobRunVO {
    @NotNull
    private Long jobId;

    @NotNull
    private Long clusterId;

    private List<DictVO> resources;

    public DiJobDTO toDto() {
        DiJobDTO dto = new DiJobDTO();
        dto.setId(this.jobId);
        dto.setClusterId(this.clusterId);
        return dto;
    }
}
