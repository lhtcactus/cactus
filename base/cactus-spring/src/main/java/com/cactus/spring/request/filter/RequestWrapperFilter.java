package com.cactus.spring.request.filter;

import com.cactus.spring.request.ReusableRequestWrapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 替换request
 * @author lht
 * @since 2021/3/1 14:16
 */
@ConditionalOnProperty(prefix = "cactus.request.filter",
        name="enable",
        havingValue = "true",
        matchIfMissing = true)
@WebFilter
@Order
public class RequestWrapperFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (servletRequest.getContentType() != null && servletRequest.getContentType().contains("application/json")) {
            //Read request.getBody() as many time you need
            filterChain.doFilter(new ReusableRequestWrapper(request), servletResponse);
        }else{
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }
}
