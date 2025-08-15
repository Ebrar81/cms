package com.cmsapp.auth_service.controller;

import com.cmsapp.auth_service.dto.*;
import com.cmsapp.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController //http isteklerine cevap verir ve sonuçları json döndürür
@RequestMapping("/api/auth")
@RequiredArgsConstructor //final değişkenleri otomatik constructor
public class AuthController {

    //service devrediyoruz. controller sadece isteği alır
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));  //HTTP 200 OK dönermiş
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    } // kullanıcı doğrulanır ve başarılıysa JWT token döner

    // korunan örnek endpoint (token gerektirir)
    @GetMapping("/me")
    public ResponseEntity<?> me() {
        // SecurityContextHolder- Spring Security, doğrulanan kullanıcı bilgisini burada tutar
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(auth.getName());
    }
}
