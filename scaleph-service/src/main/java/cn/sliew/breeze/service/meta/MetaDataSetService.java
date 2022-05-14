package cn.sliew.breeze.service.meta;

import cn.sliew.breeze.service.dto.meta.MetaDataSetDTO;
import cn.sliew.breeze.service.param.meta.MetaDataSetParam;
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
