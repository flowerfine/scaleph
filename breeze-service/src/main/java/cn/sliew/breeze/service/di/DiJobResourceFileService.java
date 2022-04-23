package cn.sliew.breeze.service.di;

import cn.sliew.breeze.service.dto.di.DiResourceFileDTO;
import cn.sliew.breeze.service.vo.DictVO;

import java.util.List;

/**
 * <p>
 * 数据集成-作业资源 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-04-19
 */
public interface DiJobResourceFileService {
    int bindResource(Long jobId, List<DictVO> resources);

    List<DiResourceFileDTO> listJobResources(Long jobId);
}
