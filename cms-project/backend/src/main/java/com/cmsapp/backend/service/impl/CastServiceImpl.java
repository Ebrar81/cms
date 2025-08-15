package com.cmsapp.backend.service.impl;

import com.cmsapp.backend.dto.request.CastCreateRequest;
import com.cmsapp.backend.dto.response.CastResponse;
import com.cmsapp.backend.mapper.ContentMapper;
import com.cmsapp.backend.model.Cast;
import com.cmsapp.backend.repository.CastRepository;
import com.cmsapp.backend.service.CastService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CastServiceImpl implements CastService {
    private final CastRepository castRepository;
    private final ContentMapper contentMapper;


    @Override
    public List<CastResponse> getAllCasts() {
        List<Cast> casts = castRepository.findAll();
        return contentMapper.toCastResponseList(casts);
    }

    @Override
    public CastResponse getCastById(Long id) {
        Cast cast = castRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "ID " + id + " ile cast bulunamadı"
                ));
        return contentMapper.toCastResponse(cast);
    }

    @Override
    public CastResponse createCast(CastCreateRequest request) {
        // Request DTO'yu Entity'ye çevir
        Cast cast = contentMapper.toCastEntity(request);

        // Database'e kaydet
        Cast savedCast = castRepository.save(cast);

        // Response DTO'ya çevir ve döndür
        return contentMapper.toCastResponse(savedCast);
    }

    @Override
    public CastResponse updateCast(Long id, CastCreateRequest request) {
        // Mevcut cast'i bul
        Cast existingCast = castRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Güncellenecek cast bulunamadı: " + id
                ));

        // Alanları güncelle (null kontrolü ile)
        if (request.getName() != null) {
            existingCast.setName(request.getName());
        }
        if (request.getPoster() != null) {
            existingCast.setPoster(request.getPoster());
        }

        // Kaydet ve Response DTO'ya çevir
        Cast updatedCast = castRepository.save(existingCast);
        return contentMapper.toCastResponse(updatedCast);
    }

    @Override
    public void deleteCast(Long id) {
        if (!castRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Silinecek cast bulunamadı: " + id
            );
        }
        castRepository.deleteById(id);
    }

    @Override
    public List<CastResponse> searchByName(String name) {
        List<Cast> casts = castRepository.findByNameContainingIgnoreCase(name);
        return contentMapper.toCastResponseList(casts);
    }

    @Override
    public List<CastResponse> getCastsOrderByName() {
        List<Cast> casts = castRepository.findByOrderByNameAsc();
        return contentMapper.toCastResponseList(casts);
    }

    @Override
    public List<CastResponse> getCastsByContentRating(String minRating) {
        List<Cast> casts = castRepository.findCastsByContentRating(minRating);
        return contentMapper.toCastResponseList(casts);
    }

}
