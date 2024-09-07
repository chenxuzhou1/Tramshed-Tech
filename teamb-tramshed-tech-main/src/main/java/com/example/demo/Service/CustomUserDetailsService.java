//package com.example.demo.Service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import com.example.demo.Service.ClientService;
//import com.fasterxml.jackson.databind.JsonNode;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private ClientService clientService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        try {
//            JsonNode userNode = clientService.getClientDetails(username);
//            if (userNode != null && userNode.size() > 0) {
//                String password = userNode.path("properties").path("Password").path("rich_text").path(0).path("plain_text").asText();
//                return User.withUsername(username)
//                        .password("{noop}" + password) // 指示未编码密码
//                        .authorities("USER")
//                        .build();
//            } else {
//                throw new UsernameNotFoundException("User not found with email: " + username);
//            }
//        } catch (Exception e) {
//            throw new UsernameNotFoundException("Error loading user by email: " + username, e);
//        }
//    }
//}
//
//
