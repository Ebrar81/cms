package com.cmsapp.backend.mapper;

import com.cmsapp.backend.dto.request.*;
import com.cmsapp.backend.dto.response.CastResponse;
import com.cmsapp.backend.dto.response.ContentResponse;
import com.cmsapp.backend.dto.response.MetadataResponse;
import com.cmsapp.backend.model.Cast;
import com.cmsapp.backend.model.Content;
import com.cmsapp.backend.model.Metadata;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class ContentMapper {
    // REQUEST DTO → ENTITY
    public Content toEntity(ContentCreateRequest request) {
        Content content = new Content();
        content.setRating(request.getRating());
        content.setDirector(request.getDirector());
        content.setActors(request.getActors());
        // metadata ve casts service'te set edilecek
        return content;
    }

    // ENTITY → RESPONSE DTO
    public ContentResponse toResponse(Content content) {
        ContentResponse response = new ContentResponse();
        response.setId(content.getId());
        response.setRating(content.getRating());
        response.setDirector(content.getDirector());
        response.setActors(content.getActors());
        response.setCreatedAt(content.getCreatedAt());

        // Metadata çevirme
        if (content.getMetadata() != null) {
            response.setMetadata(toMetadataResponse(content.getMetadata()));
        }

        // Cast listesi çevirme
        if (content.getCasts() != null && !content.getCasts().isEmpty()) {
            List<CastResponse> castResponses = content.getCasts().stream()
                    .map(this::toCastResponse)
                    .collect(Collectors.toList());
            response.setCasts(castResponses);
        }

        return response;
    }

    // Metadata Entity → Response DTO
    public MetadataResponse toMetadataResponse(Metadata metadata) {
        MetadataResponse response = new MetadataResponse();
        response.setId(metadata.getId());
        response.setTitle(metadata.getTitle());
        response.setPlot(metadata.getPlot());
        response.setPoster(metadata.getPoster());
        response.setYear(metadata.getYear());
        response.setLanguage(metadata.getLanguage());
        response.setCountry(metadata.getCountry());
        return response;
    }

    // Cast Entity → Response DTO
    public CastResponse toCastResponse(Cast cast) {
        CastResponse response = new CastResponse();
        response.setId(cast.getId());
        response.setName(cast.getName());
        response.setPoster(cast.getPoster());
        return response;
    }

    // Metadata Request → Entity
    public Metadata toMetadataEntity(MetadataCreateRequest request) {
        Metadata metadata = new Metadata();
        metadata.setTitle(request.getTitle());
        metadata.setPlot(request.getPlot());
        metadata.setPoster(request.getPoster());
        metadata.setYear(request.getYear());
        metadata.setLanguage(request.getLanguage());
        metadata.setCountry(request.getCountry());
        return metadata;
    }

    // Cast Request → Entity
    public Cast toCastEntity(CastCreateRequest request) {
        Cast cast = new Cast();
        cast.setName(request.getName());
        cast.setPoster(request.getPoster());
        return cast;
    }

    // Liste dönüşümleri
    public List<ContentResponse> toContentResponseList(List<Content> contents) {
        return contents.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<MetadataResponse> toMetadataResponseList(List<Metadata> metadataList) {
        return metadataList.stream()
                .map(this::toMetadataResponse)
                .collect(Collectors.toList());
    }

    public List<CastResponse> toCastResponseList(List<Cast> casts) {
        return casts.stream()
                .map(this::toCastResponse)
                .collect(Collectors.toList());
    }

    // Metadata Update mapping
    public void updateMetadataFromRequest(Metadata existingMetadata, MetadataUpdateRequest request) {
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
    }

    // Cast Update mapping
    public void updateCastFromRequest(Cast existingCast, CastUpdateRequest request) {
        if (request.getName() != null) {
            existingCast.setName(request.getName());
        }
        if (request.getPoster() != null) {
            existingCast.setPoster(request.getPoster());
        }
    }

    // Content Update mapping (zaten var ama tam hali)
    public void updateContentFromRequest(Content existingContent, ContentUpdateRequest request) {
        if (request.getRating() != null) {
            existingContent.setRating(request.getRating());
        }
        if (request.getDirector() != null) {
            existingContent.setDirector(request.getDirector());
        }
        if (request.getActors() != null) {
            existingContent.setActors(request.getActors());
        }
        // metadata ve cast güncellemesi service'te yapılacak
    }
}
