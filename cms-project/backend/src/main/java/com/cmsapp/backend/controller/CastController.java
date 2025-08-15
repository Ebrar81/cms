package com.cmsapp.backend.controller;

import com.cmsapp.backend.dto.request.CastCreateRequest;
import com.cmsapp.backend.dto.response.ApiResponse;
import com.cmsapp.backend.dto.response.CastResponse;
import com.cmsapp.backend.model.Cast;
import com.cmsapp.backend.service.CastService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/casts")
@RequiredArgsConstructor
public class CastController {

    private final CastService castService;


    @GetMapping
    public ResponseEntity<List<CastResponse>> getAllCasts() {
        List<CastResponse> casts = castService.getAllCasts();
        return ResponseEntity.ok(casts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CastResponse> getCastById(@PathVariable Long id) {
        CastResponse cast = castService.getCastById(id);
        return ResponseEntity.ok(cast);
    }

    @PostMapping
    public ResponseEntity<CastResponse> createCast(@RequestBody CastCreateRequest request) {
        CastResponse createdCast = castService.createCast(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCast);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CastResponse> updateCast(
            @PathVariable Long id,
            @RequestBody CastCreateRequest request) {
        CastResponse updatedCast = castService.updateCast(id, request);
        return ResponseEntity.ok(updatedCast);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCast(@PathVariable Long id) {
        castService.deleteCast(id);
        return ResponseEntity.noContent().build();
    }

    // Search endpoints
    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<CastResponse>> searchByName(@PathVariable String name) {
        List<CastResponse> casts = castService.searchByName(name);
        return ResponseEntity.ok(casts);
    }

    @GetMapping("/sorted/by-name")
    public ResponseEntity<List<CastResponse>> getCastsOrderByName() {
        List<CastResponse> casts = castService.getCastsOrderByName();
        return ResponseEntity.ok(casts);
    }

    @GetMapping("/search/by-rating/{minRating}")
    public ResponseEntity<List<CastResponse>> getCastsByContentRating(@PathVariable String minRating) {
        List<CastResponse> casts = castService.getCastsByContentRating(minRating);
        return ResponseEntity.ok(casts);
    }
}