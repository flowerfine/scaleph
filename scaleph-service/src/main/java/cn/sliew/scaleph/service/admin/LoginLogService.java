package cn.sliew.scaleph.service.admin;

import cn.sliew.scaleph.service.dto.admin.LogLoginDTO;
import cn.sliew.scaleph.service.param.admin.LoginLogParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 用户登录登出日志 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface LoginLogService {

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
     * @param loginLogParam 查询参数
     * @return page list
     */
    Page<LogLoginDTO> listByPage(LoginLogParam loginLogParam);
}
