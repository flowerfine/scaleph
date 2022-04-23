package cn.sliew.breeze.service.di.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.sliew.breeze.dao.entity.DiJobLink;
import cn.sliew.breeze.dao.mapper.DiJobLinkMapper;
import cn.sliew.breeze.service.convert.di.DiJobLinkConvert;
import cn.sliew.breeze.service.di.DiJobLinkService;
import cn.sliew.breeze.service.dto.di.DiJobLinkDTO;
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
public class DiJobLinkServiceImpl implements DiJobLinkService {

    @Autowired
    private DiJobLinkMapper diJobLinkMapper;

    @Override
    public int insert(DiJobLinkDTO diJobLink) {
        DiJobLink link = DiJobLinkConvert.INSTANCE.toDo(diJobLink);
        return this.diJobLinkMapper.insert(link);
    }

    @Override
    public int deleteByProjectId(Collection<? extends Serializable> projectIds) {
        return this.diJobLinkMapper.deleteByProjectId(projectIds);
    }

    @Override
    public int deleteByJobId(Collection<? extends Serializable> jobIds) {
        return this.diJobLinkMapper.deleteByJobId(jobIds);
    }

    @Override
    public List<DiJobLinkDTO> listJobLink(Long jobId) {
        List<DiJobLink> list = this.diJobLinkMapper.selectList(
                new LambdaQueryWrapper<DiJobLink>()
                        .eq(DiJobLink::getJobId, jobId)
        );
        return DiJobLinkConvert.INSTANCE.toDto(list);
    }

    @Override
    public int deleteSurplusLink(Long jobId, List<String> linkCodeList) {
        return this.diJobLinkMapper.delete(
                new LambdaQueryWrapper<DiJobLink>()
                        .eq(DiJobLink::getJobId, jobId)
                        .notIn(CollectionUtil.isNotEmpty(linkCodeList), DiJobLink::getLinkCode, linkCodeList)
        );
    }

    @Override
    public int upsert(DiJobLinkDTO diJobLink) {
        DiJobLink link = this.diJobLinkMapper.selectOne(
                new LambdaQueryWrapper<DiJobLink>()
                        .eq(DiJobLink::getJobId, diJobLink.getJobId())
                        .eq(DiJobLink::getLinkCode, diJobLink.getLinkCode())
        );
        DiJobLink jobLink = DiJobLinkConvert.INSTANCE.toDo(diJobLink);
        if (link == null) {
            return this.diJobLinkMapper.insert(jobLink);
        } else {
            return this.diJobLinkMapper.update(jobLink,
                    new LambdaUpdateWrapper<DiJobLink>()
                            .eq(DiJobLink::getJobId, jobLink.getJobId())
                            .eq(DiJobLink::getLinkCode, jobLink.getLinkCode())
            );
        }
    }
}
