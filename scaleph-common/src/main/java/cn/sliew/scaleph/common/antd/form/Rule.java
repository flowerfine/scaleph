package cn.sliew.scaleph.common.antd.form;

import lombok.Data;

@Data
public class Rule {

    private String message;

    private Boolean required;
    private Long min;
    private Long max;
    private Long len;
    private String pattern;
}
