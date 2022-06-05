package cn.sliew.scaleph.engine.seatunnel;

import lombok.Getter;

/**
 * todo submit seatunnel job behaves depends on seatunnel version automatically.
 */
@Getter
public enum SeatunnelVersion {

    V_2_1_0(0, "2.1.0", "org.apache.seatunnel.SeatunnelFlink"),
    V_2_1_1(1, "2.1.1", "org.apache.seatunnel.SeatunnelFlink"),

    DEV(Integer.MAX_VALUE, "dev", "org.apache.seatunnel.core.flink.SeatunnelFlink"),
    ;

    private int code;
    private String version;
    private String entryClass;

    SeatunnelVersion(int code, String version, String entryClass) {
        this.code = code;
        this.version = version;
        this.entryClass = entryClass;
    }
}
