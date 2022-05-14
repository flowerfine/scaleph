package cn.sliew.scalegh.service.admin;

import cn.sliew.scalegh.service.dto.admin.MessageDTO;
import cn.sliew.scalegh.service.param.admin.MessageParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 站内信表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-08-01
 */
public interface MessageService {
    /**
     * 新增
     *
     * @param messageDTO 消息对象
     * @return int
     */
    int insert(MessageDTO messageDTO);

    /**
     * 修改
     *
     * @param messageDTO dict
     * @return int
     */
    int update(MessageDTO messageDTO);

    /**
     * 查询未读消息数量
     *
     * @param receiver 收件人
     * @return 消息数量
     */
    int countUnReadMsg(String receiver);

    /**
     * 分页查询
     *
     * @param messageParam 查询参数
     * @return page list
     */
    Page<MessageDTO> listByPage(MessageParam messageParam);

    /**
     * 全部读取
     *
     * @param receiver 收件人
     * @return int
     */
    int readAll(String receiver);
}
