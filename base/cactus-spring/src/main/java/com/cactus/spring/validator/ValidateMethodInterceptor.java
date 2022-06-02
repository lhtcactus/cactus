package com.cactus.spring.validator;

import com.cactus.spring.validator.exception.ValidateException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * validate interceptor
 * @since  2021/8/2 9:40 上午
 * @author lht
 */
public class ValidateMethodInterceptor implements MethodInterceptor {
    private final Validator validator;


    public ValidateMethodInterceptor() {
        this(Validation.buildDefaultValidatorFactory());
    }

    public ValidateMethodInterceptor(ValidatorFactory validatorFactory) {
        this(validatorFactory.getValidator());
    }

    public ValidateMethodInterceptor(Validator validator) {
        this.validator = validator;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (this.isFactoryBeanMetadataMethod(invocation.getMethod())) {
            return invocation.proceed();
        } else {
            Class<?>[] groups = this.determineValidationGroups(invocation);
            ExecutableValidator execVal = this.validator.forExecutables();
            Method methodToValidate = invocation.getMethod();
            Object target = invocation.getThis();
            Set<ConstraintViolation<Object>> result;

            List<LocalConstraintViolation> constraintViolations = new ArrayList<>();

            try {
                //先验证方法
                result = execVal.validateParameters(target, methodToValidate, invocation.getArguments(), groups);

            } catch (IllegalArgumentException var7) {
                methodToValidate = BridgeMethodResolver.findBridgedMethod(ClassUtils.getMostSpecificMethod(invocation.getMethod(), target.getClass()));
                result = execVal.validateParameters(target, methodToValidate, invocation.getArguments(), groups);
            }


            if (!result.isEmpty()) {
                constraintViolations.add(new LocalConstraintViolation(result));
                throw new ValidateException(target,methodToValidate.getName(),constraintViolations);
            } else {
//                validateParams(Object[] arguments,Class<?>[] group,Object target,List<ValidateError.LocalConstraintViolation> constraintViolations){
                //验证参数值
                validateParams(invocation.getArguments(), groups,target,constraintViolations);

                //是否存在错误
                if (!constraintViolations.isEmpty()) {
                    throw new ValidateException(target,methodToValidate.getName(),constraintViolations);
                }

                Object returnValue = invocation.proceed();
                //返回值验证
                result = execVal.validateReturnValue(target, methodToValidate, returnValue, groups);
                //是否存在错误
                if (!result.isEmpty()) {
                    constraintViolations.add(new LocalConstraintViolation(result));
                    throw new ValidateException(target,methodToValidate.getName(),constraintViolations);
                } else {
                    return returnValue;
                }
            }
        }
    }

    /**
     * 是否为源方法
     * @param  method 方法体
     * @since  2021/8/2 9:40 上午
     * @author lht
     */
    private boolean isFactoryBeanMetadataMethod(Method method) {
        Class<?> clazz = method.getDeclaringClass();
        if (clazz.isInterface()) {
            return (clazz == FactoryBean.class || clazz == SmartFactoryBean.class) && !"getObject".equals(method.getName());
        } else {
            Class<?> factoryBeanType = null;
            if (SmartFactoryBean.class.isAssignableFrom(clazz)) {
                factoryBeanType = SmartFactoryBean.class;
            } else if (FactoryBean.class.isAssignableFrom(clazz)) {
                factoryBeanType = FactoryBean.class;
            }

            return factoryBeanType != null && !"getObject".equals(method.getName()) && ClassUtils.hasMethod(factoryBeanType, method.getName(), method.getParameterTypes());
        }
    }

    /**
     * 获取validate组
     * @param  invocation MethodInvocation
     * @since  2021/8/2 9:41 上午
     * @author lht
     */
    private Class<?>[] determineValidationGroups(MethodInvocation invocation) {
        Validated validatedAnn = AnnotationUtils.findAnnotation(invocation.getMethod(), Validated.class);
        if (validatedAnn == null) {
            validatedAnn = AnnotationUtils.findAnnotation(invocation.getThis().getClass(), Validated.class);
        }

        return validatedAnn != null ? validatedAnn.value() : new Class[0];
    }

    /**
     * 验证参数数组对象
     * @param  arguments 参数
     * @param  group validate 组
     * @param  target 目标对象
     * @param  constraintViolations 收集异常集合
     * @since  2021/8/2 9:41 上午
     * @author lht
     */
    private void  validateParams(Object[] arguments,Class<?>[] group,Object target,List<LocalConstraintViolation> constraintViolations){
        for(Object param : arguments){
            if(param !=null && !isJavaClass(param.getClass())){
                Set<ConstraintViolation<Object>> constraintViolations1 = validator.validate(param,group);
                if(constraintViolations1!=null && constraintViolations1.size()>0){
                    constraintViolations.add(new LocalConstraintViolation(constraintViolations1,param.getClass().getSimpleName()));
                }
            }
        }
    }



    /**
     * 判断一个类是JAVA类型还是用户定义类型
     * @param  clz class
     * @since  2021/8/2 9:42 上午
     * @author lht
     */
    private boolean isJavaClass(Class<?> clz) {
        return clz != null && clz.getClassLoader() == null;
    }



}

