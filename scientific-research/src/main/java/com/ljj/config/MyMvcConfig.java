package com.ljj.config;


import com.ljj.component.LoginHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //外部访问路径映射到本地磁盘路径
        registry.addResourceHandler("/temp/**").addResourceLocations("file:D:/temp/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //视图映射 浏览器发送/controller 来到 （controller.html） 自动拼接前后缀 controller
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController("/setPass").setViewName("setPass");
        registry.addViewController("/info").setViewName("info");
        registry.addViewController("/userList").setViewName("userList");
        registry.addViewController("/addUser").setViewName("addUser");
        registry.addViewController("/addResearch").setViewName("addResearch");
        registry.addViewController("/researchList4User").setViewName("researchList4User");
        registry.addViewController("/statBySchool4Admin").setViewName("statBySchool4Admin");
        registry.addViewController("/stat4User").setViewName("stat4User");
        registry.addViewController("/statByHalfYear4Admin").setViewName("statByHalfYear4Admin");
        registry.addViewController("/researchList4Admin").setViewName("researchList4Admin");
        registry.addViewController("/addNotice").setViewName("addNotice");
        registry.addViewController("/auditList4Admin").setViewName("auditList4Admin");
        registry.addViewController("/statByPerson4Admin").setViewName("statByPerson4Admin");
        registry.addViewController("/noticeList").setViewName("noticeList");
        registry.addViewController("/noticeDetail").setViewName("noticeDetail");
        registry.addViewController("/noticeDetail/").setViewName("noticeDetail");
        registry.addViewController("/menuList").setViewName("menuList");
    }

    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截所有请求
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
                //不拦截一下请求
                .excludePathPatterns("/","/index","/index.html","/login")
                .excludePathPatterns("/css/**","/images/**/","/js/**","/layui/**","/layuiadmin/**");
    }
}
