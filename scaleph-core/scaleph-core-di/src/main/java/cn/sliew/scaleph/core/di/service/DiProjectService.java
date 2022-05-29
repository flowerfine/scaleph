package cn.sliew.scaleph.core.di.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cn.sliew.scaleph.core.di.service.dto.DiProjectDTO;
import cn.sliew.scaleph.core.di.service.param.DiProjectParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 数据集成-项目信息 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-03-09
 */
public interface DiProjectService {
    /**
     * insert
     *
     * @param dto info
     * @return int
     */
    int insert(DiProjectDTO dto);

    /**
     * update
     *
     * @param dto info
     * @return int
     */
    int update(DiProjectDTO dto);

    /**
     * delete one
     *
     * @param id id
     * @return int
     */
    int deleteById(Long id);

    /**
     * 批量删除
     *
     * @param map ids
     * @return int
     */
    int deleteBatch(Map<Integer, ? extends Serializable> map);

    /**
     * 分页查询
     *
     * @param param param
     * @return pages data
     */
    Page<DiProjectDTO> listByPage(DiProjectParam param);

    /**
     * 查询全部
     *
     * @return
     */
    List<DiProjectDTO> listAll();

    Long totalCnt();

    DiProjectDTO selectOne(Serializable id);
}
