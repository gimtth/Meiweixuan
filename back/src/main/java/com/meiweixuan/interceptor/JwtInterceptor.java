package com.meiweixuan.interceptor;

import com.meiweixuan.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行OPTIONS请求
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        
        // 如果没有token，返回未授权
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        
        token = token.substring(7);
        
        try {
            // 验证token
            String username = jwtTokenUtil.getUsernameFromToken(token);
            if (username != null && !jwtTokenUtil.isTokenExpired(token)) {
                // 将用户信息存入请求属性
                request.setAttribute("userId", jwtTokenUtil.getUserIdFromToken(token));
                request.setAttribute("username", username);
                request.setAttribute("userType", jwtTokenUtil.getUserTypeFromToken(token));
                return true;
            }
        } catch (Exception e) {
            // token无效或已过期
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
} 