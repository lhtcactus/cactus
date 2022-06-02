package com.cactus.swagger.config;

import com.cactus.core.exception.GlobalException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.stream.Collectors;

/**
 * 配置swagger
 * @author lht
 * @date  2020/9/17 10:29
 */
@Configuration
@ConditionalOnProperty(prefix = "cactus.swagger"
        ,name = {"enable"}
        ,havingValue = "true")
@EnableSwagger2
public class SwaggerAutoConfigurer {


    @Bean
    public Docket docket(DocProperties doc) {
        if(!StringUtils.hasLength(doc.getBasePackage())){
            throw new GlobalException("swagger basePackage cannot be empty");
        }
        if(!StringUtils.hasText(doc.getName())){
            throw new GlobalException("swagger name cannot be empty");
        }

        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        if(!CollectionUtils.isEmpty(doc.getDocParams())){
            docket.globalRequestParameters(doc.getDocParams().stream()
                    .map(docParam -> new RequestParameterBuilder()
                        .name(docParam.getName())
                        .in(docParam.getParameterType())
                        .description(docParam.getDescription())
                        .required(docParam.getRequired())
                        .build())
                    .collect(Collectors.toList()));
        }
        return docket
                .groupName(doc.getName())
                .apiInfo(new ApiInfoBuilder()
                        .title(doc.getTitle())
                        .description(doc.getDescription())
                        .contact(new Contact(doc.getAuthor(), doc.getUrl(), doc.getEmail()))
                        .version(doc.getVersion())
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage(doc.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }
}
