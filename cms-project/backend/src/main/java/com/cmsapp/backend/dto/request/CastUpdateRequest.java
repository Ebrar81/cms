package com.cmsapp.backend.dto.request;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CastUpdateRequest {
    private String name;
    private String poster;
}
