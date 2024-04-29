package com.example.raynetcrm.controller;

import com.example.raynetcrm.service.ClientImporterCSV;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class DataImportController {
    private final ClientImporterCSV clientImporterCSV;
    private static final String SUPPORTED_FILE_FORMAT = "text/csv";

    @PostMapping("/uploadData")
    public ResponseEntity<String> uploadData(@RequestParam("file") MultipartFile file) throws IOException {
        if (!Objects.equals(file.getContentType(), SUPPORTED_FILE_FORMAT)) {
            return ResponseEntity.badRequest().body("Invalid file format. Must be CSV.");
        }
        clientImporterCSV.processCsv(file.getInputStream());
        return ResponseEntity.accepted().body("Your CSV file is being processed.");
    }
}
