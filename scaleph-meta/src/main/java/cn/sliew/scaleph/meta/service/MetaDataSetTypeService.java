package cn.sliew.scaleph.meta.service;

import java.io.Serializable;
import java.util.Map;

import cn.sliew.scaleph.meta.service.dto.MetaDataSetTypeDTO;
import cn.sliew.scaleph.meta.service.param.MetaDataSetTypeParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 元数据-参考数据类型 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-04-25
 */
public interface MetaDataSetTypeService {

    int insert(MetaDataSetTypeDTO metaDataSetTypeDTO);

    int update(MetaDataSetTypeDTO metaDataSetTypeDTO);

    int deleteById(Long id);

    int deleteBatch(Map<Integer, ? extends Serializable> map);

    Page<MetaDataSetTypeDTO> listByPage(MetaDataSetTypeParam param);
}
