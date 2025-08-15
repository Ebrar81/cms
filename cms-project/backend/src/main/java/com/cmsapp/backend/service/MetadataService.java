package com.cmsapp.backend.service;

import com.cmsapp.backend.dto.request.MetadataCreateRequest;
import com.cmsapp.backend.dto.response.MetadataResponse;

import java.util.List;

public interface MetadataService {


    List<MetadataResponse> getAllMetadata();
    MetadataResponse getMetadataById(Long id);
    MetadataResponse createMetadata(MetadataCreateRequest request);
    MetadataResponse updateMetadata(Long id, MetadataCreateRequest request); // Şimdilik CREATE request kullanıyoruz
    void deleteMetadata(Long id);


    List<MetadataResponse> searchByTitle(String title);
    List<MetadataResponse> getMetadataByYear(Integer year);
    List<MetadataResponse> getMetadataByYearGreaterThan(Integer year);

    List<MetadataResponse> searchInTitleOrPlot(String keyword);
}
