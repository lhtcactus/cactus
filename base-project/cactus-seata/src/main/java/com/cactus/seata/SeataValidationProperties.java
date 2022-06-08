package com.cactus.seata;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lht
 * @since 2021/10/20 2:52 下午
 */
@Configuration
@ConfigurationProperties(prefix = "cactus.seata.validate")
@Getter
@Setter
public class SeataValidationProperties {

    /**
     * validate data fields
     * yml example
     *
     * cactus:
     *   seata:
     *     validate:
     *       fieldsGroups:
     *         - ["id","name"]
     *         - ["update"]
     *
     * 任意一个集合满足条件就通过
     */
    private String[][] fieldsGroups;
    /**
     * 是否启用 true/false default true
     */
    private Boolean enable=true;
}
