package cn.sliew.scaleph.service.impl;

import java.util.ArrayList;
import java.util.List;

import cn.sliew.scaleph.ApplicationTest;
import cn.sliew.scaleph.core.di.service.DiDirectoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
