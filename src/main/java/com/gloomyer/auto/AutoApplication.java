package com.gloomyer.auto;

import com.gloomyer.auto.filter.AccessControlAllowOriginFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

@SpringBootApplication
public class AutoApplication {

    @Bean
    public FilterRegistrationBean myFilterBean(AccessControlAllowOriginFilter filter) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(filter);//设置为自定义的过滤器MyFilter
        filterRegistrationBean.addUrlPatterns("/*");//拦截所有请求
        filterRegistrationBean.setOrder(1);//优先级为1
        return filterRegistrationBean;
    }

    public static void main(String[] args) {
        SpringApplication.run(AutoApplication.class, args);
    }
}
