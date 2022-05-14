package cn.sliew.breeze.service.admin.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.sliew.breeze.common.constant.Constants;
import cn.sliew.breeze.dao.entity.master.system.SystemConfig;
import cn.sliew.breeze.dao.mapper.master.system.SystemConfigMapper;
import cn.sliew.breeze.service.admin.SystemConfigService;
import cn.sliew.breeze.service.convert.admin.SystemConfigConvert;
import cn.sliew.breeze.service.dto.admin.SystemConfigDTO;
import cn.sliew.breeze.service.vo.BasicConfigVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统配置信息表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-10-10
 */
@Service
public class SystemConfigServiceImpl implements SystemConfigService {

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Override
    public int insert(SystemConfigDTO systemConfigDTO) {
        SystemConfig systemConfig = SystemConfigConvert.INSTANCE.toDo(systemConfigDTO);
        return this.systemConfigMapper.insert(systemConfig);
    }

    @Override
    public int updateByCode(SystemConfigDTO systemConfigDTO) {
        SystemConfig systemConfig = SystemConfigConvert.INSTANCE.toDo(systemConfigDTO);
        return this.systemConfigMapper.update(systemConfig, new LambdaQueryWrapper<SystemConfig>()
                .eq(SystemConfig::getCfgCode, systemConfig.getCfgCode()));
    }

    @Override
    public int deleteByCode(String code) {
        if (StrUtil.isEmpty(code)) {
            return 0;
        } else {
            return this.systemConfigMapper.delete(new LambdaQueryWrapper<SystemConfig>()
                    .eq(SystemConfig::getCfgCode, code));
        }

    }

    @Override
    public SystemConfigDTO selectByCode(String code) {
        SystemConfig systemConfig = this.systemConfigMapper.selectOne(new LambdaQueryWrapper<SystemConfig>()
                .eq(SystemConfig::getCfgCode, code));
        return SystemConfigConvert.INSTANCE.toDto(systemConfig);
    }

    @Override
    public String getSeatunnelHome() {
        SystemConfigDTO systemConfig = this.selectByCode(Constants.CFG_BASIC_CODE);
        if (systemConfig != null) {
            BasicConfigVO config = JSONUtil.toBean(systemConfig.getCfgValue(), BasicConfigVO.class);
            return config.getSeatunnelHome();
        } else {
            return null;
        }
    }
}
