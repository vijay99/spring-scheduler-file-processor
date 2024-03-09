package com.example.springschedulerfileprocessor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileProcessingService {

    @Value("${destination.folder}")
    private String destinationFolder;

    public void processFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Map to store combined values for each unique key (first and second columns)
            Map<String, StringBuilder> combinedValuesMap = new HashMap<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split("\\|");

                if (columns.length == 3) {
                    String key = columns[0] + "|" + columns[1];
                    String value = columns[2];

                    // Combine values for the same key
                    combinedValuesMap.computeIfAbsent(key, k -> new StringBuilder())
                            .append(value)
                            .append(",");
                }
            }

            // Write the combined records to the output file in the destination folder
            String outputFileName="IR_ScoreV2_BASEREASONS_FINAL_"+getCurrentDate()+".dat";
            Path outputPath = Paths.get(destinationFolder, outputFileName);
            try (FileWriter writer = new FileWriter(outputPath.toFile())) {
                combinedValuesMap.forEach((key, combinedValues) -> {
                    // Remove the trailing comma and write the combined record
                    String combinedRecord = key + "|" + combinedValues.substring(0, combinedValues.length() - 1);
                    try {
                        writer.write(combinedRecord + System.lineSeparator());
                    } catch (IOException e) {
                        e.printStackTrace(); // Handle the exception as needed
                    }
                });
            }


        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as needed
        }
    }

    private String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
        return currentDate.format(formatter);
    }



}
