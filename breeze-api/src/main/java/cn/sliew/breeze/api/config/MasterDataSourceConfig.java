package cn.sliew.breeze.api.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "cn.sliew.breeze.dao.mapper", sqlSessionFactoryRef = MasterDataSourceConfig.MASTER_SQL_SESSION_FACTORY)
public class MasterDataSourceConfig {

    static final String MASTER_SQL_SESSION_FACTORY = "masterSqlSessionFactory";

    static final String MASTER_DATA_SOURCE_FACTORY = "masterDataSource";
    static final String MASTER_TRANSACTION_MANAGER_FACTORY = "masterTransactionManager";

    @Autowired
    private MybatisPlusInterceptor mybatisPlusInterceptor;

    @Primary
    @Bean(MASTER_DATA_SOURCE_FACTORY)
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean(MASTER_TRANSACTION_MANAGER_FACTORY)
    public DataSourceTransactionManager masterTransactionManager() {
        return new DataSourceTransactionManager(masterDataSource());
    }

    @Primary
    @Bean(MASTER_SQL_SESSION_FACTORY)
    public SqlSessionFactory masterSqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();

        MybatisPlusProperties props = new MybatisPlusProperties();
        props.setMapperLocations(new String[]{"classpath*:cn.sliew.breeze.dao.mapper/*Mapper.xml"});
        factoryBean.setMapperLocations(props.resolveMapperLocations());

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setLogImpl(Slf4jImpl.class);
        factoryBean.setConfiguration(configuration);

        factoryBean.setDataSource(masterDataSource());
        factoryBean.setTypeAliasesPackage("cn.sliew.breeze.dao.entity");
        factoryBean.setPlugins(mybatisPlusInterceptor);
        return factoryBean.getObject();
    }

}
