package cn.sliew.breeze.service.di.impl;

import cn.sliew.breeze.dao.entity.DiResourceFile;
import cn.sliew.breeze.dao.mapper.DiResourceFileMapper;
import cn.sliew.breeze.service.convert.di.DiResourceFileConvert;
import cn.sliew.breeze.service.di.DiResourceFileService;
import cn.sliew.breeze.service.dto.di.DiResourceFileDTO;
import cn.sliew.breeze.service.param.di.DiResourceFileParam;
import cn.sliew.breeze.service.vo.DictVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class DiResourceFileServiceImpl implements DiResourceFileService {

    @Autowired
    private DiResourceFileMapper diResourceFileMapper;

    @Override
    public int insert(DiResourceFileDTO dto) {
        DiResourceFile file = DiResourceFileConvert.INSTANCE.toDo(dto);
        return this.diResourceFileMapper.insert(file);
    }

    @Override
    public int update(DiResourceFileDTO dto) {
        DiResourceFile file = DiResourceFileConvert.INSTANCE.toDo(dto);
        return this.diResourceFileMapper.updateById(file);
    }

    @Override
    public int deleteByProjectId(Collection<? extends Serializable> projectIds) {
        return this.diResourceFileMapper.delete(
                new LambdaQueryWrapper<DiResourceFile>()
                        .in(DiResourceFile::getProjectId, projectIds)
        );
    }

    @Override
    public int deleteById(Long id) {
        return this.diResourceFileMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        return this.diResourceFileMapper.deleteBatchIds(map.values());
    }

    @Override
    public Page<DiResourceFileDTO> listByPage(DiResourceFileParam param) {
        Page<DiResourceFileDTO> result = new Page<>();
        Page<DiResourceFile> list = this.diResourceFileMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()), param.getProjectId(), param.getFileName());
        List<DiResourceFileDTO> dtoList = DiResourceFileConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }

    @Override
    public List<DiResourceFileDTO> listByIds(Collection<? extends Serializable> ids) {
        List<DiResourceFile> resources = this.diResourceFileMapper.selectList(
                new LambdaQueryWrapper<DiResourceFile>()
                        .in(DiResourceFile::getId, ids)
        );
        return DiResourceFileConvert.INSTANCE.toDto(resources);
    }

    @Override
    public List<DictVO> listByProjectId(Long projectId) {
        List<DiResourceFile> resources = this.diResourceFileMapper.selectList(
                new LambdaQueryWrapper<DiResourceFile>()
                        .eq(DiResourceFile::getProjectId, projectId)
        );
        List<DictVO> list = new ArrayList<>();
        resources.forEach(f -> {
            DictVO vo = new DictVO(String.valueOf(f.getId()), f.getFileName());
            list.add(vo);
        });
        return list;
    }
}
