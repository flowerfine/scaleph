package com.liyu.breeze.service.impl;

import com.liyu.breeze.ApplicationTest;
import com.liyu.breeze.service.di.DiDirectoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

class DiDirectoryServiceImplTest extends ApplicationTest {

    @Autowired
    private DiDirectoryService directoryService;

    @Test
    void simpleTest() {
        List<Long> ids = new ArrayList<Long>() {{
            add(3L);
            add(4L);
            add(7L);
        }};
        this.directoryService.loadFullPath(ids);
    }
}
