package com.cmsapp.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentUpdateRequest {
    private String rating;
    private String director;
    private String actors;
    private Long metadataId;
    private List<Long> castIds;

    private MetadataUpdateRequest metadata;
}
