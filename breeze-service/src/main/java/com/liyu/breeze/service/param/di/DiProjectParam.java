package com.liyu.breeze.service.param.di;

import com.liyu.breeze.service.param.PaginationParam;
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
