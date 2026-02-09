package com.yulgnier.web.app.custom.interceptor;

import com.yulgnier.common.login.LoginUser;
import com.yulgnier.common.login.LoginUserHolder;
import com.yulgnier.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("access-token");   // 从请求头中获取token
        Claims claims = JwtUtil.parseToken(token);
        Long id = claims.get("id", Long.class);
        String username = claims.get("username", String.class);
        LoginUserHolder.setLoginUser(new LoginUser(id, username));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        LoginUserHolder.clear();
    }
}
