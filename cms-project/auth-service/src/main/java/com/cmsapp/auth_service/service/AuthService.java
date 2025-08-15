package com.cmsapp.auth_service.service;

import com.cmsapp.auth_service.dto.AuthResponse;
import com.cmsapp.auth_service.dto.LoginRequest;
import com.cmsapp.auth_service.dto.RegisterRequest;
import com.cmsapp.auth_service.entity.Role;
import com.cmsapp.auth_service.entity.User;
import com.cmsapp.auth_service.repository.UserRepository;
import com.cmsapp.auth_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repo; //kullanıcıyı db'de aramak-kaydetmek için
    private final PasswordEncoder passwordEncoder; //şifreyi hashlemek (tek yönlü)
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest req) {
        if (repo.existsByUsername(req.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setEmail(req.getEmail());

        // Role alanı boşsa USER, doluysa Enum’a çevir
        // req.getRole().isBlank()- rol boş string ya da sadece boşluklardan oluşuyorsa
        // trim() → Baştaki ve sondaki boşlukları siler.
        String roleStr = (req.getRole() == null || req.getRole().isBlank())
                ? "USER"
                : req.getRole().trim().toUpperCase();
        try {
            u.setRole(Role.valueOf(roleStr));
        } catch (IllegalArgumentException e) {
            u.setRole(Role.USER); // Geçersiz değer gelirse USER yap
        }

        repo.save(u);

        String token = jwtUtil.generateToken(u.getUsername(), u.getRole().name());
        return new AuthResponse(token, u.getUsername(), u.getRole().name());
    }

    public AuthResponse login(LoginRequest req) {
        User u = repo.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("Bad credentials"));

        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new RuntimeException("Bad credentials");
        }

        String token = jwtUtil.generateToken(u.getUsername(), u.getRole().name());
        return new AuthResponse(token, u.getUsername(), u.getRole().name());
    }
}
