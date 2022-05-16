package cn.sliew.scaleph.api.config;

import cn.sliew.scaleph.api.util.SecurityUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Configuration
public class MybatisConfig {

    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * @see cn.sliew.scaleph.api.aspect.InsertUpdateAspect
     */
    @Component
    public static class MetaHandler implements MetaObjectHandler {

        @Override
        public void insertFill(MetaObject metaObject) {
            String userName = getUserNameOrDefault();
            this.strictInsertFill(metaObject, "createTime", () -> new Date(), Date.class);
            this.strictInsertFill(metaObject, "updateTime", () -> new Date(), Date.class);
            this.strictInsertFill(metaObject, "creator", () -> userName, String.class);
            this.strictInsertFill(metaObject, "editor", () -> userName, String.class);
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            String userName = getUserNameOrDefault();
            this.strictUpdateFill(metaObject, "updateTime", () -> new Date(), Date.class);
            this.strictUpdateFill(metaObject, "editor", () -> userName, String.class);
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
