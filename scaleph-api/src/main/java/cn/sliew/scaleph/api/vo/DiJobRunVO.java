package cn.sliew.scaleph.api.vo;

import javax.validation.constraints.NotNull;
import java.util.List;

import cn.sliew.scaleph.core.di.service.dto.DiJobDTO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

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
