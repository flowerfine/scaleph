package cn.sliew.scalegh.service.meta;

import cn.sliew.scalegh.service.dto.meta.MetaDataElementDTO;
import cn.sliew.scalegh.service.param.meta.MetaDataElementParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 元数据-数据元信息 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-04-25
 */
public interface MetaDataElementService {

    int insert(MetaDataElementDTO metaDataElementDTO);

    int update(MetaDataElementDTO metaDataElementDTO);

    int deleteById(Long id);

    int deleteBatch(Map<Integer, ? extends Serializable> map);

    Page<MetaDataElementDTO> listByPage(MetaDataElementParam param);
}
