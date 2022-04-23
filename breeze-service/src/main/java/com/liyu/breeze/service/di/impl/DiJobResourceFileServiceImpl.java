package com.liyu.breeze.service.di.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liyu.breeze.dao.entity.DiJobResourceFile;
import com.liyu.breeze.dao.entity.DiResourceFile;
import com.liyu.breeze.dao.mapper.DiJobResourceFileMapper;
import com.liyu.breeze.service.convert.di.DiResourceFileConvert;
import com.liyu.breeze.service.di.DiJobResourceFileService;
import com.liyu.breeze.service.dto.di.DiResourceFileDTO;
import com.liyu.breeze.service.vo.DictVO;
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
