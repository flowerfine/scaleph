package cn.sliew.scaleph.plugin.seatunnel.flink;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import org.apache.flink.configuration.Configuration;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * fixme flink job submit and seatunnel conf generate
 */
public class SeatunnelNativeFlinkSubmitter {

    /**
     * 环境变量
     */
    public List<PropertyDescriptor> getSupportedEnvProperties() {
        return Collections.emptyList();
    }

    public void submit(Properties properties) {

    }

    public Configuration buildConfiguration() {
        return null;
    }

    public PackageJarJob buildPackageJar() {
        return null;
    }

    /**
     * todo connector graph + env
     */
    public URL buildConfigFile() {
        return null;
    }
}
