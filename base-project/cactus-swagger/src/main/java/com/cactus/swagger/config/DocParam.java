package com.cactus.swagger.config;

/**
 * swagger 参数配置
 * @author lht
 * @since 2022/2/16 10:50 上午
 */
public class DocParam {
    /**
     * 参数名称
     */
    private String name;
    /**
     * 参数描述
     */
    private String description;

    /**
     * 是否必填
     */
    private Boolean required = false;
    /**
     * 参数位置 例如：header
     */
    private String parameterType = "header";


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }


}
