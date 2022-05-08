package cn.sliew.breeze.service.param.di;

import cn.sliew.breeze.service.param.PaginationParam;
import lombok.Data;

import java.util.Date;

@Data
public class DiJobLogParam extends PaginationParam {

    private Long projectId;

    private String jobCode;

    private Date startTime;

    private Date endTime;

    private String jobInstanceState;

}
