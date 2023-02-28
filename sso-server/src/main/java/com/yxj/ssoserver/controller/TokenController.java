package com.yxj.ssoserver.controller;

import com.yxj.ssoserver.common.RedisUtils;
import com.yxj.ssoserver.security.SecurityUser;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("sso")
public class TokenController {

    @Resource
    private RedisUtils redisUtils;

    @GetMapping("claims")
    public Map<String, Object> getClaimsByTokenUID(@RequestParam("ssoToken") String ssoToken, HttpServletRequest request){
        SecurityContext userSecurityContext = (SecurityContext)redisUtils.get(ssoToken);
        SecurityUser principal = (SecurityUser) userSecurityContext.getAuthentication().getPrincipal();
        Map<String, Object> map = new HashMap<>();
        map.put("uniqueId",principal.getUser().getId());
        map.put("name",principal.getUser().getLoginName());
        map.put("bizData", principal.getUser());
        return map;
    }
}
