package cn.sliew.scaleph.meta.service;

import cn.sliew.scaleph.meta.service.dto.DataSourceMetaDTO;
import cn.sliew.scaleph.meta.service.param.DataSourceMetaParam;
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

    int insert(DataSourceMetaDTO metaDTO);

    int update(DataSourceMetaDTO metaDTO);

    int deleteById(Long id);

    int deleteBatch(Map<Integer, ? extends Serializable> map);

    DataSourceMetaDTO selectOne(Serializable id);

    Page<DataSourceMetaDTO> listByPage(DataSourceMetaParam param);

    List<DataSourceMetaDTO> listByType(String type);
}
