package cn.sliew.scaleph.api.config;

import cn.sliew.scaleph.common.constant.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger 配置
 *
 * @author gleiyu
 */
@EnableOpenApi
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket apiDocket(Environment environment) {
        Profiles profiles = Profiles.of("dev");
        boolean flag = environment.acceptsProfiles(profiles);
        //token param
        RequestParameterBuilder tokenParam = new RequestParameterBuilder();
        List<RequestParameter> paramList = new ArrayList<>();
        tokenParam.name(Constants.TOKEN_KEY)
                .description("user token")
                .in("header")
                .required(true)
                .build();
        paramList.add(tokenParam.build());
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.sliew.scalegh"))
                .paths(PathSelectors.any())
                .build()
                .globalRequestParameters(paramList);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Scaleph API文档")
                .description("Scaleph API文档")
                .contact(new Contact("liyu", "http://www.sliew.cn", "liyu@sliew.cn"))
                .version("0.0.1-SNAPSHOT")
                .build();
    }
}
