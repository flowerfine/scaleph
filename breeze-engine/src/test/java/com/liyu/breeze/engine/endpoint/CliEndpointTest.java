package com.liyu.breeze.engine.endpoint;

import cn.sliew.flinkful.cli.base.CliClient;
import cn.sliew.flinkful.cli.base.PackageJarJob;
import cn.sliew.flinkful.cli.descriptor.DescriptorCliClient;
import cn.sliew.flinkful.common.enums.DeploymentTarget;
import org.apache.flink.client.deployment.executors.RemoteExecutor;
import org.apache.flink.configuration.*;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * 1.添加 mysql-connector-java.jar 依赖到项目中
 * 2.运行单元测试
 */
class CliClientTest {

    private String mysqlPath = "/Users/wangqi/Documents/software/repository/mysql/mysql-connector-java/8.0.28/mysql-connector-java-8.0.28.jar";
//    private String mysqlPath = "/Library/Maven/repository/mysql/mysql-connector-java/8.0.28/mysql-connector-java-8.0.28.jar";

    private String seatunnelHome = "/Users/wangqi/Downloads/apache-seatunnel-incubating-2.0.5-SNAPSHOT";
    private String seatunnelPath = seatunnelHome + "/lib/seatunnel-core-flink.jar";

    @Test
    void testStandaloneSubmit() throws Exception {
        CliClient client = new DescriptorCliClient();
        client.submit(DeploymentTarget.STANDALONE_SESSION, buildConfiguration(), buildJarJob());
    }

    /**
     * 通过 {@link PipelineOptions#JARS} 将任务 jar 包和对应的依赖都可以一起传到 JobManager。
     * 通过这种方式，可以避免在 JobManager 手动添加 seatunnel-core-flink.jar 或 mysql-connector-java.jar
     */
    private Configuration buildConfiguration() throws MalformedURLException {
        Configuration configuration = new Configuration();
//        configuration.setString(JobManagerOptions.ADDRESS, "172.16.202.119");
//        configuration.setInteger(JobManagerOptions.PORT, 32431);
//        configuration.setInteger(RestOptions.PORT, 32642);

        configuration.setString(JobManagerOptions.ADDRESS, "localhost");
        configuration.setInteger(JobManagerOptions.PORT, 6123);
        configuration.setInteger(RestOptions.PORT, 8081);
        URL seatunnelURL = new File(seatunnelPath).toURL();
        URL mysqlURL = new File(mysqlPath).toURL();
        List<URL> jars = Arrays.asList(seatunnelURL, mysqlURL);
        ConfigUtils.encodeCollectionToConfig(configuration, PipelineOptions.JARS, jars, Object::toString);
        configuration.setString(DeploymentOptions.TARGET, RemoteExecutor.NAME);
        return configuration;
    }

    private PackageJarJob buildJarJob() throws IOException {
        PackageJarJob job = new PackageJarJob();
        job.setJarFilePath(seatunnelPath);
        job.setEntryPointClass("org.apache.seatunnel.SeatunnelFlink");
        URL resource = getClass().getClassLoader().getResource("flink_jdbc_file.conf");
        job.setProgramArgs(new String[]{"--config", resource.getPath()});
        job.setClasspaths(Arrays.asList());
        job.setSavepointSettings(SavepointRestoreSettings.none());
        return job;
    }
}
