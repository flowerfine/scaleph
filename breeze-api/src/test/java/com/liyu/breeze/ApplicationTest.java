package com.liyu.breeze;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = BreezeApplication.class)
@ActiveProfiles("dev")
public class ApplicationTest {

    protected static final Logger log = LoggerFactory.getLogger(ApplicationTest.class);

}
