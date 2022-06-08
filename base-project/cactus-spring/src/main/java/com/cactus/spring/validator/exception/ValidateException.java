package com.cactus.spring.validator.exception;

import com.cactus.core.exception.GlobalException;
import com.cactus.spring.validator.LocalConstraintViolation;
import lombok.Getter;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.stream.Collectors;

/**
 * validate exception
 * @author lht
 * @date 2020/9/22 17:12
 */
@Getter
public class ValidateException extends GlobalException {
    /**
     * 请求源
     */
    private final Object target;
    /**
     * 方法名称
     */
    private final String methodName;
    /**
     * 验证与数据源
     */
    private final List<LocalConstraintViolation> constraintViolations;



    public ValidateException(Object target, String methodName, List<LocalConstraintViolation> constraintViolations) {
        this.target = target;
        this.methodName = methodName;
        this.constraintViolations = constraintViolations;
        //格式化验证消息
        super.resultMsg  = constraintViolations.stream()
                .map(localConstraintViolation -> localConstraintViolation.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage).collect(Collectors.joining(",")))
                .collect(Collectors.joining(","));
    }
    @Override
    public String getMessage() {
        return getResultMsg();
    }
}
