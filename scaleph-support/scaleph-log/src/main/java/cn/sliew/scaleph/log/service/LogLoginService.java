package cn.sliew.scaleph.log.service;

import cn.sliew.scaleph.log.service.dto.LogLoginDTO;
import cn.sliew.scaleph.log.service.param.LogLoginParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 用户登录登出日志 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface LogLoginService {

    /**
     * 新增日志
     *
     * @param logLoginDTO 登录日志
     * @return int
     */
    int insert(LogLoginDTO logLoginDTO);

    /**
     * 分页查询
     *
     * @param logLoginParam 查询参数
     * @return page list
     */
    Page<LogLoginDTO> listByPage(LogLoginParam logLoginParam);
}
