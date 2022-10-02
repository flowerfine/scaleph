package cn.sliew.scaleph.core.di.service.vo;

import lombok.Data;

import java.util.Map;

@Data
public class NodeCellVO {
    private String id;
    private String label;
    private Map<String, Object> data;
    private Integer x;
    private Integer y;
}
