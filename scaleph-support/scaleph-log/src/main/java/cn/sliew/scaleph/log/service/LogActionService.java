package cn.sliew.scaleph.log.service;

import cn.sliew.scaleph.log.service.dto.LogActionDTO;
import cn.sliew.scaleph.log.service.param.LogActionParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 用户操作日志 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface LogActionService {
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
     * @param logActionParam 查询参数
     * @return page list
     */
    Page<LogActionDTO> listByPage(LogActionParam logActionParam);
}
