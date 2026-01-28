package com.eyesecurity.ingestion_service.service;

import com.eyesecurity.ingestion_service.model.EnrichmentRequest;
import com.eyesecurity.ingestion_service.model.EnrichmentResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class EnrichmentService {

    private static final String ENRICHMENT_URL = "https://api.heyering.com/enrichment";
    private static final String API_AUTH = "eye-am-hiring";
    private final RestTemplate restTemplate;

    public EnrichmentService() {
        this.restTemplate = new RestTemplate();
    }

    public EnrichmentResponse enrich(EnrichmentRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", API_AUTH);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EnrichmentRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<EnrichmentResponse> response = restTemplate
                    .postForEntity(ENRICHMENT_URL, entity, EnrichmentResponse.class);
            return response.getBody();
        } catch (Exception e) {
            // handle intermittent errors
            System.err.println("Enrichment failed for record id " + request.getId() + ": " + e.getMessage());
            return null; // todo intermittent issues
        }
    }
}