package cn.sliew.scaleph.dao.mapper.master.di;

import java.util.List;

import cn.sliew.scaleph.dao.entity.master.di.DiDirectory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 数据集成-项目目录 Mapper 接口
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
@Repository
public interface DiDirectoryMapper extends BaseMapper<DiDirectory> {
    /**
     * 获取目录对应的所有上级节点信息
     *
     * @param ids directory ids
     * @return directory list
     */
    List<DiDirectory> selectFullPath(@Param("ids") List<Long> ids);
}
