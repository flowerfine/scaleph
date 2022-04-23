package com.liyu.breeze.service.param.di;

import com.liyu.breeze.service.param.PaginationParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author gleiyu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DiClusterConfigParam extends PaginationParam {

    private String clusterName;

    private String clusterType;
}
