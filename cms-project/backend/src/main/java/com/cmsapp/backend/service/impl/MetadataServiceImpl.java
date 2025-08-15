package com.cmsapp.backend.service.impl;

import com.cmsapp.backend.dto.request.MetadataCreateRequest;
import com.cmsapp.backend.dto.response.MetadataResponse;
import com.cmsapp.backend.mapper.ContentMapper;
import com.cmsapp.backend.model.Metadata;
import com.cmsapp.backend.repository.MetadataRepository;
import com.cmsapp.backend.service.MetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetadataServiceImpl implements MetadataService {
    private final MetadataRepository metadataRepository;
    private final ContentMapper contentMapper;


    @Override
    public List<MetadataResponse> getAllMetadata() {
        List<Metadata> metadataList = metadataRepository.findAll();
        return contentMapper.toMetadataResponseList(metadataList);
    }

    @Override
    public MetadataResponse getMetadataById(Long id) {
        Metadata metadata = metadataRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "ID " + id + " ile metadata bulunamadı"
                ));
        return contentMapper.toMetadataResponse(metadata);
    }

    @Override
    public MetadataResponse createMetadata(MetadataCreateRequest request) {
        // Request DTO'yu Entity'ye çevir
        Metadata metadata = contentMapper.toMetadataEntity(request);

        // Database'e kaydet
        Metadata savedMetadata = metadataRepository.save(metadata);

        // Response DTO'ya çevir ve döndür
        return contentMapper.toMetadataResponse(savedMetadata);
    }

    @Override
    public MetadataResponse updateMetadata(Long id, MetadataCreateRequest request) {
        // Mevcut metadata'yı bul
        Metadata existingMetadata = metadataRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Güncellenecek metadata bulunamadı: " + id
                ));

        // Alanları güncelle (null kontrolü ile)
        if (request.getTitle() != null) {
            existingMetadata.setTitle(request.getTitle());
        }
        if (request.getPlot() != null) {
            existingMetadata.setPlot(request.getPlot());
        }
        if (request.getPoster() != null) {
            existingMetadata.setPoster(request.getPoster());
        }
        if (request.getYear() != null) {
            existingMetadata.setYear(request.getYear());
        }
        if (request.getLanguage() != null) {
            existingMetadata.setLanguage(request.getLanguage());
        }
        if (request.getCountry() != null) {
            existingMetadata.setCountry(request.getCountry());
        }

        // Kaydet ve Response DTO'ya çevir
        Metadata updatedMetadata = metadataRepository.save(existingMetadata);
        return contentMapper.toMetadataResponse(updatedMetadata);
    }

    @Override
    public void deleteMetadata(Long id) {
        if (!metadataRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Silinecek metadata bulunamadı: " + id
            );
        }
        metadataRepository.deleteById(id);
    }


    @Override
    public List<MetadataResponse> searchByTitle(String title) {
        List<Metadata> metadataList = metadataRepository.findByTitleContainingIgnoreCase(title);
        return contentMapper.toMetadataResponseList(metadataList);
    }

    @Override
    public List<MetadataResponse> getMetadataByYear(Integer year) {
        List<Metadata> metadataList = metadataRepository.findByYear(year);
        return contentMapper.toMetadataResponseList(metadataList);
    }

    @Override
    public List<MetadataResponse> getMetadataByYearGreaterThan(Integer year) {
        List<Metadata> metadataList = metadataRepository.findByYearGreaterThan(year);
        return contentMapper.toMetadataResponseList(metadataList);
    }

    @Override
    public List<MetadataResponse> searchInTitleOrPlot(String keyword) {
        List<Metadata> metadataList = metadataRepository.searchInTitleOrPlot(keyword);
        return contentMapper.toMetadataResponseList(metadataList);
    }

}
