package cn.sliew.scaleph.plugin.seatunnel.flink.conf;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseSourceConnectorOptions {

    @JsonProperty("source_table_name")
    private String sourceTableName;

    @JsonProperty("field_name")
    private String fieldName;


}
