package cn.sliew.scaleph.core.di.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.sliew.scaleph.core.di.service.dto.DiDirectoryDTO;

/**
 * <p>
 * 数据集成-项目目录 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-03-10
 */
public interface DiDirectoryService {
    /**
     * 新增
     *
     * @param dto info
     * @return int
     */
    DiDirectoryDTO insert(DiDirectoryDTO dto);

    /**
     * 修改
     *
     * @param dto info
     * @return int
     */
    int update(DiDirectoryDTO dto);

    /**
     * 按id删除
     *
     * @param id id
     * @return int
     */
    int deleteById(Long id);

    /**
     * 按pid删除
     *
     * @param pid pid
     * @return int
     */
    int deleteByPid(Long pid);

    /**
     * 根据项目id列表删除目录
     *
     * @param projectId project ids
     * @return int
     */
    int deleteByProjectIds(Collection<? extends Serializable> projectId);

    /**
     * 获取目录的全路径
     *
     * @param directoryIds dir ids
     * @return dir list
     */
    Map<Long, DiDirectoryDTO> loadFullPath(List<Long> directoryIds);

    /**
     * 查询项目下目录信息
     *
     * @param projectId project id
     * @return list
     */
    List<DiDirectoryDTO> selectByProjectId(Long projectId);

    /**
     * 查询目录下是否有子目录
     *
     * @param projectId project id
     * @param dirId     dir id
     * @return boolean
     */
    boolean hasChildDir(Long projectId, Long dirId);

    /**
     * 查询目录
     *
     * @param dirId id
     * @return dir info
     */
    DiDirectoryDTO selectById(Long dirId);
}
