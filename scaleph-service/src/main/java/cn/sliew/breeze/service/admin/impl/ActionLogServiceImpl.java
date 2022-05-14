package cn.sliew.breeze.service.admin.impl;

import cn.sliew.breeze.dao.entity.LogAction;
import cn.sliew.breeze.dao.mapper.log.LogActionMapper;
import cn.sliew.breeze.service.admin.ActionLogService;
import cn.sliew.breeze.service.convert.admin.LogActionConvert;
import cn.sliew.breeze.service.dto.admin.LogActionDTO;
import cn.sliew.breeze.service.param.admin.ActionLogParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户操作日志 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
@Service
public class ActionLogServiceImpl implements ActionLogService {

    @Autowired
    private LogActionMapper logActionMapper;

    @Override
    public int insert(LogActionDTO logActionDTO) {
        LogAction logAction = LogActionConvert.INSTANCE.toDo(logActionDTO);
        return this.logActionMapper.insert(logAction);
    }

    @Override
    public Page<LogActionDTO> listByPage(ActionLogParam actionLogParam) {
        Page<LogActionDTO> result = new Page<>();
        Page<LogAction> list = this.logActionMapper.selectPage(
                new Page<>(actionLogParam.getCurrent(), actionLogParam.getPageSize()),
                new QueryWrapper<LogAction>()
                        .lambda()
                        .eq(LogAction::getUserName, actionLogParam.getUserName())
                        .gt(LogAction::getActionTime, actionLogParam.getActionTime())
        );
        List<LogActionDTO> dtoList = LogActionConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }
}
