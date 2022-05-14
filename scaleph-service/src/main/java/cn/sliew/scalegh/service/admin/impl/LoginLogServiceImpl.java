package cn.sliew.scalegh.service.admin.impl;

import cn.sliew.breeze.dao.entity.log.LogLogin;
import cn.sliew.breeze.dao.mapper.log.LogLoginMapper;
import cn.sliew.scalegh.service.admin.LoginLogService;
import cn.sliew.scalegh.service.convert.admin.LogLoginConvert;
import cn.sliew.scalegh.service.dto.admin.LogLoginDTO;
import cn.sliew.scalegh.service.param.admin.LoginLogParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户登录登出日志 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Service
public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    private LogLoginMapper logLoginMapper;

    @Override
    public int insert(LogLoginDTO logLoginDTO) {
        LogLogin log = LogLoginConvert.INSTANCE.toDo(logLoginDTO);
        return this.logLoginMapper.insert(log);
    }

    @Override
    public Page<LogLoginDTO> listByPage(LoginLogParam loginLogParam) {
        Page<LogLoginDTO> result = new Page<>();
        Page<LogLogin> list = this.logLoginMapper.selectPage(
                new Page<>(loginLogParam.getCurrent(), loginLogParam.getPageSize()),
                new QueryWrapper<LogLogin>()
                        .lambda()
                        .eq(LogLogin::getUserName, loginLogParam.getUserName())
                        .gt(LogLogin::getLoginTime, loginLogParam.getLoginTime())
                        .orderByDesc(LogLogin::getLoginTime)
        );
        List<LogLoginDTO> dtoList = LogLoginConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }
}
