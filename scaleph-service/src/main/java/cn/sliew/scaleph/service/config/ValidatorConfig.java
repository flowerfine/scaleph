package cn.sliew.scaleph.service.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 数据校验快速失败返回模式
 *
 * @author gleiyu
 * @date 2020-5-6 23:33:54
 */
@Configuration
public class ValidatorConfig {
    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure()
                .addProperty("hibernate.validator.fail_fast", "true")
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        postProcessor.setValidator(validator());
        return postProcessor;
    }
}
