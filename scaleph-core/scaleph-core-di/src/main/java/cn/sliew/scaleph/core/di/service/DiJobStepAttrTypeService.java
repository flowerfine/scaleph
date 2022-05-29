package cn.sliew.scaleph.core.di.service;

import java.util.List;

import cn.sliew.scaleph.core.di.service.dto.DiJobStepAttrTypeDTO;

/**
 * <p>
 * 数据集成-作业步骤参数类型信息 服务类
 * </p>
 *
 * @author liyu
 * @since 2022-03-30
 */
public interface DiJobStepAttrTypeService {

    /**
     * 查询全部参数说明，
     *
     * @return map
     */
    List<DiJobStepAttrTypeDTO> listByType(String stepType, String stepName);
}
