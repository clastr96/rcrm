package com.example.raynetcrm.service;

import com.example.raynetcrm.dto.ClientDto;
import com.example.raynetcrm.entity.Client;

import java.util.List;

public interface ClientService {
    boolean clientExists(String regNumber);

    Client createClient(ClientDto clientDto);

    List<Client> updateClient(ClientDto clientDto);
    List<Client> findAllReadyForUpsert();
    Client markClientAsUpserted(Client client);
}
