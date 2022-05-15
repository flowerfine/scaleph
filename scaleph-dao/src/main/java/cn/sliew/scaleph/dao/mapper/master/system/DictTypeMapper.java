package cn.sliew.scaleph.dao.mapper.master.system;

import cn.sliew.scaleph.dao.entity.master.system.DictType;
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
