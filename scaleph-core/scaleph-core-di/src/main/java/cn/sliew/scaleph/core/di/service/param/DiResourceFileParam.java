package cn.sliew.scaleph.core.di.service.param;

import cn.sliew.scaleph.common.param.PaginationParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gleiyu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DiResourceFileParam extends PaginationParam {
    private Long projectId;
    private String fileName;
}
