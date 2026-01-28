package com.eyesecurity.ingestion_service.service;

import com.eyesecurity.ingestion_service.model.AnalyticsRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AnalyticsServiceTest {

    private AnalyticsService analyticsService;
    private RestTemplate restTemplateMock;

    @BeforeEach
    void setUp() {
        restTemplateMock = mock(RestTemplate.class);
        analyticsService = new AnalyticsService() {
            {
                this.restTemplate = restTemplateMock;
            }
        };
    }

    @Test
    void givenRecords_whenSendRecords_thenPostsToAnalytics() throws InterruptedException {
        // Given
        AnalyticsRecord record1 = new AnalyticsRecord();
        record1.setId(1L);
        AnalyticsRecord record2 = new AnalyticsRecord();
        record2.setId(2L);

        List<AnalyticsRecord> records = List.of(record1, record2);

        ResponseEntity<String> successResponse = new ResponseEntity<>("OK", HttpStatus.OK);
        when(restTemplateMock.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(successResponse);

        // When
        analyticsService.sendRecords(records);

        // Then
        verify(restTemplateMock, atLeastOnce())
                .postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void givenRateLimitResponse_whenSendRecords_thenRetriesAfterDelay() throws InterruptedException {
        // Given
        AnalyticsRecord record = new AnalyticsRecord();
        record.setId(1L);

        ResponseEntity<String> rateLimitResponse = new ResponseEntity<>("Too Many Requests", HttpStatus.TOO_MANY_REQUESTS);
        when(restTemplateMock.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(rateLimitResponse)
                .thenReturn(new ResponseEntity<>("OK", HttpStatus.OK));

        List<AnalyticsRecord> records = List.of(record);

        // When
        analyticsService.sendRecords(records);

        // Then
        verify(restTemplateMock, times(2)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }
}