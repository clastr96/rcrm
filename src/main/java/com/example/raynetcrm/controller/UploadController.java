package com.example.raynetcrm.controller;

import com.example.raynetcrm.service.ClientImporterCSV;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
// TODO: rename controller, update request mapping
public class UploadController {
    private final ClientImporterCSV clientImporterCSV;
    private static String SUPPORTED_FILE_FORMAT = "text/csv";

    @PostMapping("/uploadData")
    public ResponseEntity<String> uploadData(@RequestParam("file") MultipartFile file) throws Exception {
        if (!Objects.equals(file.getContentType(), SUPPORTED_FILE_FORMAT)) {
            return ResponseEntity.badRequest().body("Invalid file format. Must be CSV.");
        }
        clientImporterCSV.processCsv(file.getInputStream());
        // TODO: other response..
        return ResponseEntity.ok("Processing the csv..");
    }
}
