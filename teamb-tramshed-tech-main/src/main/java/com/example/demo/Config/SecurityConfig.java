//package com.example.demo.Config;
//
//
//
//
//import com.example.demo.Controller.CustomAuthenticationSuccessHandler;
//import com.example.demo.Service.CustomUserDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//
//    @Autowired
//    private CustomUserDetailsService customUserDetailsService;
//    @Autowired
//    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .userDetailsService(customUserDetailsService)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/admin","/events/pp","/clients/login",
//                                "/EventSpace", "/Introduction", "/clients/create","/events/{eventId}",
//                                "events/{eventId}/cancel","events/{eventId}/details","/eventForm","/submit",
//                                "/Registration","/EditProfile","/venues/{venueId}",
//        "/Login", "/oauth2/authorization/google").permitAll()
//                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
//                        .anyRequest().authenticated())
//                .formLogin(form -> form
//                        .loginPage("/Login")  // 指定显示登录表单的 URL
//                        .loginProcessingUrl("/clients/login")  // 指定处理登录请求的 URL
//                        .defaultSuccessUrl("/EventSpace", true)
//                        .permitAll())
//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/Login")
//                        .successHandler(customAuthenticationSuccessHandler))
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/Introduction")
//                        .permitAll())
//                .csrf(csrf -> csrf.disable());
//
//        return http.build();
//    }
//}
//
//
//
//
//
//
//
//
//
//
