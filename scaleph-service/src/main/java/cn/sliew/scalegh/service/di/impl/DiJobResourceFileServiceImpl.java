package cn.sliew.scalegh.service.di.impl;

import cn.sliew.breeze.dao.entity.master.di.DiJobResourceFile;
import cn.sliew.breeze.dao.entity.master.di.DiResourceFile;
import cn.sliew.breeze.dao.mapper.master.di.DiJobResourceFileMapper;
import cn.sliew.scalegh.service.convert.di.DiResourceFileConvert;
import cn.sliew.scalegh.service.di.DiJobResourceFileService;
import cn.sliew.scalegh.service.dto.di.DiResourceFileDTO;
import cn.sliew.scalegh.service.vo.DictVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 数据集成-作业资源 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2022-04-19
 */
@Service
public class DiJobResourceFileServiceImpl implements DiJobResourceFileService {

    @Autowired
    private DiJobResourceFileMapper diJobResourceFileMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int bindResource(Long jobId, List<DictVO> resources) {
        this.diJobResourceFileMapper.delete(
                new LambdaQueryWrapper<DiJobResourceFile>()
                        .eq(DiJobResourceFile::getJobId, jobId)
        );
        int result = 0;
        for (DictVO dto : resources) {
            DiJobResourceFile jobFile = new DiJobResourceFile();
            jobFile.setJobId(jobId);
            jobFile.setResourceFileId(Long.valueOf(dto.getValue()));
            result += this.diJobResourceFileMapper.insert(jobFile);
        }
        return result;
    }

    @Override
    public List<DiResourceFileDTO> listJobResources(Long jobId) {
        List<DiResourceFile> list = this.diJobResourceFileMapper.listJobResources(jobId);
        return DiResourceFileConvert.INSTANCE.toDto(list);
    }
}
