package cn.sliew.scalegh.service.meta;

import cn.sliew.scalegh.service.dto.meta.DataSourceMetaDTO;
import cn.sliew.scalegh.service.param.meta.DataSourceMetaParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 元数据-数据源连接信息 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-10-17
 */

public interface DataSourceMetaService {
    /**
     * 新增
     *
     * @param metaDTO meta
     * @return int
     */
    int insert(DataSourceMetaDTO metaDTO);

    /**
     * 更新
     *
     * @param metaDTO meta
     * @return int
     */
    int update(DataSourceMetaDTO metaDTO);

    /**
     * 删除
     *
     * @param id
     * @return int
     */
    int deleteById(Long id);

    /**
     * 根据主键id批量删除
     *
     * @param map ids
     * @return int
     */
    int deleteBatch(Map<Integer, ? extends Serializable> map);

    /**
     * 根据id查询
     *
     * @param id id
     * @return meta
     */
    DataSourceMetaDTO selectOne(Serializable id);

    /**
     * 分页查询
     *
     * @param param 参数
     * @return page
     */
    Page<DataSourceMetaDTO> listByPage(DataSourceMetaParam param);

    /**
     * 按类型查询数据源信息
     *
     * @param type type
     * @return list
     */
    List<DataSourceMetaDTO> listByType(String type);
}
