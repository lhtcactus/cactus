package com.cactus.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.cactus.core.toolkit.IdUtil;
import com.cactus.mybatis.injector.ExtensionSqlInjector;
import com.cactus.mybatis.interceptor.ExtensionPaginationInnerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus 自动配置
 * @author lht
 * @since 2021/10/15 4:35 下午
 */
@Configuration
@ConditionalOnProperty(
        prefix = "cactus.mybatis",
        name ={"enable"},
        havingValue = "true",
        matchIfMissing = true)
public class MybatisPlusAuto {


    /**
     * 设置 mybatis-plus MybatisPlusProperties 配置
     * @since  2021/10/15 4:55 下午
     * @author lht
     */
    @Bean("identifierGeneratorCustomizer")
    @ConditionalOnMissingBean(name = "identifierGeneratorCustomizer")
    public MybatisPlusPropertiesCustomizer identifierGeneratorCustomizer(){
        return properties -> {
            GlobalConfig config = properties.getGlobalConfig();
            //设置生成id规则
            config.setIdentifierGenerator(new IdentifierGenerator() {
                @Override
                public Number nextId(Object entity) {
                    return IdUtil.nextIdNumber();
                }

                @Override
                public String nextUUID(Object entity) {
                    return IdUtil.nextId();
                }
            });
        };
    }

    @Bean("sqlInjectorCustomizer")
    @ConditionalOnMissingBean(name = "sqlInjectorCustomizer")
    public MybatisPlusPropertiesCustomizer sqlInjectorCustomizer(){
        return properties -> {
            GlobalConfig config = properties.getGlobalConfig();
            //设置扩展sql注入
            config.setSqlInjector(new ExtensionSqlInjector());
        };
    }


    /**
     * 默认使用MARIADB
     * @author lht
     * @since  2021/1/29 10:28
     */
    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new ExtensionPaginationInnerInterceptor(DbType.MARIADB));
        return interceptor;
    }


}
