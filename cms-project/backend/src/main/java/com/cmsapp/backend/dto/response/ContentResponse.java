package com.cmsapp.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentResponse {
    private Long id;
    private String rating;
    private String director;
    private String actors;
    private LocalDateTime createdAt;

    // İlişkili veriler nested DTO olarak
    private MetadataResponse metadata;
    private List<CastResponse> casts;
}
