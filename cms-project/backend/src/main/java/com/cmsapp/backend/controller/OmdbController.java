package com.cmsapp.backend.controller;

import com.cmsapp.backend.dto.response.ApiResponse;
import com.cmsapp.backend.dto.response.OmdbResponse;
import com.cmsapp.backend.service.OmdbService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/omdb")
@RequiredArgsConstructor
public class OmdbController {

    private final OmdbService omdbService;


    @GetMapping("/search")
    public ResponseEntity<OmdbResponse> searchMovie(@RequestParam String title) {

        OmdbResponse movie = omdbService.getMovieInfo(title);

        if ("False".equals(movie.getResponse())) {
            // ✅ HTTP 404 status ile hata döndür
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Film bulunamadı: " + movie.getError()
            );
        }

        // ✅ Direkt OmdbResponse döndür
        return ResponseEntity.ok(movie);
    }
}
