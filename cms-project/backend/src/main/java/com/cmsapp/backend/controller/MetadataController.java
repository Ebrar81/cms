package com.cmsapp.backend.controller;

import com.cmsapp.backend.dto.request.MetadataCreateRequest;
import com.cmsapp.backend.dto.response.ApiResponse;
import com.cmsapp.backend.dto.response.MetadataResponse;
import com.cmsapp.backend.service.MetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metadata")
@RequiredArgsConstructor
public class MetadataController {

    private final MetadataService metadataService;

    @GetMapping
    public ResponseEntity<List<MetadataResponse>> getAllMetadata() {
        List<MetadataResponse> metadata = metadataService.getAllMetadata();
        return ResponseEntity.ok(metadata);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetadataResponse> getMetadataById(@PathVariable Long id) {
        MetadataResponse metadata = metadataService.getMetadataById(id);
        return ResponseEntity.ok(metadata);
    }

    @PostMapping
    public ResponseEntity<MetadataResponse> createMetadata(@RequestBody MetadataCreateRequest request) {
        MetadataResponse createdMetadata = metadataService.createMetadata(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMetadata);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetadataResponse> updateMetadata(
            @PathVariable Long id,
            @RequestBody MetadataCreateRequest request) {
        MetadataResponse updatedMetadata = metadataService.updateMetadata(id, request);
        return ResponseEntity.ok(updatedMetadata);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetadata(@PathVariable Long id) {
        metadataService.deleteMetadata(id);
        return ResponseEntity.noContent().build();
    }

    // Search endpoints
    @GetMapping("/search/title/{title}")
    public ResponseEntity<List<MetadataResponse>> searchByTitle(@PathVariable String title) {
        List<MetadataResponse> metadata = metadataService.searchByTitle(title);
        return ResponseEntity.ok(metadata);
    }

    @GetMapping("/search/year/{year}")
    public ResponseEntity<List<MetadataResponse>> getMetadataByYear(@PathVariable Integer year) {
        List<MetadataResponse> metadata = metadataService.getMetadataByYear(year);
        return ResponseEntity.ok(metadata);
    }

    @GetMapping("/search/year-after/{year}")
    public ResponseEntity<List<MetadataResponse>> getMetadataByYearGreaterThan(@PathVariable Integer year) {
        List<MetadataResponse> metadata = metadataService.getMetadataByYearGreaterThan(year);
        return ResponseEntity.ok(metadata);
    }

    @GetMapping("/search/keyword/{keyword}")
    public ResponseEntity<List<MetadataResponse>> searchInTitleOrPlot(@PathVariable String keyword) {
        List<MetadataResponse> metadata = metadataService.searchInTitleOrPlot(keyword);
        return ResponseEntity.ok(metadata);
    }
}