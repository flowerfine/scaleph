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

package cn.sliew.scaleph.generator;

import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.Controller;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.config.builder.Mapper;
import com.baomidou.mybatisplus.generator.config.builder.Service;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * https://baomidou.com/pages/981406/#controller-%E7%AD%96%E7%95%A5%E9%85%8D%E7%BD%AE
 */
@Slf4j
public class MybatisPlusGenerator {

    private final static String AUTHOR = "wangqi";
    private final static String URL =
        "jdbc:mysql://127.0.0.1:3306/scaleph?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "123456"; //NOSONAR
    private static final String BASE_PACKAGE = "cn.sliew";
    private static final String MODULE = "scaleph";
    private static final String TABLE_PREFIX = "";

    /**
     * just add table names here and run the {@link #main(String[])} method.
     */
    private static final String[] TABLES = {"resource_kerberos"};

    public static void main(String[] args) {
        //自动生成配置
        FastAutoGenerator generator = FastAutoGenerator.create(dataSourceConfig())
            .globalConfig(MybatisPlusGenerator::globalConfig)
            .packageConfig(MybatisPlusGenerator::packageConfig)
            .templateConfig(MybatisPlusGenerator::templateConfig)
            .strategyConfig(MybatisPlusGenerator::strategyConfig)
            .injectionConfig(MybatisPlusGenerator::injectionConfig);
        generator.execute();
    }

    /**
     * 数据源配置
     *
     * @return DataSourceConfig
     */
    private static DataSourceConfig.Builder dataSourceConfig() {
        return new DataSourceConfig.Builder(URL, USERNAME, PASSWORD)
            .dbQuery(new MySqlQuery())
            .typeConvert(new MySqlTypeConvert())
            .keyWordsHandler(new MySqlKeyWordsHandler());
    }

    /**
     * 全局配置
     *
     * @return GlobalConfig
     */
    private static void globalConfig(GlobalConfig.Builder builder) {
        builder.fileOverride()
            .outputDir(System.getProperty("user.dir") +
                "/scaleph-support/scaleph-generator/src/main/java/")
            .author(AUTHOR)
            .fileOverride()
            .enableSwagger()
            .dateType(DateType.ONLY_DATE)
            .commentDate("yyyy-MM-dd");
    }

    /**
     * 包配置
     *
     * @return PackageConfig
     */
    private static void packageConfig(PackageConfig.Builder builder) {
        builder.parent(BASE_PACKAGE)
            .moduleName(MODULE)
            .entity("dao.entity")
            .service("service")
            .serviceImpl("service.impl")
            .mapper("dao.mapper")
            .xml("dao.mapper")
            .controller("api.controller")
            .other("other");
//                .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "/Users/wangqi/Downloads/generator"));
    }

    private static void templateConfig(TemplateConfig.Builder builder) {
        builder.controller(null)
                .service(null)
                .serviceImpl(null);
    }

    /**
     * 策略配置
     *
     * @return StrategyConfig
     */
    private static void strategyConfig(StrategyConfig.Builder builder) {
        builder.enableCapitalMode()
            .enableSkipView()
            .disableSqlFilter()
            .addInclude(TABLES)
            .addTablePrefix(TABLE_PREFIX);

        Entity.Builder entityBuilder = builder.entityBuilder();
        entityBuilder.superClass(BaseDO.class)
            .enableLombok()
            .enableTableFieldAnnotation()
            .enableRemoveIsPrefix()
            .naming(NamingStrategy.underline_to_camel)
            .columnNaming(NamingStrategy.underline_to_camel)
            .addSuperEntityColumns("id", "creator", "created_time", "editor", "update_time")
            .idType(IdType.AUTO)
            .addTableFills(new Column("create_time", FieldFill.INSERT))
            .addTableFills(new Property("updateTime", FieldFill.INSERT_UPDATE))
            .formatFileName("%s");

        Mapper.Builder mapperBuilder = builder.mapperBuilder();
        mapperBuilder.superClass(BaseMapper.class)
            .enableMapperAnnotation()
            .enableBaseResultMap()
            .enableBaseColumnList()
            .formatMapperFileName("%sMapper")
            .formatXmlFileName("%sMapper");

        Service.Builder serviceBuilder = builder.serviceBuilder();
        serviceBuilder.formatServiceFileName("%sService")
            .formatServiceImplFileName("%sServiceImp")
            .build();


        Controller.Builder controllerBuilder = builder.controllerBuilder();
        controllerBuilder.enableHyphenStyle()
            .enableRestStyle()
            .formatFileName("%sController")
            .build();

    }

    /**
     * 自定义配置
     *
     * @return InjectionConfig
     */
    private static void injectionConfig(InjectionConfig.Builder builder) {

    }
}
