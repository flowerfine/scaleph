package cn.sliew.scaleph.kubernetes.oam.application;

import lombok.Data;

import java.util.Properties;

@Data
public class ApplicationPolicy {

    private String name;
    private String type;
    private Properties properties;
}
