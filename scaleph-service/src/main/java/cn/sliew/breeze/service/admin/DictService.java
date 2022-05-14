package cn.sliew.breeze.service.admin;

import cn.sliew.breeze.service.dto.admin.DictDTO;
import cn.sliew.breeze.service.param.admin.DictParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据字典表 服务类
 * </p>
 *
 * @author liyu
 * @since 2021-07-24
 */
public interface DictService {
    /**
     * 新增
     *
     * @param dictDTO dict
     * @return int
     */
    int insert(DictDTO dictDTO);

    /**
     * 修改
     *
     * @param dictDTO dict
     * @return int
     */
    int update(DictDTO dictDTO);

    /**
     * 根据主键id删除
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
     * 根据类型删除
     *
     * @param dictCodeType dictCodeType
     * @return int
     */
    int deleteByType(String dictCodeType);

    /**
     * 根据主键id查询
     *
     * @param id id
     * @return DictDTO
     */
    DictDTO selectOne(Long id);

    /**
     * 根据dictTypeCode查询
     *
     * @param dictTypeCode dictTypeCode
     * @return DictTypeDTO
     */
    List<DictDTO> selectByType(String dictTypeCode);

    /**
     * 查询全部
     *
     * @return list
     */
    List<DictDTO> selectAll();

    /**
     * 分页查询
     *
     * @param param 参数
     * @return page
     */
    Page<DictDTO> listByPage(DictParam param);
}
