package cn.sliew.breeze.service.di.impl;

import cn.hutool.core.util.StrUtil;
import cn.sliew.breeze.dao.entity.master.di.DiProject;
import cn.sliew.breeze.dao.mapper.master.di.DiProjectMapper;
import cn.sliew.breeze.service.convert.di.DiProjectConvert;
import cn.sliew.breeze.service.di.DiDirectoryService;
import cn.sliew.breeze.service.di.DiJobService;
import cn.sliew.breeze.service.di.DiProjectService;
import cn.sliew.breeze.service.di.DiResourceFileService;
import cn.sliew.breeze.service.dto.di.DiDirectoryDTO;
import cn.sliew.breeze.service.dto.di.DiProjectDTO;
import cn.sliew.breeze.service.param.di.DiProjectParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据集成-项目信息 服务实现类
 * </p>
 *
 * @author liyu
 */
@Service
public class DiProjectServiceImpl implements DiProjectService {

    @Autowired
    private DiProjectMapper diProjectMapper;

    @Autowired
    private DiDirectoryService diDirectoryService;

    @Autowired
    private DiJobService diJobService;

    @Autowired
    private DiResourceFileService diResourceFileService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(DiProjectDTO dto) {
        DiProject project = DiProjectConvert.INSTANCE.toDo(dto);
        int result = this.diProjectMapper.insert(project);
        DiDirectoryDTO dir = new DiDirectoryDTO();
        dir.setProjectId(project.getId());
        dir.setDirectoryName(dto.getProjectCode());
        dir.setPid(0L);
        this.diDirectoryService.insert(dir);
        return result;
    }

    @Override
    public int update(DiProjectDTO dto) {
        DiProject project = DiProjectConvert.INSTANCE.toDo(dto);
        return this.diProjectMapper.updateById(project);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        List<Long> list = new ArrayList<Long>() {{
            add(id);
        }};
        this.diResourceFileService.deleteByProjectId(list);
        this.diDirectoryService.deleteByProjectIds(list);
        this.diJobService.deleteByProjectId(list);
        return this.diProjectMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        this.diResourceFileService.deleteByProjectId(map.values());
        this.diDirectoryService.deleteByProjectIds(map.values());
        this.diJobService.deleteByProjectId(map.values());
        return this.diProjectMapper.deleteBatchIds(map.values());
    }

    @Override
    public Page<DiProjectDTO> listByPage(DiProjectParam param) {
        Page<DiProjectDTO> result = new Page<>();
        Page<DiProject> list = this.diProjectMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                new LambdaQueryWrapper<DiProject>()
                        .like(StrUtil.isNotEmpty(param.getProjectCode()), DiProject::getProjectCode, param.getProjectCode())
                        .like(StrUtil.isNotEmpty(param.getProjectName()), DiProject::getProjectName, param.getProjectName())
        );
        List<DiProjectDTO> dtoList = DiProjectConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }

    @Override
    public List<DiProjectDTO> listAll() {
        List<DiProject> list = this.diProjectMapper.selectList(null);
        return DiProjectConvert.INSTANCE.toDto(list);
    }
}
