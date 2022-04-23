package cn.sliew.breeze.service.admin.impl;

import cn.hutool.core.util.StrUtil;
import cn.sliew.breeze.dao.entity.DictType;
import cn.sliew.breeze.dao.mapper.DictTypeMapper;
import cn.sliew.breeze.service.admin.DictService;
import cn.sliew.breeze.service.admin.DictTypeService;
import cn.sliew.breeze.service.cache.DictTypeCache;
import cn.sliew.breeze.service.convert.admin.DictTypeConvert;
import cn.sliew.breeze.service.dto.admin.DictTypeDTO;
import cn.sliew.breeze.service.param.admin.DictTypeParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据字典类型 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-07-24
 */
@Service
public class DictTypeServiceImpl implements DictTypeService {

    @Autowired
    private DictTypeMapper dictTypeMapper;
    @Autowired
    private DictService dictService;

    @Override
    public int insert(DictTypeDTO dictTypeDTO) {
        DictType dictType = DictTypeConvert.INSTANCE.toDo(dictTypeDTO);
        int result = this.dictTypeMapper.insert(dictType);
        DictTypeCache.updateCache(dictTypeDTO);
        return result;
    }

    @Override
    public int update(DictTypeDTO dictTypeDTO) {
        DictType dictType = DictTypeConvert.INSTANCE.toDo(dictTypeDTO);
        int result = this.dictTypeMapper.updateById(dictType);
        DictTypeCache.updateCache(dictTypeDTO);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        DictType dictType = this.dictTypeMapper.selectById(id);
        int result = this.dictTypeMapper.deleteById(id);
        this.dictService.deleteByType(dictType.getDictTypeCode());
        DictTypeCache.evictCache(dictType.getDictTypeCode());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        List<DictType> list = this.dictTypeMapper.selectBatchIds(map.values());
        int result = this.dictTypeMapper.deleteBatchIds(map.values());
        for (DictType dictType : list) {
            this.dictService.deleteByType(dictType.getDictTypeCode());
            DictTypeCache.evictCache(dictType.getDictTypeCode());
        }
        return result;
    }

    @Override
    public DictTypeDTO selectOne(Long id) {
        DictType dictType = this.dictTypeMapper.selectById(id);
        DictTypeDTO dto = DictTypeConvert.INSTANCE.toDto(dictType);
        DictTypeCache.updateCache(dto);
        return dto;
    }

    @Override
    public DictTypeDTO selectOne(String dictTypeCode) {
        DictType dictType = this.dictTypeMapper.selectByDictTypeCode(dictTypeCode);
        DictTypeDTO dto = DictTypeConvert.INSTANCE.toDto(dictType);
        DictTypeCache.updateCache(dto);
        return dto;
    }

    @Override
    public Page<DictTypeDTO> listByPage(DictTypeParam param) {
        Page<DictTypeDTO> result = new Page<>();
        Page<DictType> list = this.dictTypeMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                new QueryWrapper<DictType>()
                        .lambda()
                        .like(StrUtil.isNotEmpty(param.getDictTypeCode()), DictType::getDictTypeCode, param.getDictTypeCode())
                        .like(StrUtil.isNotEmpty(param.getDictTypeName()), DictType::getDictTypeName, param.getDictTypeName())
        );
        List<DictTypeDTO> dtoList = DictTypeConvert.INSTANCE.toDto(list.getRecords());
        DictTypeCache.updateCache(dtoList);
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }

    @Override
    public List<DictTypeDTO> selectAll() {
        List<DictType> dictTypeList = this.dictTypeMapper.selectList(null);
        return DictTypeConvert.INSTANCE.toDto(dictTypeList);
    }
}
