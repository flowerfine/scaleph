package cn.sliew.breeze.dao.mapper;

import cn.sliew.breeze.dao.entity.MetaDataMap;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 元数据-参考数据映射 Mapper 接口
 * </p>
 *
 * @author liyu
 * @since 2022-04-25
 */
@Repository
public interface MetaDataMapMapper extends BaseMapper<MetaDataMap> {

    Page<MetaDataMap> selectPage(IPage<?> page,
                                 @Param(value = "srcDataSetTypeCode") String srcDataSetTypeCode,
                                 @Param(value = "tgtDataSetTypeCode") String tgtDataSetTypeCode,
                                 @Param(value = "srcDataSetCode") String srcDataSetCode,
                                 @Param(value = "tgtDataSetCode") String tgtDataSetCode,
                                 @Param(value = "auto") boolean auto);
}
