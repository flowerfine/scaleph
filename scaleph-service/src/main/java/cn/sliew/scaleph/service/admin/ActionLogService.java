package cn.sliew.scaleph.service.admin;

import cn.sliew.scaleph.service.dto.admin.LogActionDTO;
import cn.sliew.scaleph.service.param.admin.ActionLogParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 用户操作日志 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface ActionLogService {
    /**
     * 新增日志
     *
     * @param logActionDTO 登录日志
     * @return int
     */
    int insert(LogActionDTO logActionDTO);

    /**
     * 分页查询
     *
     * @param actionLogParam 查询参数
     * @return page list
     */
    Page<LogActionDTO> listByPage(ActionLogParam actionLogParam);
}
