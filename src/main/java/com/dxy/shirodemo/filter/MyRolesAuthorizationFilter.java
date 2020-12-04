package com.dxy.shirodemo.filter;

import cn.hutool.json.JSONUtil;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName MyRolesAuthorizationFilter 自定义角色过滤器
 * @Author duxiaoyu
 * @Date 2020/12/4 14:43
 * @Version 1.0
 */
public class MyRolesAuthorizationFilter<onAccessDenied> extends RolesAuthorizationFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        Subject user = getSubject(request, response);
        // 获取当前请求所需角色信息
        String[] roleArray = (String[]) mappedValue;

        // 如果为空，代表无角色限制，可以访问
        if (roleArray == null || roleArray.length == 0) {
            return true;
        }

        // 检查是否拥有其中一个角色，拥有其中一个即可访问
        for (String item : roleArray) {
            if (user.hasRole(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 处理角色验证失败后的错误
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("text/json,charset=UTF-8");
        httpResponse.setCharacterEncoding("UTF-8");
        PrintWriter writer = httpResponse.getWriter();
        Map<String, Object> result = new HashMap<>(16);
        result.put("code", 401);
        result.put("msg", "暂无角色权限，请联系管理员!");
        writer.print(JSONUtil.toJsonStr(result));
        return false;
    }
}
