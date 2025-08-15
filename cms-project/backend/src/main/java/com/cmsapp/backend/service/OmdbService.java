package com.cmsapp.backend.service;

import com.cmsapp.backend.dto.response.OmdbResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class OmdbService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${omdb.api.key}")
    private String apiKey;

    @Value("${omdb.api.url}")
    private String apiUrl;

    public OmdbResponse getMovieInfo(String title) {
        try {
            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
            String url = String.format("%s?apikey=%s&t=%s", apiUrl, apiKey, encodedTitle);

            System.out.println("🔍 OMDB API Request URL: " + url);

            OmdbResponse response = restTemplate.getForObject(url, OmdbResponse.class);

            if (response == null) {
                System.err.println("❌ OMDB returned null response");
                response = createErrorResponse("No response from OMDB API");
            } else {
                System.out.println("✅ OMDB Response received: " + response.getResponse());
                if ("False".equals(response.getResponse())) {
                    System.out.println("❌ OMDB Error: " + response.getError());
                }
            }

            return response;

        } catch (Exception e) {
            System.err.println("❌ OMDB API Exception: " + e.getMessage());
            e.printStackTrace();
            return createErrorResponse("Connection failed: " + e.getMessage());
        }
    }

    private OmdbResponse createErrorResponse(String errorMessage) {
        OmdbResponse errorResponse = new OmdbResponse();
        errorResponse.setResponse("False");
        errorResponse.setError(errorMessage);
        return errorResponse;
    }
}