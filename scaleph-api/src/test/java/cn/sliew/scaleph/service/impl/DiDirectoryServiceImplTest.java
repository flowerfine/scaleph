package cn.sliew.scaleph.service.impl;

import cn.sliew.scaleph.ApplicationTest;
import cn.sliew.scaleph.service.di.DiDirectoryService;
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
