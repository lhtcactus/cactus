package com.cactus.spring.log;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.cactus.core.toolkit.JsonUtil;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.StringJoiner;

/**
 * @author lht
 * @since 2021/10/21 10:45 上午
 */
public class CactusLog {
    /**
     * 请求id
     */
    private String requestId;
    /**
     * 请求url
     */
    private String url;
    /**
     * get/post ...
     */
    private String method;
    /**
     * 请求开始时间
     */
    private LocalDateTime startTime;
    /**
     * 请求结束时间
     */
    private LocalDateTime endTime;
    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 参数
     */
    private Collection<Object> params;
    /**
     * 异常发生时间
     */
    private LocalDateTime errorTime;
    /**
     * 失败异常
     */
    private Throwable throwable;

    public String log() {
        StringJoiner log = new StringJoiner(",");
        log.add("request_id: " + requestId)
                .add("url: " + url)
                .add("method: " + method);
        if (startTime != null) {
            log.add("start_time: " + DateUtil.format(startTime, DatePattern.NORM_DATETIME_MS_PATTERN))
                    .add("end_time: " + DateUtil.format(endTime, DatePattern.NORM_DATETIME_MS_PATTERN))
                    .add("cost: " + (endTime.toInstant(ZoneOffset.of("+8")).toEpochMilli() - startTime.toInstant(ZoneOffset.of("+8")).toEpochMilli()));
        }
        if (errorTime != null) {
            log.add("error-time: " + DateUtil.format(errorTime, DatePattern.NORM_DATETIME_MS_PATTERN));
        }
        log.add("success: " + (success ? "成功" : "失败"));
        if (CollectionUtils.isEmpty(params)) {
            log.add("params: " + JsonUtil.transferToJson(params));
        }
        return "["+log+"]";
    }



    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Collection<Object> getParams() {
        return params;
    }

    public void setParams(Collection<Object> params) {
        this.params = params;
    }

    public LocalDateTime getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(LocalDateTime errorTime) {
        this.errorTime = errorTime;
    }
}
