package com.cmsapp.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentCreateRequest {
    private String rating;
    private String director;
    private String actors;
    private Long metadataId;       // Hangi metadata kullanılacak
    private List<Long> castIds;    // Hangi cast'ler eklenecek
}
