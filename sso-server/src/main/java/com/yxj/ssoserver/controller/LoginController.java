package com.yxj.ssoserver.controller;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.yxj.ssoserver.common.RestResponse;
import com.yxj.ssoserver.mappers.entity.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("sso")
public class LoginController {

    @GetMapping("loginPage.html")
    public String loginPage(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String callbackUrl) {
        return "login";
    }

    @PostMapping("doLogin")
    public void doLogin(UserEntity user, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uuid = UUID.randomUUID().toString();
        //创建cookie
        Cookie cookie = new Cookie("ssoToken", uuid);
        cookie.setHttpOnly(true);
        cookie.setDomain("localhost");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
        response.addHeader("ssoToken", uuid);

        String callbackUrl = request.getParameter("callbackUrl");
        if (StringUtils.hasText(callbackUrl)){
            StringBuffer sb = new StringBuffer(callbackUrl);
            if (callbackUrl.contains("?")) {
                sb.append("&");
            } else {
                sb.append("?");
            }
            sb.append("ssoToken=").append(uuid);
            response.sendRedirect(sb.toString());
            return;
        }

        PrintWriter writer = response.getWriter();
        Object ok = JSON.toJSONString( RestResponse.success(uuid));
        writer.println(ok);
        writer.flush();
    }
}
