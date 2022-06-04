package cn.sliew.scaleph.engine.seatunnel.service.util;

public enum QuartzJobUtil {
    ;

    public static String getJobName(String projectCode, String jobCode) {
        return String.format("%s_%s", projectCode, jobCode);
    }

    public static String getFlinkBatchJobName(String jobName) {
        return "FLINK_BATCH_JOB_" + jobName;
    }

    public static String getFlinkBatchTriggerKey(String jobName) {
        return "FLINK_BATCH_TRI_" + jobName;
    }
}
