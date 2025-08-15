package com.cmsapp.backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetadataCreateRequest {
    private String title;
    private String plot;
    private String poster;
    private Integer year;
    private String language;
    private String country;
}
