package cn.sliew.scaleph.meta.service;

import cn.sliew.scaleph.meta.service.dto.MetaDataSetDTO;
import cn.sliew.scaleph.meta.service.param.MetaDataSetParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 元数据-参考数据 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-04-25
 */
public interface MetaDataSetService {
    int insert(MetaDataSetDTO metaDataSetDTO);

    int update(MetaDataSetDTO metaDataSetDTO);

    int deleteById(Long id);

    int deleteBatch(Map<Integer, ? extends Serializable> map);

    Page<MetaDataSetDTO> listByPage(MetaDataSetParam param);

    List<MetaDataSetDTO> listByType(Long dataSetTypeId);
}
