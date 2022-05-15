package cn.sliew.scaleph.service.meta;

import cn.sliew.scaleph.service.dto.meta.MetaSystemDTO;
import cn.sliew.scaleph.service.param.meta.MetaSystemParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 元数据-业务系统信息 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-01-15
 */
public interface MetaSystemService {
    /**
     * 新增
     *
     * @param metaSystem metaSystem info
     * @return int
     */
    int insert(MetaSystemDTO metaSystem);

    /**
     * 修改
     *
     * @param metaSystem metaSystem info
     * @return int
     */
    int update(MetaSystemDTO metaSystem);

    /**
     * 删除一条
     *
     * @param id id
     * @return int
     */
    int deleteById(Long id);

    /**
     * 批量删除
     *
     * @param map ids
     * @return int
     */
    int deleteBatch(Map<Integer, ? extends Serializable> map);

    /**
     * 分页查询
     *
     * @param param param
     * @return pages data
     */
    Page<MetaSystemDTO> listByPage(MetaSystemParam param);
}
