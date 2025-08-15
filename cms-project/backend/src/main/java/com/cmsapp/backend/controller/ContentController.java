package com.cmsapp.backend.controller;

import com.cmsapp.backend.dto.request.ContentCreateRequest;
import com.cmsapp.backend.dto.request.ContentUpdateRequest;
import com.cmsapp.backend.dto.response.ApiResponse;
import com.cmsapp.backend.dto.response.ContentResponse;
import com.cmsapp.backend.model.Content;
import com.cmsapp.backend.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

// istekleri alacak ve servis'e yönlendirecek.

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;


    // Tüm içerikleri listele - RESPONSE DTO döndürür
    @GetMapping
    public ResponseEntity<List<ContentResponse>> getAllContent() {
        List<ContentResponse> contents = contentService.getAllContents();
        return ResponseEntity.ok(contents);
    }

    // Belirli ID ile içerik getir - RESPONSE DTO döndürür
    @GetMapping("/{id}")
    public ResponseEntity<ContentResponse> getContentById(@PathVariable Long id) {
        ContentResponse content = contentService.getContentById(id);
        return ResponseEntity.ok(content);
    }

    // Yeni içerik ekle - REQUEST DTO alır, RESPONSE DTO döndürür
    @PostMapping
    public ResponseEntity<ContentResponse> createContent(@RequestBody ContentCreateRequest request) {
        ContentResponse createdContent = contentService.createContent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContent);
    }

    // İçerik güncelle - REQUEST DTO alır, RESPONSE DTO döndürür
    @PutMapping("/{id}")
    public ResponseEntity<ContentResponse> updateContent(
            @PathVariable Long id,
            @RequestBody ContentUpdateRequest request) {
        ContentResponse updatedContent = contentService.updateContent(id, request);
        return ResponseEntity.ok(updatedContent);
    }

    // İçerik sil - Sadece success message döndürür
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }


    // ===== SEARCH ENDPOINTS =====

    // Rating'e göre arama
    @GetMapping("/search/rating/{rating}")
    public ResponseEntity<List<ContentResponse>> getContentsByRating(@PathVariable String rating) {
        List<ContentResponse> contents = contentService.getContentsByRating(rating);
        return ResponseEntity.ok(contents);
    }

    // Rating'den büyük olanları bulma
    @GetMapping("/search/rating-greater-than/{rating}")
    public ResponseEntity<List<ContentResponse>> getContentsByRatingGreaterThan(@PathVariable String rating) {
        List<ContentResponse> contents = contentService.getContentsByRatingGreaterThan(rating);
        return ResponseEntity.ok(contents);
    }

    // Director'a göre arama
    @GetMapping("/search/director/{director}")
    public ResponseEntity<List<ContentResponse>> getContentsByDirector(@PathVariable String director) {
        List<ContentResponse> contents = contentService.getContentsByDirector(director);
        return ResponseEntity.ok(contents);
    }


    // Sıralı listeler
    @GetMapping("/sorted/by-rating")
    public ResponseEntity<List<ContentResponse>> getContentsOrderByRating() {
        List<ContentResponse> contents = contentService.getContentsOrderByRating();
        return ResponseEntity.ok(contents);
    }

    // @Query ile JOIN sorgusu
    @GetMapping("/search/by-movie-title/{title}")
    public ResponseEntity<List<ContentResponse>> getContentsByMetadataTitle(@PathVariable String title) {
        List<ContentResponse> contents = contentService.getContentsByMetadataTitle(title);
        return ResponseEntity.ok(contents);
    }

    @PostMapping("/from-omdb")
    public ResponseEntity<ContentResponse> createContentFromOmdb(@RequestParam String title) {
        ContentResponse createdContent = contentService.createContentFromOmdb(title);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContent);
    }
}
