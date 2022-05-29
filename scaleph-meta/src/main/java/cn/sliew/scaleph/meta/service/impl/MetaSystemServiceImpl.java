package cn.sliew.scaleph.meta.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scaleph.dao.entity.master.meta.MetaDataSet;
import cn.sliew.scaleph.dao.entity.master.meta.MetaSystem;
import cn.sliew.scaleph.dao.mapper.master.meta.MetaDataSetMapper;
import cn.sliew.scaleph.dao.mapper.master.meta.MetaSystemMapper;
import cn.sliew.scaleph.meta.service.MetaSystemService;
import cn.sliew.scaleph.meta.service.convert.MetaSystemConvert;
import cn.sliew.scaleph.meta.service.dto.MetaSystemDTO;
import cn.sliew.scaleph.meta.service.param.MetaSystemParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 业务系统元数据服务实现类
 *
 * @author gleiyu
 */
@Service
public class MetaSystemServiceImpl implements MetaSystemService {
    @Autowired
    private MetaSystemMapper metaSystemMapper;
    @Autowired
    private MetaDataSetMapper metaDataSetMapper;

    @Override
    public int insert(MetaSystemDTO metaSystem) {
        MetaSystem meta = MetaSystemConvert.INSTANCE.toDo(metaSystem);
        return this.metaSystemMapper.insert(meta);
    }

    @Override
    public int update(MetaSystemDTO metaSystem) {
        MetaSystem meta = MetaSystemConvert.INSTANCE.toDo(metaSystem);
        return this.metaSystemMapper.updateById(meta);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        this.metaDataSetMapper.delete(
            new LambdaQueryWrapper<MetaDataSet>()
                .eq(MetaDataSet::getSystemId, id)
        );
        return this.metaSystemMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        this.metaDataSetMapper.delete(
            new LambdaQueryWrapper<MetaDataSet>()
                .in(MetaDataSet::getSystemId, map.values())
        );
        return this.metaSystemMapper.deleteBatchIds(map.values());
    }

    @Override
    public Page<MetaSystemDTO> listByPage(MetaSystemParam param) {
        Page<MetaSystemDTO> result = new Page<>();
        Page<MetaSystem> list = this.metaSystemMapper.selectPage(
            new Page<>(param.getCurrent(), param.getPageSize()),
            new LambdaQueryWrapper<MetaSystem>()
                .like(StrUtil.isNotEmpty(param.getSystemCode()), MetaSystem::getSystemCode,
                    param.getSystemCode())
                .like(StrUtil.isNotEmpty(param.getSystemName()), MetaSystem::getSystemName,
                    param.getSystemName())
        );
        List<MetaSystemDTO> dtoList = MetaSystemConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }


}
