package cn.sliew.breeze.dao.mapper;

import cn.sliew.breeze.dao.entity.DictType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 数据字典类型 Mapper 接口
 * </p>
 *
 * @author liyu
 * @since 2021-07-24
 */
@Repository
public interface DictTypeMapper extends BaseMapper<DictType> {
    /**
     * 根据dict type code 查询
     *
     * @param dictTypeCode dictTypeCode
     * @return DictType
     */
    DictType selectByDictTypeCode(String dictTypeCode);
}
