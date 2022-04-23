package com.liyu.breeze.common.enums;

import lombok.Getter;

@Getter
public enum DeploymentMode {

    APPLICATION("Application"),
    PER_JOB("Per-Job"),
    SESSION("session");

    private String name;

    DeploymentMode(String name) {
        this.name = name;
    }
}
