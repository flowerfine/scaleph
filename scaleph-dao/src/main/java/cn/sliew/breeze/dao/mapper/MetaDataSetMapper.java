package cn.sliew.breeze.dao.mapper;

import cn.sliew.breeze.dao.entity.MetaDataSet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 元数据-参考数据 Mapper 接口
 * </p>
 *
 * @author liyu
 * @since 2022-04-25
 */
@Repository
public interface MetaDataSetMapper extends BaseMapper<MetaDataSet> {

    Page<MetaDataSet> selectPage(IPage<?> page,
                                 @Param(value = "dataSetTypeCode") String dataSetTypeCode,
                                 @Param(value = "dataSetCode") String dataSetCode,
                                 @Param(value = "dataSetValue") String dataSetValue);
}
