package com.dxy.shirodemo.controller;

import com.dxy.shirodemo.entity.User;
import com.dxy.shirodemo.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserController
 * @Author duxiaoyu
 * @Date 2020/12/3 15:10
 * @Version 1.0
 */
@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    @ResponseBody
    public String login() {
        return "login";
    }

    @GetMapping("/error")
    @ResponseBody
    public String error() {
        return "error";
    }

    @GetMapping("/index")
    @ResponseBody
    public String index() {
        return "index";
    }

    @PostMapping("/doLogin")
    @ResponseBody
    public ResponseEntity doLogin(String account, String pwd, HttpSession session) {
        Map<String, Object> result = new LinkedHashMap<>(16);
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(account, pwd);
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            result.put("code", 200);
            result.put("msg", "登录成功");
            result.put("userInfo", subject.getPrincipal());
            result.put("token", subject.getSession().getId());
        } catch (UnknownAccountException e) {
            result.put("code", 500);
            result.put("msg", "账号不存在");
        } catch (IncorrectCredentialsException e) {
            result.put("code", 500);
            result.put("msg", "用户名或密码错误");
        } catch (LockedAccountException e) {
            result.put("code", 500);
            result.put("msg", "登录失败，该用户已被冻结");
        } catch (AuthenticationException e) {
            result.put("code", 500);
            result.put("msg", "服务器正忙");
        }
        return ResponseEntity.ok(result);
    }

    // @RequiresPermissions("user_list")
    @GetMapping("/queryUserList")
    @ResponseBody
    public ResponseEntity queryUserList() {
        List<User> userList = userService.getAllUserList();
        Map<String, Object> result = new HashMap<>();
        result.put("list", userList);
        result.put("code", 200);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:/user/login";
    }
}
