package cn.sliew.breeze.api.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * mybatis config
 *
 * @author gleiyu
 */
@Configuration
@MapperScan(basePackages = "cn.sliew.breeze.dao.mapper.log", sqlSessionFactoryRef = LogMybatisConfig.LOG_SQL_SESSION_FACTORY)
public class LogMybatisConfig {

    private final Logger log = LoggerFactory.getLogger(LogMybatisConfig.class);

    final static String LOG_SQL_SESSION_FACTORY = "logSqlSessionFactory";

    @Bean("logPagination")
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * log datasource
     *
     * @return datasource
     */
    @Bean("logDataSource")
    @ConfigurationProperties("spring.datasource.druid.log")
    public DruidDataSource logDataSource() {
        log.info("begin init log dataSource by druid database pool ...");
        return DruidDataSourceBuilder.create().build();
    }

    @Bean("logTransactionManager")
    public DataSourceTransactionManager logTransactionManager() {
        return new DataSourceTransactionManager(logDataSource());
    }

    /**
     * log mybatis config
     *
     * @return SqlSessionFactory
     * @throws Exception Exception
     */
    @Bean(LOG_SQL_SESSION_FACTORY)
    public SqlSessionFactory logSqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        MybatisPlusProperties props = new MybatisPlusProperties();
        props.setMapperLocations(new String[]{"classpath*:cn.sliew.breeze.dao.mapper/log/*Mapper.xml"});
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLogImpl(StdOutImpl.class);
        factoryBean.setDataSource(logDataSource());
        factoryBean.setMapperLocations(props.resolveMapperLocations());
        factoryBean.setTypeAliasesPackage("cn.sliew.breeze.dao.entity");
        factoryBean.setPlugins(paginationInterceptor());
        factoryBean.setConfiguration(configuration);
        return factoryBean.getObject();
    }

}
