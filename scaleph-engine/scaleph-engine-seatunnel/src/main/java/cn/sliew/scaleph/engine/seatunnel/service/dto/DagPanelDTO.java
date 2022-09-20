package cn.sliew.scaleph.engine.seatunnel.service.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "Dnd Panel", description = "XFlow nodes with collapse panel")
public class DagPanelDTO {

    private String id;

    private String header;

    private List<DagNodeDTO> children;
}
