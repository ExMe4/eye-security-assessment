package com.eyesecurity.ingestion_service.service;

import com.eyesecurity.ingestion_service.model.AnalyticsRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;

@Service
public class AnalyticsService {

    private static final String ANALYTICS_URL = "https://api.heyering.com/analytics";
    private static final String API_AUTH = "eye-am-hiring";
    private static final int BATCH_SIZE = 20;

    RestTemplate restTemplate;

    public AnalyticsService() {
        this.restTemplate = new RestTemplate();
    }

    public void sendRecords(List<AnalyticsRecord> records) throws InterruptedException {
        int start = 0;
        while (start < records.size()) {
            int end = Math.min(start + BATCH_SIZE, records.size());
            List<AnalyticsRecord> batch = records.subList(start, end);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", API_AUTH);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<List<AnalyticsRecord>> entity = new HttpEntity<>(batch, headers);

            try {
                ResponseEntity<String> response = restTemplate.postForEntity(ANALYTICS_URL, entity, String.class);
                System.out.println("Analytics batch [" + start + "-" + end + "] response: " + response.getStatusCode());
                if (response.getStatusCode().value() == 429) {
                    System.err.println("Rate limit hit, waiting 10s...");
                    Thread.sleep(10_000);
                    continue;
                }
            } catch (Exception e) {
                System.err.println("Analytics ingestion failed: " + e.getMessage());
            }

            start += BATCH_SIZE;

            // Wait 10 seconds to respect rate limit
            Thread.sleep(10_000);
        }
    }
}