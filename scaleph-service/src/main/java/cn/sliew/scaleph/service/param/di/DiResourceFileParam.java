package cn.sliew.scaleph.service.param.di;

import cn.sliew.scaleph.service.param.PaginationParam;
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
