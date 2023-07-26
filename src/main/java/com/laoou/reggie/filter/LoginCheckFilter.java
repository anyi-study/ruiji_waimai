package com.laoou.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.laoou.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
//    路径匹配器 支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
//       1、获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截请求:{}",request.getRequestURI());
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };
//2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);
//3、如果不需要处理，则直接放行
        if (check){
            log.info("请求无需处理:{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
//4、判断登录状态，如果已登录，则刚直接放行
        if(request.getSession().getAttribute("employee" )!=null){
            log.info("用户已登录,用户id:{}",request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);
            return;
        }
//5、如果未登录则返回未登录结果
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    public boolean check(String [] urls,String reqURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, reqURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
