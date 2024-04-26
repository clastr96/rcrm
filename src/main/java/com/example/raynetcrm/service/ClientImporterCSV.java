package com.example.raynetcrm.service;

import java.io.InputStream;

public interface ClientImporterCSV {
    void processCsv(InputStream inputStream) throws Exception;
}
