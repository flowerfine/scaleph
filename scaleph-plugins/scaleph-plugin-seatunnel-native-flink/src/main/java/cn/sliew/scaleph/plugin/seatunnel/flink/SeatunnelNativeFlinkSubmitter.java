package cn.sliew.scaleph.plugin.seatunnel.flink;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import com.google.common.graph.MutableGraph;
import org.apache.flink.configuration.Configuration;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
     * fixme 放在这里，不合适。。。
     */
    public URL buildConfigFile(MutableGraph<Map<String, String>> graph) {
        return null;
    }
}
