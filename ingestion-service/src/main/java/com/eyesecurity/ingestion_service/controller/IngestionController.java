package com.eyesecurity.ingestion_service.controller;

import com.eyesecurity.ingestion_service.model.SecurityLogRecord;
import com.eyesecurity.ingestion_service.model.EnrichmentRequest;
import com.eyesecurity.ingestion_service.model.EnrichmentResponse;
import com.eyesecurity.ingestion_service.model.AnalyticsRecord;
import com.eyesecurity.ingestion_service.service.EnrichmentService;
import com.eyesecurity.ingestion_service.service.AnalyticsService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ingest")
public class IngestionController {

    @Autowired
    private EnrichmentService enrichmentService;

    @Autowired
    private AnalyticsService analyticsService;

    private static final Set<String> VALID_ENRICHMENT_CATEGORIES = Set.of(
            "contentinjection",
            "drivebycompromise",
            "exploitpublicfacingapplication",
            "externalremoteservices",
            "hardwareadditions",
            "phishing",
            "replicationthroughremovablemedia",
            "supplychaincompromise",
            "trustedrelationship",
            "validaccounts"
    );

    @PostMapping
    public String ingestRecords(@RequestBody List<SecurityLogRecord> records) throws InterruptedException {

        List<AnalyticsRecord> analyticsRecords = records.stream()
                .map(r -> {
                    // Validate category
                    if (r.getCategory() == null || !VALID_ENRICHMENT_CATEGORIES.contains(r.getCategory().toLowerCase())) {
                        System.err.println("Skipping record id " + r.getId() + ": invalid category '" + r.getCategory() + "'");
                        return null; // skip this record
                    }

                    // Prepare enrichment request
                    EnrichmentRequest enrichReq = new EnrichmentRequest();
                    enrichReq.setId(r.getId());
                    enrichReq.setAsset(r.getAssetName());
                    enrichReq.setIp(r.getIp());
                    enrichReq.setCategory(r.getCategory());

                    EnrichmentResponse enrichResp = enrichmentService.enrich(enrichReq);

                    if (enrichResp == null) {
                        System.err.println("Enrichment failed for record id " + r.getId());
                        return null; // skip
                    } else {
                        System.out.println("Enrichment successful for record id " + r.getId());
                    }

                    // Map to analytics record
                    AnalyticsRecord aRecord = new AnalyticsRecord();
                    aRecord.setId(r.getId());
                    aRecord.setAsset(r.getAssetName());
                    aRecord.setIp(r.getIp());
                    aRecord.setCategory(enrichResp.getCategory());
                    aRecord.setAsn(enrichResp.getAsn());
                    aRecord.setCorrelationId(enrichResp.getCorrelationId());

                    return aRecord;

                })
                .filter(Objects::nonNull) // remove skipped records
                .collect(Collectors.toList());

        // Send to analytics
        analyticsService.sendRecords(analyticsRecords);

        return "Ingestion finished: " + analyticsRecords.size() + " records processed";
    }
}