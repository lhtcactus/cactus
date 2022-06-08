package com.cactus.swagger.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author lht
 * @since 2021/10/21 4:23 下午
 */
@Configuration
@ConfigurationProperties(prefix = "cactus.swagger.doc")
public class DocProperties {
    /**
     * swagger group name
     */
    private String name;
    /**
     * api title
     */
    private String title = "接口文档";
    /**
     * api desc
     */
    private String description = "swagger接口文档";
    /**
     * author
     */
    private String author = "admin";
    /**
     * info url
     */
    private String url;
    /**
     * author email
     */
    private String email;

    /**
     * api version
     */
    private String version = "1.0.0";
    /**
     * scanning project base package
     */
    private String basePackage;

    /**
     * 参数列表
     */
    private List<DocParam> docParams;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public List<DocParam> getDocParams() {
        return docParams;
    }

    public void setDocParams(List<DocParam> docParams) {
        this.docParams = docParams;
    }
}
