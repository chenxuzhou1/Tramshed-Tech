//package com.example.demo.Config;
//
//import jakarta.servlet.http.Cookie;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class GoogleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//
//        String email = authentication.getName();
//
//
//        Cookie cookie = new Cookie("userEmail", email);
//        cookie.setHttpOnly(false); // 如果不需要通过JS访问可以设置为true
//        cookie.setPath("/");
//        response.addCookie(cookie);
//        response.sendRedirect("/EventSpace");
//    }
//
//}