package cn.sliew.scaleph.ds.modal;

import cn.sliew.milky.common.util.JacksonUtil;

public class Main {

    public static void main(String[] args) {
        String json = "{\"dsTypeId\":1,\"type\":\"MySQL\",\"version\":\"11\",\"name\":\"dev\",\"remark\":\"dev 环境\",\"driverClassName\":\"sss.xxx.sttt\",\"url\":\"url\",\"user\":\"ssss\",\"password\":\"sss\"}";

        AbstractDataSource dataSource = JacksonUtil.parseJsonString(json, AbstractDataSource.class);
        System.out.println(JacksonUtil.toJsonString(dataSource));
    }
}
