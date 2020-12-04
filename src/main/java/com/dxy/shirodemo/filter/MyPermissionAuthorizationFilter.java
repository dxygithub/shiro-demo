package com.dxy.shirodemo.filter;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName MyPermissionAuthorizationFilter 自定义权限过滤器
 * @Author duxiaoyu
 * @Date 2020/12/4 14:59
 * @Version 1.0
 */
public class MyPermissionAuthorizationFilter extends PermissionsAuthorizationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyPermissionAuthorizationFilter.class);

    /**
     * 处理权限验证失败的错误
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
        result.put("msg", "暂无访问权限，请联系管理员!");
        writer.print(JSONUtil.toJsonStr(result));
        return false;
    }
}
