package com.cmsapp.backend.service;

import com.cmsapp.backend.dto.request.ContentCreateRequest;
import com.cmsapp.backend.dto.request.ContentUpdateRequest;
import com.cmsapp.backend.dto.response.ContentResponse;

import java.time.LocalDateTime;
import java.util.List;


// ne yapacağımızı tanımlıyoruz

public interface ContentService {

    List<ContentResponse> getAllContents();
    ContentResponse getContentById(Long id);
    ContentResponse createContent(ContentCreateRequest request);
    ContentResponse updateContent(Long id, ContentUpdateRequest request);
    ContentResponse createContentFromOmdb(String movieTitle);
    void deleteContent(Long id);

    List<ContentResponse> getContentsByRating(String rating);
    List<ContentResponse> getContentsByRatingGreaterThan(String rating);
    List<ContentResponse> getContentsByDirector(String director);
    // Sıralı listeler
    List<ContentResponse> getContentsOrderByRating();

    List<ContentResponse> getContentsByMetadataTitle(String title);
}
