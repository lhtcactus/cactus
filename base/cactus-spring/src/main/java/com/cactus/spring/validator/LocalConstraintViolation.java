package com.cactus.spring.validator;

import lombok.Getter;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * 关联数据源和验证信息
 * @author lht
 * @date 2020/9/22 17:15
 */
@Getter
public class LocalConstraintViolation {
    /**
     * 验证结果
     */
    private Set<ConstraintViolation<Object>> constraintViolations;
    /**
     * 对象源
     */
    private String objectName;
    /**
     * 是否是方法内对象
     */
    private boolean isObject;

    public LocalConstraintViolation(Set<ConstraintViolation<Object>> constraintViolations, String objectName) {
        this.constraintViolations = constraintViolations;
        this.objectName = objectName;
        this.isObject = true;
    }

    public LocalConstraintViolation(Set<ConstraintViolation<Object>> constraintViolations) {
        this.constraintViolations = constraintViolations;
        this.isObject = false;
    }

}
