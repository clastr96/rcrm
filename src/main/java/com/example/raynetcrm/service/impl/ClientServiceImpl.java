package com.example.raynetcrm.service.impl;

import com.example.raynetcrm.dto.ClientDto;
import com.example.raynetcrm.entity.Client;
import com.example.raynetcrm.repository.ClientRepository;
import com.example.raynetcrm.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    @Override
    public boolean clientExists(String regNumber) {
        return clientRepository.existsByRegNumber(regNumber);
    }

    @Override
    public Client createClient(ClientDto clientDto) {
        Client client = new Client();
        client.setRegNumber(clientDto.getRegNumber());
        client.setTitle(clientDto.getTitle());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());
        return clientRepository.save(client);
    }

    @Override
    public List<Client> updateClient(ClientDto clientDto) {
        List<Client> clientsToUpdate = clientRepository.findAllByRegNumber(clientDto.getRegNumber());
        List<Client> updatedClients = new ArrayList<>();

        for (Client client : clientsToUpdate) {
            client.setTitle(client.getTitle());
            client.setEmail(client.getEmail());
            client.setPhone(client.getPhone());
            updatedClients.add(clientRepository.save(client));
        }
        return updatedClients;
    }

    @Override
    public List<Client> findAllReadyForUpsert() {
        return clientRepository.findAllByUpsertDateIsNull();
    }

    @Override
    public Client markClientAsUpserted(Client client){
        client.setUpsertDate(Instant.now());
        return clientRepository.save(client);
    }
}
