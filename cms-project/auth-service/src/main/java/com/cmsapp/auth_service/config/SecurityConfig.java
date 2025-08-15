//package com.cmsapp.auth_service.config;
//
//import com.cmsapp.auth_service.security.JwtAuthFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.*;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.*;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.*;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtAuthFilter jwtAuthFilter; //doğrulama filtresi
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(cors -> cors.disable())
//
//                // CSRF-Siteler Arası İstek Sahteciliği
//                .csrf(csrf -> csrf.disable())
//                // stateless → Sunucu, kullanıcı hakkında oturum bilgisi tutmaz. Her istekte JWT kontrol edilir.
//                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                // herkese açık endpointler jwt gerekmez, bunlar harici gerekir
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/auth/login", "/api/auth/register", "/actuator/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                // JWT doğrulama filtresini login filtresinden önce çalıştırır.
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    // kullanıcı girişinde kimlik doğrulaması yapar
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
//        return cfg.getAuthenticationManager();
//    }
//}
