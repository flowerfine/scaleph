package cn.sliew.scaleph.core.di.service.impl;

import cn.sliew.scaleph.dao.entity.master.di.DiJobAttr;
import cn.sliew.scaleph.dao.mapper.master.di.DiJobAttrMapper;
import cn.sliew.scaleph.core.di.service.convert.DiJobAttrConvert;
import cn.sliew.scaleph.core.di.service.DiJobAttrService;
import cn.sliew.scaleph.core.di.service.dto.DiJobAttrDTO;
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
public class DiJobAttrServiceImpl implements DiJobAttrService {

    @Autowired
    private DiJobAttrMapper diJobAttrMapper;

    @Override
    public int deleteByProjectId(Collection<? extends Serializable> projectIds) {
        return this.diJobAttrMapper.deleteByProjectId(projectIds);
    }

    @Override
    public int deleteByJobId(Collection<? extends Serializable> jobIds) {
        return this.diJobAttrMapper.deleteByJobId(jobIds);
    }

    @Override
    public List<DiJobAttrDTO> listJobAttr(Long jobId) {
        List<DiJobAttr> list = this.diJobAttrMapper.selectList(new LambdaQueryWrapper<DiJobAttr>()
                .eq(DiJobAttr::getJobId, jobId)
        );
        return DiJobAttrConvert.INSTANCE.toDto(list);
    }

    @Override
    public int upsert(DiJobAttrDTO jobAttrDTO) {
        DiJobAttr jobAttr = DiJobAttrConvert.INSTANCE.toDo(jobAttrDTO);
        DiJobAttr attr = this.diJobAttrMapper.selectOne(
                new LambdaQueryWrapper<DiJobAttr>()
                        .eq(DiJobAttr::getJobId, jobAttr.getJobId())
                        .eq(DiJobAttr::getJobAttrType, jobAttr.getJobAttrType())
                        .eq(DiJobAttr::getJobAttrKey, jobAttr.getJobAttrKey())
        );
        if (attr == null) {
            return this.diJobAttrMapper.insert(jobAttr);
        } else {
            return this.diJobAttrMapper.update(jobAttr,
                    new LambdaUpdateWrapper<DiJobAttr>()
                            .eq(DiJobAttr::getJobId, jobAttr.getJobId())
                            .eq(DiJobAttr::getJobAttrType, jobAttr.getJobAttrType())
                            .eq(DiJobAttr::getJobAttrKey, jobAttr.getJobAttrKey())
            );
        }
    }


}
