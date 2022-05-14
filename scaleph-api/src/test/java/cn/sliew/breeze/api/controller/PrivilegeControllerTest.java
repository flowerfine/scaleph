package cn.sliew.breeze.api.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import cn.sliew.breeze.ApplicationTest;
import cn.sliew.breeze.common.annotation.Desc;
import cn.sliew.breeze.common.constant.PrivilegeConstants;
import cn.sliew.breeze.dao.entity.master.security.Privilege;
import org.apache.commons.text.CaseUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class PrivilegeControllerTest extends ApplicationTest {

    /**
     * 生成权限初始化sql语句及json配置
     */
    @Test
    public void testInitPrivilege() throws IllegalAccessException, FileNotFoundException {
        Class<PrivilegeConstants> clazz = PrivilegeConstants.class;
        Object obj = new Object();
        Date date = new Date();
        List<Privilege> list = new ArrayList<>();
        File file = ResourceUtils.getFile("classpath:privilege.sql");
        FileUtil.writeUtf8String("------------- json -------------\n", file);
        for (Field field : clazz.getFields()) {
            if (field.isAnnotationPresent(Desc.class)) {
                Desc desc = field.getAnnotation(Desc.class);
                Privilege privilege = JSONUtil.toBean(desc.value(), Privilege.class);
                privilege.setPrivilegeCode(field.get(obj).toString());
                privilege.setCreator("sys");
                privilege.setEditor("sys");
                privilege.setCreateTime(date);
                privilege.setUpdateTime(date);
                list.add(privilege);
                // json format output
                FileUtil.appendUtf8String(CaseUtils.toCamelCase(field.getName(), false, '_') + ":'" + field.get(obj).toString() + "',\n", file);
            }
        }
        //insert db
        FileUtil.appendUtf8String("------------- sql -------------\n", file);
        FileUtil.appendUtf8String("truncate table t_privilege;\n", file);
        list.forEach(d -> {
            FileUtil.appendUtf8String("insert into t_privilege (id,privilege_code,privilege_name,resource_type,resource_path,pid,creator,editor) values("
                    + d.getId() + ",'"
                    + d.getPrivilegeCode() + "','"
                    + d.getPrivilegeName() + "','"
                    + d.getResourceType() + "','"
                    + d.getResourcePath() + "',"
                    + d.getPid() + ",'"
                    + d.getCreator() + "','"
                    + d.getEditor() +
                    "');\n", file);
        });

    }
}
