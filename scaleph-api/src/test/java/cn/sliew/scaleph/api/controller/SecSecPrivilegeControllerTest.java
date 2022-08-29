/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.api.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import cn.sliew.scaleph.ApplicationTest;
import cn.sliew.scaleph.common.annotation.Desc;
import cn.sliew.scaleph.common.constant.PrivilegeConstants;
import cn.sliew.scaleph.dao.entity.master.security.SecPrivilege;
import org.apache.commons.text.CaseUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

class SecSecPrivilegeControllerTest extends ApplicationTest {

    /**
     * 生成权限初始化sql语句及json配置
     */
    @Test
    public void testInitPrivilege() throws IllegalAccessException, FileNotFoundException {
        Class<PrivilegeConstants> clazz = PrivilegeConstants.class;
        Object obj = new Object();
        Date date = new Date();
        List<SecPrivilege> list = new ArrayList<>();
        File file = ResourceUtils.getFile("classpath:privilege.sql");
        FileUtil.writeUtf8String("------------- json -------------\n", file);
        for (Field field : clazz.getFields()) {
            if (field.isAnnotationPresent(Desc.class)) {
                Desc desc = field.getAnnotation(Desc.class);
                SecPrivilege secPrivilege = JSONUtil.toBean(desc.value(), SecPrivilege.class);
                secPrivilege.setPrivilegeCode(field.get(obj).toString());
                secPrivilege.setCreator("sys");
                secPrivilege.setEditor("sys");
                secPrivilege.setCreateTime(date);
                secPrivilege.setUpdateTime(date);
                list.add(secPrivilege);
                // json format output
                FileUtil.appendUtf8String(
                    CaseUtils.toCamelCase(field.getName(), false, '_') + ":'" +
                        field.get(obj).toString() + "',\n", file);
            }
        }
        //insert db
        FileUtil.appendUtf8String("------------- sql -------------\n", file);
        FileUtil.appendUtf8String("truncate table sec_privilege;\n", file);
        list.forEach(d -> {
            FileUtil.appendUtf8String(
                "insert into sec_privilege (id,privilege_code,privilege_name,resource_type,resource_path,pid,creator,editor) values("
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
