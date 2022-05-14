package cn.sliew.breeze.service.convert.meta;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import cn.sliew.breeze.common.constant.DictConstants;
import cn.sliew.breeze.common.enums.DataSourcePropTypeEnum;
import cn.sliew.breeze.dao.entity.master.meta.DataSourceMeta;
import cn.sliew.breeze.service.convert.BaseConvert;
import cn.sliew.breeze.service.convert.DictVoConvert;
import cn.sliew.breeze.service.dto.meta.DataSourceMetaDTO;
import cn.sliew.breeze.service.vo.DictVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gleiyu
 */
@Mapper(uses = {DictVoConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataSourceMetaConvert extends BaseConvert<DataSourceMeta, DataSourceMetaDTO> {
    DataSourceMetaConvert INSTANCE = Mappers.getMapper(DataSourceMetaConvert.class);

    @Override
    default DataSourceMeta toDo(DataSourceMetaDTO dto) {
        if (dto == null) {
            return null;
        }
        DataSourceMeta meta = new DataSourceMeta();
        meta.setId(dto.getId());
        meta.setDatasourceName(dto.getDataSourceName());
        meta.setDatasourceType(DictVoConvert.INSTANCE.toDo(dto.getDataSourceType()));
        meta.setConnectionType(DictVoConvert.INSTANCE.toDo(dto.getConnectionType()));
        meta.setHostName(dto.getHostName());
        meta.setDatabaseName(dto.getDatabaseName());
        meta.setPort(dto.getPort());
        meta.setUserName(dto.getUserName());
        meta.setPassword(dto.getPassword());
        meta.setRemark(dto.getRemark());
        meta.setProps(JSONUtil.toJsonStr(new HashMap<String, String>() {{
            put(DataSourcePropTypeEnum.GENERAL.getCode(), dto.getGeneralProps());
            put(DataSourcePropTypeEnum.JDBC.getCode(), dto.getJdbcProps());
            put(DataSourcePropTypeEnum.POOL.getCode(), dto.getPoolProps());
        }}));
        meta.setCreateTime(dto.getCreateTime());
        meta.setCreator(dto.getCreator());
        meta.setUpdateTime(dto.getUpdateTime());
        meta.setEditor(dto.getEditor());
        return meta;
    }


    @Override
    default DataSourceMetaDTO toDto(DataSourceMeta entity) {
        if (entity == null) {
            return null;
        }
        DataSourceMetaDTO dto = new DataSourceMetaDTO();
        dto.setId(entity.getId());
        dto.setDataSourceName(entity.getDatasourceName());
        dto.setDataSourceType(DictVO.toVO(DictConstants.DATASOURCE_TYPE, entity.getDatasourceType()));
        dto.setConnectionType(DictVO.toVO(DictConstants.CONNECTION_TYPE, entity.getConnectionType()));
        dto.setHostName(entity.getHostName());
        dto.setDatabaseName(entity.getDatabaseName());
        dto.setPort(entity.getPort());
        dto.setUserName(entity.getUserName());
        dto.setPassword(entity.getPassword());
        dto.setRemark(entity.getRemark());
        if (entity.getProps() != null) {
            Map<String, String> map = JSONUtil.toBean(entity.getProps(), new TypeReference<Map<String, String>>() {
            }.getType(), true);
            dto.setGeneralProps(map.get(DataSourcePropTypeEnum.GENERAL.getCode()));
            dto.setJdbcProps(map.get(DataSourcePropTypeEnum.JDBC.getCode()));
            dto.setPoolProps(map.get(DataSourcePropTypeEnum.POOL.getCode()));
        }
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        dto.setCreator(entity.getCreator());
        dto.setEditor(entity.getEditor());
        return dto;
    }
}
