package cn.sliew.scaleph.engine.seatunnel;

import lombok.Getter;

/**
 * todo submit seatunnel job behaves depends on seatunnel version automatically.
 */
@Getter
public enum SeatunnelRelease {

    V_2_1_0(0, "2.1.0", "apache-seatunnel-incubating-2.1.0-bin.tar.gz","org.apache.seatunnel.SeatunnelFlink", "https://archive.apache.org/dist/incubator/seatunnel/2.1.0/apache-seatunnel-incubating-2.1.0-bin.tar.gz"),
    V_2_1_1(1, "2.1.1", "apache-seatunnel-incubating-2.1.1-bin.tar.gz","org.apache.seatunnel.SeatunnelFlink", "https://archive.apache.org/dist/incubator/seatunnel/2.1.1/apache-seatunnel-incubating-2.1.1-bin.tar.gz"),

    DEV(Integer.MAX_VALUE, "dev", "unsupported","org.apache.seatunnel.core.flink.SeatunnelFlink", "unsupported"),
    ;

    private int code;
    private String version;
    private String releaseName;
    private String entryClass;
    private String releaseUrl;

    SeatunnelRelease(int code, String version, String releaseName, String entryClass, String releaseUrl) {
        this.code = code;
        this.version = version;
        this.releaseName = releaseName;
        this.entryClass = entryClass;
        this.releaseUrl = releaseUrl;
    }
}
