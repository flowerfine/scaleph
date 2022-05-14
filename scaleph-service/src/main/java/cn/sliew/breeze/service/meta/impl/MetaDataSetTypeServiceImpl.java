package cn.sliew.breeze.service.meta.impl;

import cn.hutool.core.util.StrUtil;
import cn.sliew.breeze.dao.entity.master.meta.MetaDataSet;
import cn.sliew.breeze.dao.entity.master.meta.MetaDataSetType;
import cn.sliew.breeze.dao.mapper.master.meta.MetaDataSetMapper;
import cn.sliew.breeze.dao.mapper.master.meta.MetaDataSetTypeMapper;
import cn.sliew.breeze.service.convert.meta.MetaDataSetTypeConvert;
import cn.sliew.breeze.service.dto.meta.MetaDataSetTypeDTO;
import cn.sliew.breeze.service.meta.MetaDataSetTypeService;
import cn.sliew.breeze.service.param.meta.MetaDataSetTypeParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
public class MetaDataSetTypeServiceImpl implements MetaDataSetTypeService {

    @Autowired
    private MetaDataSetTypeMapper metaDataSetTypeMapper;
    @Autowired
    private MetaDataSetMapper metaDataSetMapper;

    @Override
    public int insert(MetaDataSetTypeDTO metaDataSetTypeDTO) {
        MetaDataSetType type = MetaDataSetTypeConvert.INSTANCE.toDo(metaDataSetTypeDTO);
        return this.metaDataSetTypeMapper.insert(type);
    }

    @Override
    public int update(MetaDataSetTypeDTO metaDataSetTypeDTO) {
        MetaDataSetType type = MetaDataSetTypeConvert.INSTANCE.toDo(metaDataSetTypeDTO);
        return this.metaDataSetTypeMapper.updateById(type);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        this.metaDataSetMapper.delete(
                new LambdaQueryWrapper<MetaDataSet>()
                        .eq(MetaDataSet::getDataSetTypeId, id)
        );
        return this.metaDataSetTypeMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        this.metaDataSetMapper.delete(
                new LambdaQueryWrapper<MetaDataSet>()
                        .in(MetaDataSet::getDataSetTypeId, map.values())
        );
        return this.metaDataSetTypeMapper.deleteBatchIds(map.values());
    }

    @Override
    public Page<MetaDataSetTypeDTO> listByPage(MetaDataSetTypeParam param) {
        Page<MetaDataSetTypeDTO> result = new Page<>();
        Page<MetaDataSetType> list = this.metaDataSetTypeMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                new LambdaQueryWrapper<MetaDataSetType>()
                        .like(StrUtil.isNotEmpty(param.getDataSetTypeCode()), MetaDataSetType::getDataSetTypeCode, param.getDataSetTypeCode())
                        .like(StrUtil.isNotEmpty(param.getDataSetTypeName()), MetaDataSetType::getDataSetTypeName, param.getDataSetTypeName())
        );
        List<MetaDataSetTypeDTO> dtoList = MetaDataSetTypeConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }
}
