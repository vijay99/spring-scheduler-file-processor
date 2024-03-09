package com.example.springschedulerfileprocessor.scheduler;

import com.example.springschedulerfileprocessor.service.FileProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Component
public class FileCheckerScheduler {

    @Value("${source.folder}")
    private String sourceFolder;

    @Value("${destination.folder}")
    private String destinationFolder;

    @Autowired
    private FileProcessingService fileProcessingService;

    @Scheduled(fixedDelay = 60000) // Check every minute
    public void checkAndProcessFile() {
        System.out.println("I am inside checkAndProcessFile()::::::::::");

        File sourceDirectory = new File(sourceFolder);
        /*System.out.println("sourceDirectory ::"+sourceDirectory);
        System.out.println("sourceDirectory.exists():::"+sourceDirectory.exists());
        System.out.println("sourceDirectory.isDirectory():::"+sourceDirectory.isDirectory());*/

        //check if source directory exists
        if (sourceDirectory.exists() && sourceDirectory.isDirectory()) {
            System.out.println("inside if check:::::");
            File[] files = sourceDirectory.listFiles((dir, name) -> name.startsWith("IR_ScoreV2_BASEREASONS"));
            System.out.println("files.length::"+files.length);

            if (files != null && files.length > 0) {
                // Process each matching file
                System.out.println("inside check::::::::"+files != null && files.length > 0 );
                for (File file : files) {
                    // Process the file
                    fileProcessingService.processFile(file.getAbsolutePath());

                    // Move the processed file to the destination folder
                   /* File destinationFile = new File(destinationFolder, file.getName());
                    file.renameTo(destinationFile);*/
                    // Delete the source file after successful processing
                    deleteSourceFile(file.getAbsolutePath());
                }

            }


        }
    }

    private void deleteSourceFile(String filePath) {
        Path sourcePath = Paths.get(filePath);

        try {
            // Introduce a short delay before attempting deletion
            Thread.sleep(1000);

            Files.deleteIfExists(sourcePath);
            System.out.println("Source file deleted successfully: " + filePath);
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to delete source file: " + filePath);
            e.printStackTrace();
        }
    }
}
