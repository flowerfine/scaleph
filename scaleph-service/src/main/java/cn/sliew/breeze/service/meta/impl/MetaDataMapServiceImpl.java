package cn.sliew.breeze.service.meta.impl;

import cn.sliew.breeze.dao.entity.MetaDataMap;
import cn.sliew.breeze.dao.mapper.MetaDataMapMapper;
import cn.sliew.breeze.service.convert.meta.MetaDataMapConvert;
import cn.sliew.breeze.service.dto.meta.MetaDataMapDTO;
import cn.sliew.breeze.service.meta.MetaDataMapService;
import cn.sliew.breeze.service.param.meta.MetaDataMapParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
public class MetaDataMapServiceImpl implements MetaDataMapService {

    @Autowired
    private MetaDataMapMapper metaDataMapMapper;

    @Override
    public int insert(MetaDataMapDTO metaDataMapDTO) {
        MetaDataMap map = MetaDataMapConvert.INSTANCE.toDo(metaDataMapDTO);
        return this.metaDataMapMapper.insert(map);
    }

    @Override
    public int update(MetaDataMapDTO metaDataMapDTO) {
        MetaDataMap map = MetaDataMapConvert.INSTANCE.toDo(metaDataMapDTO);
        return this.metaDataMapMapper.updateById(map);
    }

    @Override
    public int deleteById(Long id) {
        return this.metaDataMapMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        return this.metaDataMapMapper.deleteBatchIds(map.values());
    }

    @Override
    public Page<MetaDataMapDTO> listByPage(MetaDataMapParam param) {
        Page<MetaDataMapDTO> result = new Page<>();
        Page<MetaDataMap> list = this.metaDataMapMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                param.getSrcDataSetTypeCode(),
                param.getTgtDataSetTypeCode(),
                param.getSrcDataSetCode(),
                param.getTgtDataSetCode(),
                param.isAuto()
        );
        List<MetaDataMapDTO> dtoList = MetaDataMapConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }
}
