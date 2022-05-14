package cn.sliew.scalegh.service.param.di;

import cn.sliew.scalegh.service.param.PaginationParam;
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
