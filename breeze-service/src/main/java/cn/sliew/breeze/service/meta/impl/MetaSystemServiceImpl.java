package cn.sliew.breeze.service.meta.impl;

import cn.hutool.core.util.StrUtil;
import cn.sliew.breeze.dao.entity.MetaSystem;
import cn.sliew.breeze.dao.mapper.MetaSystemMapper;
import cn.sliew.breeze.service.convert.meta.MetaSystemConvert;
import cn.sliew.breeze.service.dto.meta.MetaSystemDTO;
import cn.sliew.breeze.service.meta.MetaSystemService;
import cn.sliew.breeze.service.param.meta.MetaSystemParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 业务系统元数据服务实现类
 *
 * @author gleiyu
 */
@Service
public class MetaSystemServiceImpl implements MetaSystemService {
    @Autowired
    private MetaSystemMapper metaSystemMapper;


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
    public int deleteById(Long id) {
        return this.metaSystemMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(Map<Integer, ? extends Serializable> map) {
        return this.metaSystemMapper.deleteBatchIds(map.values());
    }

    @Override
    public Page<MetaSystemDTO> listByPage(MetaSystemParam param) {
        Page<MetaSystemDTO> result = new Page<>();
        Page<MetaSystem> list = this.metaSystemMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                new LambdaQueryWrapper<MetaSystem>()
                        .like(StrUtil.isNotEmpty(param.getSystemCode()), MetaSystem::getSystemCode, param.getSystemCode())
                        .like(StrUtil.isNotEmpty(param.getSystemName()), MetaSystem::getSystemName, param.getSystemName())
        );
        List<MetaSystemDTO> dtoList = MetaSystemConvert.INSTANCE.toDto(list.getRecords());
        result.setCurrent(list.getCurrent());
        result.setSize(list.getSize());
        result.setRecords(dtoList);
        result.setTotal(list.getTotal());
        return result;
    }


}
