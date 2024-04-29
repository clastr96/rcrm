package com.example.raynetcrm.service.impl;

import com.example.raynetcrm.dto.ClientCsvBean;
import com.example.raynetcrm.entity.Client;
import com.example.raynetcrm.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;


    @Test
    void testClientExists() {
        // Given
        String regNumber = "123456789";
        when(clientRepository.existsByRegNumber(regNumber)).thenReturn(true);

        // When
        boolean exists = clientService.clientExists(regNumber);

        // Then
        assertTrue(exists);
    }

    @Test
    void testCreateClient() {
        // Given
        Client expectedClient = new Client();
        when(clientRepository.save(any(Client.class))).thenReturn(expectedClient);

        ClientCsvBean clientCsvBean = new ClientCsvBean();
        clientCsvBean.setRegNumber("123456789");
        clientCsvBean.setTitle("Test Client");
        clientCsvBean.setEmail("test@example.com");
        clientCsvBean.setPhone("123-456-7890");

        // When
        Client createdClient = clientService.createClient(clientCsvBean);

        // Then
        assertNotNull(createdClient);
        assertEquals(expectedClient, createdClient);
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void testUpdateClient() {
        // Given
        Client expectedClient = new Client();
        List<Client> clientsToUpdate = new ArrayList<>();
        clientsToUpdate.add(expectedClient);

        when(clientRepository.findAllByRegNumber(anyString())).thenReturn(clientsToUpdate);
        when(clientRepository.save(any(Client.class))).thenReturn(expectedClient);

        ClientCsvBean clientCsvBean = new ClientCsvBean();
        clientCsvBean.setRegNumber("123456789");
        clientCsvBean.setTitle("Updated Client");
        clientCsvBean.setEmail("updated@example.com");
        clientCsvBean.setPhone("987-654-3210");

        // When
        List<Client> updatedClients = clientService.updateClient(clientCsvBean);

        // Then
        assertFalse(updatedClients.isEmpty());
        assertEquals(expectedClient, updatedClients.getFirst());
        verify(clientRepository).save(any(Client.class));
    }
}
