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

package cn.sliew.scaleph.api.config;

import java.util.Date;

import cn.sliew.scaleph.security.util.SecurityUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Configuration
public class MybatisConfig {

    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * replace InsertUpdateAspect, which already deleted
     */
    @Component
    public static class MetaHandler implements MetaObjectHandler {

        @Override
        public void insertFill(MetaObject metaObject) {
            String userName = getUserNameOrDefault();
            this.strictInsertFill(metaObject, "creator", () -> userName, String.class);
            this.strictInsertFill(metaObject, "createTime", () -> new Date(), Date.class);
            this.strictInsertFill(metaObject, "editor", () -> userName, String.class);
            this.strictInsertFill(metaObject, "updateTime", () -> new Date(), Date.class);
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            String userName = getUserNameOrDefault();
            this.strictUpdateFill(metaObject, "editor", () -> userName, String.class);
            this.strictUpdateFill(metaObject, "updateTime", () -> new Date(), Date.class);
        }

        private String getUserNameOrDefault() {
            String userName = SecurityUtil.getCurrentUserName();
            if (StringUtils.isEmpty(userName)) {
                return "sys";
            }
            return userName;
        }

    }
}
