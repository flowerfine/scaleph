package cn.sliew.scalegh.service.admin.impl;

import cn.hutool.core.util.StrUtil;
import cn.sliew.breeze.dao.entity.master.system.Dict;
import cn.sliew.breeze.dao.mapper.master.system.DictMapper;
import cn.sliew.scalegh.service.admin.DictService;
import cn.sliew.scalegh.service.cache.DictCache;
import cn.sliew.scalegh.service.config.CaffeineCacheConfig;
import cn.sliew.scalegh.service.convert.admin.DictConvert;
import cn.sliew.scalegh.service.dto.admin.DictDTO;
import cn.sliew.scalegh.service.param.admin.DictParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据字典表 服务实现类
 * </p>
 *
 * @author liyu
 * @since 2021-07-24
 */
@Service
@CacheConfig(cacheNames = CaffeineCacheConfig.UnBoundedCaches.CACHE_DICT, cacheManager = "unBoundedCacheManager")
public class DictServiceImpl implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Override
    public int insert(DictDTO dictDTO) {
        Dict dict = DictConvert.INSTANCE.toDo(dictDTO);
        int result = this.dictMapper.insert(dict);
        DictCache.updateCache(dictDTO);
        return result;
    }

    @Override
    public int update(DictDTO dictDTO) {
        Dict dict = DictConvert.INSTANCE.toDo(dictDTO);
        int result = this.dictMapper.updateById(dict);
        DictCache.updateCache(dictDTO);
        return result;
    }

    @Override
    public int deleteById(Long id) {
        Dict dict = this.dictMapper.selectById(id);
        int result = this.dictMapper.deleteById(id);
        DictCache.evictCache(dict.getKey());
        return result;
    }

    @Override
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        List<Dict> list = this.dictMapper.selectBatchIds(map.values());
        int result = this.dictMapper.deleteBatchIds(map.values());
        for (Dict dict : list) {
            DictCache.evictCache(dict.getKey());
        }
        return result;
    }

    @Override
    public int deleteByType(String dictCodeType) {
        int result = this.dictMapper.delete(new LambdaQueryWrapper<Dict>()
                .eq(Dict::getDictTypeCode, dictCodeType));
        DictCache.evictCacheByType(dictCodeType);
        return result;
    }

    @Override
    public DictDTO selectOne(Long id) {
        Dict dict = this.dictMapper.selectById(id);
        DictDTO dto = DictConvert.INSTANCE.toDto(dict);
        DictCache.updateCache(dto);
        return dto;
    }

    @Override
    public List<DictDTO> selectByType(String dictTypeCode) {
        List<Dict> list = this.dictMapper.selectList(new QueryWrapper<Dict>()
                .lambda()
                .eq(Dict::getDictTypeCode, dictTypeCode));
        List<DictDTO> dtoList = DictConvert.INSTANCE.toDto(list);
        DictCache.updateCache(dtoList);
        return dtoList;
    }

    @Override
    public List<DictDTO> selectAll() {
        List<Dict> list = this.dictMapper.selectList(null);
        return DictConvert.INSTANCE.toDto(list);
    }

    @Override
    public Page<DictDTO> listByPage(DictParam param) {
        Page<DictDTO> result = new Page<>();
        Page<Dict> list = this.dictMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                new LambdaQueryWrapper<Dict>()
                        .like(StrUtil.isNotEmpty(param.getDictTypeCode()), Dict::getDictTypeCode, param.getDictTypeCode())
                        .like(StrUtil.isNotEmpty(param.getDictCode()), Dict::getDictCode, param.getDictCode())
                        .like(StrUtil.isNotEmpty(param.getDictValue()), Dict::getDictValue, param.getDictValue())
                        .eq(StrUtil.isNotEmpty(param.getIsValid()), Dict::getIsValid, param.getIsValid())
        );
        List<DictDTO> dtoList = DictConvert.INSTANCE.toDto(list.getRecords());
        DictCache.updateCache(dtoList);
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }
}
