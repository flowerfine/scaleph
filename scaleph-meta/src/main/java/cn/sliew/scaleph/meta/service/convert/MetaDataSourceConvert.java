package cn.sliew.scaleph.meta.service.convert;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.meta.MetaDatasource;
import cn.sliew.scaleph.meta.service.dto.MetaDatasourceDTO;
import cn.sliew.scaleph.system.service.convert.DictVoConvert;
import com.fasterxml.jackson.core.type.TypeReference;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper(uses = {DictVoConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MetaDataSourceConvert extends BaseConvert<MetaDatasource, MetaDatasourceDTO> {
    MetaDataSourceConvert INSTANCE = Mappers.getMapper(MetaDataSourceConvert.class);

    @Override
    default MetaDatasource toDo(MetaDatasourceDTO dto) {
        MetaDatasource metaDatasource = new MetaDatasource();
        metaDatasource.setName(dto.getName());
        metaDatasource.setVersion(dto.getVersion());
        metaDatasource.setProps(JacksonUtil.toJsonString(dto.getProps()));
        metaDatasource.setAdditionalProps(JacksonUtil.toJsonString(dto.getAdditionalProps()));
        metaDatasource.setRemark(dto.getRemark());
        return metaDatasource;
    }

    @Override
    default MetaDatasourceDTO toDto(MetaDatasource entity) {
        MetaDatasourceDTO metaDatasourceDTO = new MetaDatasourceDTO();
        metaDatasourceDTO.setName(entity.getName());
        metaDatasourceDTO.setVersion(entity.getVersion());
        metaDatasourceDTO.setProps(JacksonUtil.parseJsonString(entity.getProps(), new TypeReference<Map<String, Object>>() {
        }));
        metaDatasourceDTO.setAdditionalProps(JacksonUtil.parseJsonString(entity.getAdditionalProps(), new TypeReference<Map<String, Object>>() {
        }));
        metaDatasourceDTO.setRemark(entity.getRemark());
        return metaDatasourceDTO;
    }
}
