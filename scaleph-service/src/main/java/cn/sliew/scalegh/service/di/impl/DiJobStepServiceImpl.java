package cn.sliew.scalegh.service.di.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.sliew.scalegh.dao.entity.master.di.DiJobStep;
import cn.sliew.scalegh.dao.mapper.master.di.DiJobStepMapper;
import cn.sliew.scalegh.service.convert.di.DiJobStepConvert;
import cn.sliew.scalegh.service.di.DiJobStepAttrService;
import cn.sliew.scalegh.service.di.DiJobStepService;
import cn.sliew.scalegh.service.dto.di.DiJobStepDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author gleiyu
 */
@Service
public class DiJobStepServiceImpl implements DiJobStepService {

    @Autowired
    private DiJobStepMapper diJobStepMapper;

    @Autowired
    private DiJobStepAttrService diJobStepAttrService;

    @Override
    public int update(DiJobStepDTO diJobStepDTO) {
        DiJobStep step = DiJobStepConvert.INSTANCE.toDo(diJobStepDTO);
        return this.diJobStepMapper.update(step, new LambdaUpdateWrapper<DiJobStep>()
                .eq(DiJobStep::getJobId, step.getJobId())
                .eq(DiJobStep::getStepCode, step.getStepCode())
        );
    }

    @Override
    public int upsert(DiJobStepDTO diJobStep) {
        DiJobStep step = this.diJobStepMapper.selectOne(
                new LambdaQueryWrapper<DiJobStep>()
                        .eq(DiJobStep::getJobId, diJobStep.getJobId())
                        .eq(DiJobStep::getStepCode, diJobStep.getStepCode())
        );
        DiJobStep jobStep = DiJobStepConvert.INSTANCE.toDo(diJobStep);
        if (step == null) {
            return this.diJobStepMapper.insert(jobStep);
        } else {
            return this.diJobStepMapper.update(jobStep,
                    new LambdaUpdateWrapper<DiJobStep>()
                            .eq(DiJobStep::getJobId, jobStep.getJobId())
                            .eq(DiJobStep::getStepCode, jobStep.getStepCode())
            );
        }
    }

    @Override
    public int deleteByProjectId(Collection<? extends Serializable> projectIds) {
        return this.diJobStepMapper.deleteByProjectId(projectIds);
    }

    @Override
    public int deleteByJobId(Collection<? extends Serializable> jobIds) {
        return this.diJobStepMapper.deleteByJobId(jobIds);
    }

    @Override
    public int deleteSurplusStep(Long jobId, List<String> stepCodeList) {
        this.diJobStepAttrService.deleteSurplusStepAttr(jobId, stepCodeList);
        return this.diJobStepMapper.delete(
                new LambdaQueryWrapper<DiJobStep>()
                        .eq(DiJobStep::getJobId, jobId)
                        .notIn(CollectionUtil.isNotEmpty(stepCodeList), DiJobStep::getStepCode, stepCodeList)
        );
    }

    @Override
    public List<DiJobStepDTO> listJobStep(Long jobId) {
        List<DiJobStep> list = this.diJobStepMapper.selectByJobId(jobId);
        return DiJobStepConvert.INSTANCE.toDto(list);
    }

    @Override
    public DiJobStepDTO selectOne(Long jobId, String stepCode) {
        DiJobStep step = this.diJobStepMapper.selectOne(
                new LambdaQueryWrapper<DiJobStep>()
                        .eq(DiJobStep::getJobId, jobId)
                        .eq(DiJobStep::getStepCode, stepCode)
        );
        return DiJobStepConvert.INSTANCE.toDto(step);
    }
}
