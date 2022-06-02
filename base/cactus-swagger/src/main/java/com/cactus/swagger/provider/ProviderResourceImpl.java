package com.cactus.swagger.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.List;

/**
 * @author lht
 * @date 2020/9/17 14:05
 */
@Component
public class ProviderResourceImpl implements SwaggerResourcesProvider {

    @Value("${cactus.swagger.provider:}")
    public List<SwaggerResource> resources;


    @Override
    public List<SwaggerResource> get() {
        return resources;
    }



}