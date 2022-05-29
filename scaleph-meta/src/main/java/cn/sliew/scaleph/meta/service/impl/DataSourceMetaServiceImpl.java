package cn.sliew.scaleph.meta.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cn.sliew.scaleph.common.codec.CodecUtil;
import cn.sliew.scaleph.dao.entity.master.meta.DataSourceMeta;
import cn.sliew.scaleph.dao.mapper.master.meta.DataSourceMetaMapper;
import cn.sliew.scaleph.meta.service.DataSourceMetaService;
import cn.sliew.scaleph.meta.service.convert.DataSourceMetaConvert;
import cn.sliew.scaleph.meta.service.dto.DataSourceMetaDTO;
import cn.sliew.scaleph.meta.service.param.DataSourceMetaParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author gleiyu
 */
@Service
public class DataSourceMetaServiceImpl implements DataSourceMetaService {

    @Autowired
    private DataSourceMetaMapper dataSourceMetaMapper;

    @Override
    public int insert(DataSourceMetaDTO metaDTO) {
        if (StringUtils.hasText(metaDTO.getPassword())) {
            String encodePasswd = CodecUtil.encodeToBase64(metaDTO.getPassword());
            metaDTO.setPassword(encodePasswd);
        }
        DataSourceMeta meta = DataSourceMetaConvert.INSTANCE.toDo(metaDTO);
        return this.dataSourceMetaMapper.insert(meta);
    }

    @Override
    public int update(DataSourceMetaDTO metaDTO) {
        if (StringUtils.hasText(metaDTO.getPassword())) {
            String encodePasswd = CodecUtil.encodeToBase64(metaDTO.getPassword());
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
        Page<DataSourceMeta> list = this.dataSourceMetaMapper.selectPage(
            new Page<>(param.getCurrent(), param.getPageSize()),
            Wrappers.lambdaQuery(DataSourceMeta.class)
                .eq(StringUtils.hasText(param.getDatabaseName()), DataSourceMeta::getDatabaseName,
                    param.getDatabaseName())
                .eq(StringUtils.hasText(param.getDataSourceType()),
                    DataSourceMeta::getDatasourceType, param.getDataSourceType())
                .eq(StringUtils.hasText(param.getHostName()), DataSourceMeta::getHostName,
                    param.getHostName())
                .like(StringUtils.hasText(param.getDataSourceName()),
                    DataSourceMeta::getDatasourceName, param.getDataSourceName())
        );
        Page<DataSourceMetaDTO> result =
            new Page<>(list.getCurrent(), list.getSize(), list.getTotal());
        List<DataSourceMetaDTO> dtoList = DataSourceMetaConvert.INSTANCE.toDto(list.getRecords());
        result.setRecords(dtoList);
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
