package cn.sliew.scaleph.api.controller.datadev;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConnectorService;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.ConnectorType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Api(tags = "数据开发-SeaTunnel connector")
@RestController
@RequestMapping(path = "/api/datadev/seatunnel/connector")
public class SeatunnelConnectorController {

    @Autowired
    private SeatunnelConnectorService seatunnelConnectorService;

    @Logging
    @GetMapping("properties/env")
    @ApiOperation(value = "查询 SeaTunnel env 支持的属性", notes = "获取 env 分类属性")
    public List<PropertyDescriptor> getSupportedEnvProperties() {
        return seatunnelConnectorService.getSupportedEnvProperties();
    }

    @Logging
    @GetMapping("{type}")
    @ApiOperation(value = "查询 SeaTunnel connector 列表", notes = "查询 source, transform, sink 类型的 connector")
    public Set<PluginInfo> getAvailableConnectors(@PathVariable("type") ConnectorType type) {
        return seatunnelConnectorService.getAvailableConnectors(type);
    }

    @Logging
    @GetMapping("/properties/connector")
    @ApiOperation(value = "查询 SeaTunnel connector 支持的属性")
    public List<PropertyDescriptor> getSupportedProperties(@Valid PluginInfo pluginInfo) {
        return seatunnelConnectorService.getSupportedProperties(pluginInfo);
    }

}
