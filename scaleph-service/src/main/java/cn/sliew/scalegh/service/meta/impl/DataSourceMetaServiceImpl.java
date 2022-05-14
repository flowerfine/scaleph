package cn.sliew.scalegh.service.meta.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.sliew.scalegh.dao.entity.master.meta.DataSourceMeta;
import cn.sliew.scalegh.dao.mapper.master.meta.DataSourceMetaMapper;
import cn.sliew.scalegh.service.convert.meta.DataSourceMetaConvert;
import cn.sliew.scalegh.service.dto.meta.DataSourceMetaDTO;
import cn.sliew.scalegh.service.meta.DataSourceMetaService;
import cn.sliew.scalegh.service.param.meta.DataSourceMetaParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author gleiyu
 */
@Service
public class DataSourceMetaServiceImpl implements DataSourceMetaService {

    @Autowired
    private DataSourceMetaMapper dataSourceMetaMapper;

    @Override
    public int insert(DataSourceMetaDTO metaDTO) {
        if (StrUtil.isNotEmpty(metaDTO.getPassword())) {
            String encodePasswd = Base64.encode(metaDTO.getPassword());
            metaDTO.setPassword(encodePasswd);
        }
        DataSourceMeta meta = DataSourceMetaConvert.INSTANCE.toDo(metaDTO);
        return this.dataSourceMetaMapper.insert(meta);
    }

    @Override
    public int update(DataSourceMetaDTO metaDTO) {
        if (StrUtil.isNotEmpty(metaDTO.getPassword())) {
            String encodePasswd = Base64.encode(metaDTO.getPassword());
            metaDTO.setPassword(encodePasswd);
        }
        DataSourceMeta meta = DataSourceMetaConvert.INSTANCE.toDo(metaDTO);
        return this.dataSourceMetaMapper.updateById(meta);
    }

    @Override
    public int deleteById(Long id) {
        return this.dataSourceMetaMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        return this.dataSourceMetaMapper.deleteBatchIds(map.values());
    }

    @Override
    public DataSourceMetaDTO selectOne(Serializable id) {
        DataSourceMeta meta = this.dataSourceMetaMapper.selectById(id);
        return DataSourceMetaConvert.INSTANCE.toDto(meta);
    }

    @Override
    public Page<DataSourceMetaDTO> listByPage(DataSourceMetaParam param) {
        Page<DataSourceMetaDTO> result = new Page<>();
        Page<DataSourceMeta> list = this.dataSourceMetaMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(DataSourceMeta.class)
                        .eq(StrUtil.isNotEmpty(param.getDatabaseName()), DataSourceMeta::getDatabaseName, param.getDatabaseName())
                        .eq(StrUtil.isNotEmpty(param.getDataSourceType()), DataSourceMeta::getDatasourceType, param.getDataSourceType())
                        .eq(StrUtil.isNotEmpty(param.getHostName()), DataSourceMeta::getHostName, param.getHostName())
                        .like(StrUtil.isNotEmpty(param.getDataSourceName()), DataSourceMeta::getDatasourceName, param.getDataSourceName())
        );
        List<DataSourceMetaDTO> dtoList = DataSourceMetaConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }

    @Override
    public List<DataSourceMetaDTO> listByType(String type) {
        List<DataSourceMeta> list = this.dataSourceMetaMapper.selectList(
                new LambdaQueryWrapper<DataSourceMeta>()
                        .eq(DataSourceMeta::getDatasourceType, type)
        );
        return DataSourceMetaConvert.INSTANCE.toDto(list);
    }
}
