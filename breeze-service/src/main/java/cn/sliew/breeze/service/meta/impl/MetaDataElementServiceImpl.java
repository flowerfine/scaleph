package cn.sliew.breeze.service.meta.impl;

import cn.hutool.core.util.StrUtil;
import cn.sliew.breeze.dao.entity.MetaDataElement;
import cn.sliew.breeze.dao.mapper.MetaDataElementMapper;
import cn.sliew.breeze.service.convert.meta.MetaDataElementConvert;
import cn.sliew.breeze.service.dto.meta.MetaDataElementDTO;
import cn.sliew.breeze.service.meta.MetaDataElementService;
import cn.sliew.breeze.service.param.meta.MetaDataElementParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
public class MetaDataElementServiceImpl implements MetaDataElementService {

    @Autowired
    private MetaDataElementMapper metaDataElementMapper;

    @Override
    public int insert(MetaDataElementDTO metaDataElementDTO) {
        MetaDataElement dataElement = MetaDataElementConvert.INSTANCE.toDo(metaDataElementDTO);
        return this.metaDataElementMapper.insert(dataElement);
    }

    @Override
    public int update(MetaDataElementDTO metaDataElementDTO) {
        MetaDataElement dataElement = MetaDataElementConvert.INSTANCE.toDo(metaDataElementDTO);
        return this.metaDataElementMapper.updateById(dataElement);
    }

    @Override
    public int deleteById(Long id) {
        return this.metaDataElementMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        return this.metaDataElementMapper.deleteBatchIds(map.values());
    }

    @Override
    public Page<MetaDataElementDTO> listByPage(MetaDataElementParam param) {
        Page<MetaDataElementDTO> result = new Page<>();
        Page<MetaDataElement> list = this.metaDataElementMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                new LambdaQueryWrapper<MetaDataElement>()
                        .like(StrUtil.isNotEmpty(param.getElementCode()), MetaDataElement::getElementCode, param.getElementCode())
                        .like(StrUtil.isNotEmpty(param.getElementName()), MetaDataElement::getElementName, param.getElementName())
        );
        List<MetaDataElementDTO> dtoList = MetaDataElementConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }
}
