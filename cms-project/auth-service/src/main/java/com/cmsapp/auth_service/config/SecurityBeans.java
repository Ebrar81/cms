package com.cmsapp.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeans {
    @Bean //Bu methodun dönüş değerini Spring uygulama boyunca kullanabilir.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
