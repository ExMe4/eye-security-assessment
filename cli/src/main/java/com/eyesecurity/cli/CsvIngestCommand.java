package com.eyesecurity.cli;

import com.eyesecurity.cli.model.SecurityLogRecord;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.file.Path;

@Command(
        name = "csv-ingest",
        description = "Ingests security log CSV files into the ingestion service",
        mixinStandardHelpOptions = true
)
public class CsvIngestCommand implements Runnable {

    private static final String CSV_DELIMITER_REGEX = "[;\t]";

    @Option(
            names = "--file",
            required = true,
            description = "Path to the CSV file to ingest"
    )
    private Path csvFile;

    @Option(
            names = "--category",
            description = "Optional category filter"
    )
    private String categoryFilter;

    @Override
    public void run() {
        if (!csvFile.toFile().exists()) {
            System.err.println("Error: CSV file not found: " + csvFile);
            return;
        }

        List<SecurityLogRecord> records = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(csvFile)) {
            String line;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {

                if (!headerSkipped) {
                    // first row contains column names
                    headerSkipped = true;
                    continue;
                }

                String[] parts = line.split(CSV_DELIMITER_REGEX);

                if (parts.length < 5) {
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }

                String category = parts.length >= 6 ? parts[5].trim() : "";

                if (category.isEmpty()) {
                    System.out.println("Warning: missing category for record id " + parts[0].trim());
                }

                SecurityLogRecord record = new SecurityLogRecord(
                        Long.parseLong(parts[0].trim()),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim(),
                        category
                );

                records.add(record);
            }

        } catch (Exception e) {
            System.err.println("Failed to read CSV file: " + e.getMessage());
            return;
        }

        System.out.println("Total records read: " + records.size());

        List<SecurityLogRecord> filteredRecords = records;

        if (categoryFilter != null && !categoryFilter.isBlank()) {
            filteredRecords = records.stream()
                    .filter(r -> r.getCategory().equalsIgnoreCase(categoryFilter))
                    .toList();

            System.out.println(
                    "Records after filtering (category=" + categoryFilter + "): "
                            + filteredRecords.size()
            );
        } else {
            System.out.println("No category filter applied.");
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CsvIngestCommand()).execute(args);
        System.exit(exitCode);
    }
}