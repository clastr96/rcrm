package com.example.raynetcrm.service.impl;

import com.example.raynetcrm.dto.ClientCsvBean;
import com.example.raynetcrm.entity.Client;
import com.example.raynetcrm.service.ClientService;
import com.example.raynetcrm.service.RCRMService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientImporterCSVImplTest {

    @Mock
    private ClientService clientService;

    @Mock
    private RCRMService rcrmService;

    @InjectMocks
    private ClientImporterCSVImpl clientImporterCSV;

    @Test
    void testProcessCsv_ValidCsvData() throws IOException {
        // Given
        InputStream inputStream = new ClassPathResource("test_valid.csv").getInputStream();

        when(clientService.clientExists(anyString())).thenReturn(false);
        when(clientService.createClient(any(ClientCsvBean.class))).thenReturn(new Client());
        doNothing().when(rcrmService).upsertClient(any(Client.class), anyBoolean());

        // When
        clientImporterCSV.processCsv(inputStream);

        // Then
        verify(clientService, times(3)).clientExists(anyString());
        verify(clientService, times(3)).createClient(any(ClientCsvBean.class));
        verify(rcrmService, times(3)).upsertClient(any(Client.class), anyBoolean());
    }

    @Test
    void testProcessCsv_InvalidCsvData() throws IOException {
        // Given
        InputStream inputStream = new ClassPathResource("test_invalid.csv").getInputStream();

        when(clientService.clientExists(anyString())).thenReturn(false);
        when(clientService.createClient(any(ClientCsvBean.class))).thenReturn(new Client());
        doNothing().when(rcrmService).upsertClient(any(Client.class), anyBoolean());

        // When
        clientImporterCSV.processCsv(inputStream);

        // Then
        verify(clientService, times(2)).clientExists(anyString());
        verify(clientService, times(2)).createClient(any(ClientCsvBean.class));
        verify(rcrmService, times(2)).upsertClient(any(Client.class), anyBoolean());
    }
}