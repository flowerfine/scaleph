package cn.sliew.breeze.service.di;

import cn.sliew.breeze.service.dto.di.DiResourceFileDTO;
import cn.sliew.breeze.service.param.di.DiResourceFileParam;
import cn.sliew.breeze.service.vo.DictVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据集成-资源 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-04-13
 */
public interface DiResourceFileService {

    int insert(DiResourceFileDTO dto);

    int update(DiResourceFileDTO dto);

    int deleteByProjectId(Collection<? extends Serializable> projectIds);

    int deleteById(Long id);

    int deleteBatch(Map<Integer, ? extends Serializable> map);

    Page<DiResourceFileDTO> listByPage(DiResourceFileParam param);

    List<DiResourceFileDTO> listByIds(Collection<? extends Serializable> ids);

    List<DictVO> listByProjectId(Long projectId);
}
