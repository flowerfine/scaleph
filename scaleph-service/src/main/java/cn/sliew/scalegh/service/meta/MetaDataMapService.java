package cn.sliew.scalegh.service.meta;

import cn.sliew.scalegh.service.dto.meta.MetaDataMapDTO;
import cn.sliew.scalegh.service.param.meta.MetaDataMapParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 元数据-参考数据映射 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-04-25
 */
public interface MetaDataMapService {
    int insert(MetaDataMapDTO metaDataMapDTO);

    int update(MetaDataMapDTO metaDataMapDTO);

    int deleteById(Long id);

    int deleteBatch(Map<Integer, ? extends Serializable> map);

    Page<MetaDataMapDTO> listByPage(MetaDataMapParam param);
}
