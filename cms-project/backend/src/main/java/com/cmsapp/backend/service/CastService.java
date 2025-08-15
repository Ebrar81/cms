package com.cmsapp.backend.service;

import com.cmsapp.backend.dto.request.CastCreateRequest;
import com.cmsapp.backend.dto.response.CastResponse;

import java.util.List;

public interface CastService {


    List<CastResponse> getAllCasts();
    CastResponse getCastById(Long id);
    CastResponse createCast(CastCreateRequest request);
    CastResponse updateCast(Long id, CastCreateRequest request); // Şimdilik CREATE request kullanıyoruz
    void deleteCast(Long id);

    List<CastResponse> searchByName(String name);
    List<CastResponse> getCastsOrderByName();

    List<CastResponse> getCastsByContentRating(String minRating);

}
