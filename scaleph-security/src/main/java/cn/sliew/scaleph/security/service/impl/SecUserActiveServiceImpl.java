package cn.sliew.scaleph.security.service.impl;

import java.util.Date;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.dao.entity.master.security.SecUserActive;
import cn.sliew.scaleph.dao.mapper.master.security.SecUserActiveMapper;
import cn.sliew.scaleph.security.service.SecUserActiveService;
import cn.sliew.scaleph.security.service.convert.SecUserActiveConvert;
import cn.sliew.scaleph.security.service.dto.SecUserActiveDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户邮箱激活日志表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-09-30
 */
@Service
public class SecUserActiveServiceImpl implements SecUserActiveService {

    @Autowired
    private SecUserActiveMapper secUserActiveMapper;

    @Override
    public int insert(SecUserActiveDTO secUserActiveDTO) {
        SecUserActive secUserActive = SecUserActiveConvert.INSTANCE.toDo(secUserActiveDTO);
        return this.secUserActiveMapper.insert(secUserActive);
    }

    @Override
    public int updateByUserAndCode(SecUserActiveDTO secUserActiveDTO) {
        if (secUserActiveDTO != null && StrUtil.isEmpty(secUserActiveDTO.getUserName()) &&
            StrUtil.isEmpty(secUserActiveDTO.getActiveCode())) {
            secUserActiveDTO.setActiveTime(new Date());
            SecUserActive secUserActive = SecUserActiveConvert.INSTANCE.toDo(secUserActiveDTO);
            return this.secUserActiveMapper.update(secUserActive, new LambdaQueryWrapper<SecUserActive>()
                .eq(SecUserActive::getActiveCode, secUserActive.getActiveCode())
                .eq(SecUserActive::getUserName, secUserActive.getUserName())
            );
        }
        return 0;
    }

    @Override
    public SecUserActiveDTO selectOne(String userName, String activeCode) {
        SecUserActive
            secUserActive = this.secUserActiveMapper.selectOne(new LambdaQueryWrapper<SecUserActive>()
            .eq(SecUserActive::getUserName, userName)
            .eq(SecUserActive::getActiveCode, activeCode)
        );
        return SecUserActiveConvert.INSTANCE.toDto(secUserActive);
    }
}
