package cn.sliew.scaleph.service.param.di;

import cn.sliew.scaleph.common.param.PaginationParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gleiyu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DiProjectParam extends PaginationParam {
    private String projectCode;

    private String projectName;
}
