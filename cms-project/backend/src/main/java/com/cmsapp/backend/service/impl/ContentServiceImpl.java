package com.cmsapp.backend.service.impl;


import com.cmsapp.backend.dto.request.ContentCreateRequest;
import com.cmsapp.backend.dto.request.ContentUpdateRequest;
import com.cmsapp.backend.dto.response.ContentResponse;
import com.cmsapp.backend.dto.response.OmdbResponse;
import com.cmsapp.backend.mapper.ContentMapper;
import com.cmsapp.backend.model.Cast;
import com.cmsapp.backend.model.Content;
import com.cmsapp.backend.model.Metadata;
import com.cmsapp.backend.repository.CastRepository;
import com.cmsapp.backend.repository.ContentRepository;
import com.cmsapp.backend.repository.MetadataRepository;
import com.cmsapp.backend.service.ContentService;
import com.cmsapp.backend.service.OmdbService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {
    // Dependency'ler
    private final ContentRepository contentRepository;
    private final MetadataRepository metadataRepository;
    private final CastRepository castRepository;
    private final ContentMapper contentMapper;
    private final OmdbService omdbService;



    @Override
    public List<ContentResponse> getAllContents() {
        List<Content> contents = contentRepository.findAll();
        return contentMapper.toContentResponseList(contents);
    }


    @Override
    public ContentResponse getContentById(Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "ID " + id + " ile içerik bulunamadı"
                ));
        return contentMapper.toResponse(content);
    }


    @Override
    @Transactional
    public ContentResponse createContent(ContentCreateRequest request) {

        // Request DTO'yu Content entity'ye çevir
        Content content = contentMapper.toEntity(request);

        // Metadata'yı bul ve bağla
        Metadata metadata = metadataRepository.findById(request.getMetadataId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Metadata bulunamadı: " + request.getMetadataId()
                ));
        content.setMetadata(metadata);

        // Cast'leri bul ve bağla (eğer gönderilmişse)
        if (request.getCastIds() != null && !request.getCastIds().isEmpty()) {
            List<Cast> casts = castRepository.findAllById(request.getCastIds());

            if (casts.size() != request.getCastIds().size()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Bazı cast üyeleri bulunamadı"
                );
            }
            content.setCasts(casts);
        }

        // Oluşturma tarihini set et
        content.setCreatedAt(LocalDateTime.now());

        // Database'e kaydet
        Content savedContent = contentRepository.save(content);

        // Response DTO'ya çevir ve döndür
        return contentMapper.toResponse(savedContent);
    }


    @Override
    @Transactional
    public ContentResponse updateContent(Long id, ContentUpdateRequest request) {

        // Mevcut content'i bul
        Content existingContent = contentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Güncellenecek içerik bulunamadı: " + id
                ));

        // Basit alanları güncelle (sadece null olmayan alanlar)
        contentMapper.updateContentFromRequest(existingContent, request);

        if (request.getMetadata() != null && existingContent.getMetadata() != null) {
            // Mevcut ContentMapper'ınızdaki metodu kullanıyoruz
            contentMapper.updateMetadataFromRequest(existingContent.getMetadata(), request.getMetadata());

            // Metadata'yı kaydet
            metadataRepository.save(existingContent.getMetadata());
        }

        // Metadata güncellemesi (eğer yeni metadata ID gönderilmişse)
        if (request.getMetadataId() != null) {
            Metadata metadata = metadataRepository.findById(request.getMetadataId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Metadata bulunamadı: " + request.getMetadataId()
                    ));
            existingContent.setMetadata(metadata);
        }

        // Cast güncellemesi (eğer yeni cast ID'leri gönderilmişse)
        if (request.getCastIds() != null) {
            if (request.getCastIds().isEmpty()) {
                existingContent.setCasts(null);
            } else {
                List<Cast> casts = castRepository.findAllById(request.getCastIds());
                if (casts.size() != request.getCastIds().size()) {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Bazı cast üyeleri bulunamadı"
                    );
                }
                existingContent.setCasts(casts);
            }
        }

        // Güncellenmiş content'i kaydet
        Content updatedContent = contentRepository.save(existingContent);

        // Response DTO'ya çevir ve döndür
        return contentMapper.toResponse(updatedContent);
    }


    @Override
    public void deleteContent(Long id) {
        if (!contentRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Silinecek içerik bulunamadı: " + id
            );
        }
        contentRepository.deleteById(id);
    }

    // ===== SEARCH METHOD IMPLEMENTATION'LARI =====

    @Override
    public List<ContentResponse> getContentsByRating(String rating) {
        List<Content> contents = contentRepository.findByRating(rating);
        return contentMapper.toContentResponseList(contents);
    }

    @Override
    public List<ContentResponse> getContentsByRatingGreaterThan(String rating) {
        List<Content> contents = contentRepository.findByRatingGreaterThan(rating);
        return contentMapper.toContentResponseList(contents);
    }

    @Override
    public List<ContentResponse> getContentsByDirector(String director) {
        List<Content> contents = contentRepository.findByDirectorContainingIgnoreCase(director);
        return contentMapper.toContentResponseList(contents);
    }

    @Override
    public List<ContentResponse> getContentsOrderByRating() {
        List<Content> contents = contentRepository.findByOrderByRatingDesc();
        return contentMapper.toContentResponseList(contents);
    }

    @Override
    public List<ContentResponse> getContentsByMetadataTitle(String title) {
        List<Content> contents = contentRepository.findContentsByMetadataTitle(title);
        return contentMapper.toContentResponseList(contents);
    }

    @Override
    @Transactional
    public ContentResponse createContentFromOmdb(String movieTitle) {
        try {
            // 1. OMDB'den film bilgisini çek
            OmdbResponse omdbData = omdbService.getMovieInfo(movieTitle);

            // 2. OMDB'de film bulunamadıysa hata fırlat
            if ("False".equals(omdbData.getResponse())) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "OMDB'de film bulunamadı: " + omdbData.getError()
                );
            }

            // 3. Metadata oluştur ve database'e kaydet
            Metadata metadata = new Metadata();
            metadata.setTitle(omdbData.getTitle());
            metadata.setPlot(omdbData.getPlot());
            metadata.setPoster(omdbData.getPoster());
            metadata.setYear(parseYear(omdbData.getYear())); // Helper method
            metadata.setLanguage(omdbData.getLanguage());
            metadata.setCountry(omdbData.getCountry());

            Metadata savedMetadata = metadataRepository.save(metadata);

            // 4. Content oluştur ve database'e kaydet
            Content content = new Content();
            content.setRating(omdbData.getImdbRating());
            content.setDirector(omdbData.getDirector());
            content.setActors(omdbData.getActors());
            content.setMetadata(savedMetadata);
            content.setCreatedAt(LocalDateTime.now());

            Content savedContent = contentRepository.save(content);

            // 5. Response DTO'ya çevir ve döndür
            return contentMapper.toResponse(savedContent);

        } catch (ResponseStatusException e) {
            throw e; // Bu hatayı olduğu gibi fırlat
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "OMDB'den içerik oluşturulamadı: " + e.getMessage()
            );
        }
    }

    // Helper method - Year parsing için
    private Integer parseYear(String yearStr) {
        try {
            if (yearStr != null && !yearStr.isEmpty() && !yearStr.equals("N/A")) {
                return Integer.parseInt(yearStr.substring(0, 4));
            }
            return null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
