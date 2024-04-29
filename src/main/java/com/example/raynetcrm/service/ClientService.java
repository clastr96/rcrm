package com.example.raynetcrm.service;

import com.example.raynetcrm.dto.ClientCsvBean;
import com.example.raynetcrm.entity.Client;

import java.util.List;

public interface ClientService {
    boolean clientExists(String regNumber);

    Client createClient(ClientCsvBean clientCsvBean);

    List<Client> updateClient(ClientCsvBean clientCsvBean);
    List<Client> findAllReadyForUpsert();
    Client markClientAsUpserted(Client client);
}
