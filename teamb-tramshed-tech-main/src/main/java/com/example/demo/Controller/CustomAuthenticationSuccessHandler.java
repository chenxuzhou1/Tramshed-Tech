//package com.example.demo.Controller;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    public CustomAuthenticationSuccessHandler() {
//        // 设置登录成功后重定向的默认URL
//        setDefaultTargetUrl("/EventSpace");
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        if (authentication instanceof OAuth2AuthenticationToken) {
//            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
//            OAuth2User user = oauthToken.getPrincipal();
//            String email = user.getAttribute("email");  // 确保邮件属性与你的身份提供者返回的一致
//
//            // 设置Cookie
//            Cookie cookie = new Cookie("userEmail", email);
//            cookie.setHttpOnly(false);
//            cookie.setPath("/");
//            response.addCookie(cookie);
//            response.sendRedirect("/EventSpace");
//
//        }
//        else {
//
//            response.sendRedirect("/loginError");
//        }
//
//
//    // 调用父类的方法来处理重定向或其他逻辑
//        super.onAuthenticationSuccess(request, response, authentication);
//    }
//}
//
