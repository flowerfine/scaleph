package cn.sliew.scaleph.engine.seatunnel;

import lombok.Getter;

/**
 * todo submit seatunnel job behaves depends on seatunnel version automatically.
 */
@Getter
public enum FlinkRelease {

//    V_1_13_6(0, "1.13", "1.13.6", "flink-1.13.6-bin-scala_2.11.tgz", "https://archive.apache.org/dist/flink/flink-1.13.6/flink-1.13.6-bin-scala_2.11.tgz"),
    V_1_13_6(0, "1.13", "1.13.6", "flink-1.13.6-bin-scala_2.11.tgz", "https://dlcdn.apache.org/flink/flink-1.13.6/flink-1.13.6-bin-scala_2.11.tgz"),
    V_1_14_4(1, "1.14", "1.14.4", "flink-1.14.4-bin-scala_2.11.tgz", "https://archive.apache.org/dist/flink/flink-1.14.4/flink-1.14.4-bin-scala_2.11.tgz"),
    V_1_15_0(2, "1.15", "1.15.0", "flink-1.15.0-bin-scala_2.12.tgz", "https://archive.apache.org/dist/flink/flink-1.15.0/flink-1.15.0-bin-scala_2.12.tgz"),
    ;

    private int code;
    private String majorVersion;
    private String fullVersion;
    private String releaseName;
    private String releaseUrl;

    FlinkRelease(int code, String majorVersion, String fullVersion, String releaseName, String releaseUrl) {
        this.code = code;
        this.majorVersion = majorVersion;
        this.fullVersion = fullVersion;
        this.releaseName = releaseName;
        this.releaseUrl = releaseUrl;
    }
}
