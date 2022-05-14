package cn.sliew.breeze.service.di.impl;

import cn.hutool.core.util.StrUtil;
import cn.sliew.breeze.dao.entity.master.di.DiClusterConfig;
import cn.sliew.breeze.dao.mapper.master.di.DiClusterConfigMapper;
import cn.sliew.breeze.service.convert.di.DiClusterConfigConvert;
import cn.sliew.breeze.service.di.DiClusterConfigService;
import cn.sliew.breeze.service.dto.di.DiClusterConfigDTO;
import cn.sliew.breeze.service.param.di.DiClusterConfigParam;
import cn.sliew.breeze.service.vo.DictVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DiClusterConfigServiceImpl implements DiClusterConfigService {

    @Autowired
    private DiClusterConfigMapper diClusterConfigMapper;

    @Override
    public int insert(DiClusterConfigDTO dto) {
        DiClusterConfig config = DiClusterConfigConvert.INSTANCE.toDo(dto);
        return this.diClusterConfigMapper.insert(config);
    }

    @Override
    public int update(DiClusterConfigDTO dto) {
        DiClusterConfig config = DiClusterConfigConvert.INSTANCE.toDo(dto);
        return this.diClusterConfigMapper.updateById(config);
    }

    @Override
    public int deleteById(Long id) {
        return this.diClusterConfigMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        return this.diClusterConfigMapper.deleteBatchIds(map.values());
    }

    @Override
    public Page<DiClusterConfigDTO> listByPage(DiClusterConfigParam param) {
        Page<DiClusterConfigDTO> result = new Page<>();
        Page<DiClusterConfig> list = this.diClusterConfigMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                new LambdaQueryWrapper<DiClusterConfig>()
                        .like(StrUtil.isNotEmpty(param.getClusterName()), DiClusterConfig::getClusterName, param.getClusterName())
                        .eq(StrUtil.isNotEmpty(param.getClusterType()), DiClusterConfig::getClusterType, param.getClusterType())
        );
        List<DiClusterConfigDTO> dtoList = DiClusterConfigConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }

    @Override
    public List<DictVO> listAll() {
        List<DiClusterConfig> list = this.diClusterConfigMapper.selectList(null);
        List<DictVO> voList = new ArrayList<>();
        list.forEach(c -> {
            DictVO vo = new DictVO(String.valueOf(c.getId()), c.getClusterName());
            voList.add(vo);
        });
        return voList;
    }

    @Override
    public DiClusterConfigDTO selectOne(Long id) {
        DiClusterConfig config = this.diClusterConfigMapper.selectById(id);
        return DiClusterConfigConvert.INSTANCE.toDto(config);
    }
}
