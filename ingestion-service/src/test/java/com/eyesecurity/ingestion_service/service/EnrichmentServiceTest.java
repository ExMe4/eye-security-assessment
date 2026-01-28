package com.eyesecurity.ingestion_service.service;

import com.eyesecurity.ingestion_service.model.EnrichmentRequest;
import com.eyesecurity.ingestion_service.model.EnrichmentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class EnrichmentServiceTest {

    private EnrichmentService enrichmentService;
    private RestTemplate restTemplateMock;

    @BeforeEach
    void setUp() {
        restTemplateMock = mock(RestTemplate.class);
        enrichmentService = new EnrichmentService() {
            {
                this.restTemplate = restTemplateMock;
            }
        };
    }

    @Test
    void givenValidRequest_whenEnrich_thenReturnsEnrichmentResponse() {
        // Given
        EnrichmentRequest request = new EnrichmentRequest();
        request.setId(123L);

        EnrichmentResponse mockResponse = new EnrichmentResponse();
        mockResponse.setAsn("ASN123");
        mockResponse.setCategory("phishing");
        mockResponse.setCorrelationId(999L);

        when(restTemplateMock.postForEntity(anyString(), any(HttpEntity.class), eq(EnrichmentResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // When
        EnrichmentResponse response = enrichmentService.enrich(request);

        // Then
        assertNotNull(response);
        assertEquals("ASN123", response.getAsn());
        assertEquals("phishing", response.getCategory());
        assertEquals(999L, response.getCorrelationId());
    }

    @Test
    void givenFailedRequests_whenEnrich_thenRetriesAndReturnsNull() {
        // Given
        EnrichmentRequest request = new EnrichmentRequest();
        request.setId(123L);

        when(restTemplateMock.postForEntity(anyString(), any(HttpEntity.class), eq(EnrichmentResponse.class)))
                .thenThrow(new RuntimeException("Service error"));

        // When
        EnrichmentResponse response = enrichmentService.enrich(request);

        // Then
        assertNull(response);
        verify(restTemplateMock, atLeast(3)).postForEntity(anyString(), any(HttpEntity.class), eq(EnrichmentResponse.class));
    }
}