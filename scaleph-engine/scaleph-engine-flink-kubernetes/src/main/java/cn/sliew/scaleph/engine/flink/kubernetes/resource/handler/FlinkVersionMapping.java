package cn.sliew.scaleph.engine.flink.kubernetes.resource.handler;

import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.common.dict.flink.kubernetes.OperatorFlinkVersion;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.FlinkVersionOption;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum FlinkVersionMapping {

    V_1_15(OperatorFlinkVersion.v1_15, FlinkVersion.V_1_15_4, FlinkVersion.V_1_15_0, FlinkVersion.V_1_15_1, FlinkVersion.V_1_15_2, FlinkVersion.V_1_15_3, FlinkVersion.V_1_15_4),
    V_1_16(OperatorFlinkVersion.v1_16, FlinkVersion.V_1_16_3, FlinkVersion.V_1_16_0, FlinkVersion.V_1_16_1, FlinkVersion.V_1_16_2, FlinkVersion.V_1_16_3),
    V_1_17(OperatorFlinkVersion.v1_17, FlinkVersion.V_1_17_2, FlinkVersion.V_1_17_0, FlinkVersion.V_1_17_1, FlinkVersion.V_1_17_2),
    V_1_18(OperatorFlinkVersion.v1_18, FlinkVersion.V_1_18_0, FlinkVersion.V_1_18_0),
    ;

    private OperatorFlinkVersion majorVersion;
    private FlinkVersion defaultVersion;
    private FlinkVersion[] supportVersions;

    FlinkVersionMapping(OperatorFlinkVersion majorVersion, FlinkVersion defaultVersion, FlinkVersion... supportVersions) {
        this.majorVersion = majorVersion;
        this.defaultVersion = defaultVersion;
        this.supportVersions = supportVersions;
    }

    public static FlinkVersionMapping of(OperatorFlinkVersion majorVersion) {
        for (FlinkVersionMapping mapping : values()) {
            if (mapping.getMajorVersion() == majorVersion) {
                return mapping;
            }
        }
        throw new IllegalStateException("unknown flink version mapping for: " + majorVersion.getValue());
    }

    public FlinkVersionOption toOption() {
        FlinkVersionOption option = new FlinkVersionOption();
        option.setLabel(getMajorVersion().getValue());
        option.setOptions(Arrays.asList(getSupportVersions()));
        return option;
    }
}
