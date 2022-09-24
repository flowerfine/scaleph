package cn.sliew.scaleph.engine.seatunnel.service.dto;

import lombok.Data;

import java.util.Map;

@Data
public class DagNodeDTO {

    private String id;

    private String label;

    private String renderKey;

    private String description;
    /*
     * node type and node name
     */
    private Map<String, String> data;
}
