# Eye Security Assessment

This repository contains a CLI application and a Spring Boot microservice for ingesting, enriching, and sending security log records to an analytics service. The project was developed as part of a security assessment.

## Prerequisites
Java 21

Maven 3.8

Spring Boot 3.2

## CLI Application
Navigate to the  folder:
```bash
cd cli
```

Run the following command:
```bash
mvn compile exec:java -Dexec.args="--file YOUR_CSV_FILE --category OPTIONAL_CATEGORY"
```

## Ingestion Service
Navigate to the  folder:
```bash
cd ingestion-service
```

Build the service:
```bash
mvn clean package
```

And run it:
```bash
java -jar target/ingestion_service-0.0.1-SNAPSHOT.jar
```

#### The microservice exposes a single POST endpoint:
POST http://localhost:8080/ingest

Content-Type: application/json

Body: List of SecurityLogRecord JSON objects
