package com.ga.bank.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileStorageUtils {

    // Prevent instantiation of utility class
    private FileStorageUtils() {
    }


    public static void ensureDirectoryExists(String dirPath) {
        if (dirPath == null || dirPath.trim().isEmpty()) {
            return;
        }
        File directory = new File(dirPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static List<String> readLines(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return new ArrayList<>();
        }

        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }

        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Error reading file at " + filePath + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }


    public static void writeLines(String filePath, List<String> lines) {
        if (filePath == null || filePath.trim().isEmpty() || lines == null) {
            return;
        }

        Path path = Paths.get(filePath);

        // Ensure parent directory exists before writing
        Path parentDir = path.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            try {
                Files.createDirectories(parentDir);
            } catch (IOException e) {
                System.err.println("Failed to create parent directories for " + filePath + ": " + e.getMessage());
                return;
            }
        }

        try {
            Files.write(
                    path,
                    lines,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );
        } catch (IOException e) {
            System.err.println("Error writing to file at " + filePath + ": " + e.getMessage());
        }
    }
}