package cn.sliew.scaleph.engine.flink.service.impl;

import cn.sliew.scaleph.core.di.service.DiClusterConfigService;
import cn.sliew.scaleph.core.di.service.dto.DiClusterConfigDTO;
import cn.sliew.scaleph.engine.flink.service.FlinkClusterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FlinkClusterServiceImpl implements FlinkClusterService {

    @Autowired
    private DiClusterConfigService diClusterConfigService;

    @Override
    public Configuration buildConfiguration(Long clusterId) {
        //build configuration
        DiClusterConfigDTO clusterConfig = diClusterConfigService.selectOne(clusterId);
        return buildConfiguration(seatunnelPath, seatunnelJarPath, diJobDTO, clusterConfig.getConfig(), baseDir);
    }
}
