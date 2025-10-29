//package com.app.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//
//
////@Configuration
////public class ActuatorSecurityConfig {
////
////    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        return http
////                .csrf(AbstractHttpConfigurer::disable)
////                .authorizeHttpRequests(requests -> requests
//////                        .requestMatchers("/actuator/**").authenticated()
////                                .requestMatchers("/actuator","/actuator/**").hasRole("ACTUATOR_ADMIN") // Only users with ACTUATOR_ADMIN role
////                                .anyRequest().permitAll()
////                )
////                .httpBasic(Customizer.withDefaults())
//////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                // Change session management to STATEFUL
////                .sessionManagement(session -> session
////                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
////                )
////                .build();
////    }
////}
//
//
//@Configuration
//public class ActuatorSecurityConfig {
//
//    // 1️⃣ High priority for /actuator/**
//    @Bean
//    @Order(1)
//    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .securityMatcher("/actuator/**") // Apply only to actuator endpoints
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(requests -> requests
//                        .anyRequest().hasRole("ACTUATOR_ADMIN")
//                )
//                .httpBasic(Customizer.withDefaults())
//                // Enable session for actuator endpoints
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
//                );
//
//        return http.build();
//    }
//
//    // 2️⃣ Default configuration for everything else (stateless)
//    @Bean
//    @Order(2)
//    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(requests -> requests
//                        .anyRequest().permitAll()
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                );
//
//        return http.build();
//    }
//}
