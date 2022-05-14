package cn.sliew.scalegh.service.admin;

import cn.sliew.scalegh.service.dto.admin.DictTypeDTO;
import cn.sliew.scalegh.service.param.admin.DictTypeParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据字典类型 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-07-24
 */
public interface DictTypeService {
    /**
     * 新增字典类型
     *
     * @param dictTypeDTO dict type
     * @return int
     */
    int insert(DictTypeDTO dictTypeDTO);

    /**
     * 更新字典类型
     *
     * @param dictTypeDTO dict type
     * @return int
     */
    int update(DictTypeDTO dictTypeDTO);

    /**
     * 根据主键id删除数据
     *
     * @param id id
     * @return int
     */
    int deleteById(Long id);

    /**
     * 根据主键id批量删除
     *
     * @param map ids
     * @return int
     */
    int deleteBatch(Map<Integer, ? extends Serializable> map);

    /**
     * 根据主键id查询
     *
     * @param id id
     * @return DictTypeDTO
     */
    DictTypeDTO selectOne(Long id);

    /**
     * 根据dictTypeCode查询
     *
     * @param dictTypeCode dictTypeCode
     * @return DictTypeDTO
     */
    DictTypeDTO selectOne(String dictTypeCode);

    /**
     * 分页查询
     *
     * @param dictTypeParam 参数
     * @return page
     */
    Page<DictTypeDTO> listByPage(DictTypeParam dictTypeParam);

    /**
     * 查询全部
     *
     * @return list
     */
    List<DictTypeDTO> selectAll();

}
