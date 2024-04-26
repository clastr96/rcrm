package com.example.raynetcrm.service.impl;

import com.example.raynetcrm.dto.ClientDto;
import com.example.raynetcrm.entity.Client;
import com.example.raynetcrm.service.ClientImporterCSV;
import com.example.raynetcrm.service.ClientService;
import com.example.raynetcrm.service.RCRMService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientImporterCSVImpl implements ClientImporterCSV {
    private final ClientService clientService;
    private final RCRMService rcrmService;
    @Async
    @Override
    public void processCsv(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            CsvToBean<ClientDto> csvReader = getCsvToBeanBuilder(reader).build();
            processCsvRecords(csvReader.iterator());
        }
    }

    private void processCsvRecords(Iterator<ClientDto> iterator) {
        while (iterator.hasNext()) {
            ClientDto clientDto = iterator.next();
            if (clientDto.isValid()) {
                processClient(clientDto);
            }
        }
    }

    private CsvToBeanBuilder<ClientDto> getCsvToBeanBuilder(BufferedReader reader) {
        return new CsvToBeanBuilder<ClientDto>(reader)
                .withType(ClientDto.class)
                .withSeparator(';')
                .withIgnoreEmptyLine(true)
                .withSkipLines(1);
    }

    private void processClient(ClientDto clientDto) {
        boolean doesExistInDb = clientService.clientExists(clientDto.getRegNumber());
        if (doesExistInDb) {
            List<Client> clients = clientService.updateClient(clientDto);
            for (Client client : clients) {
                rcrmService.upsertClient(client);
            }
        } else {
            Client client = clientService.createClient(clientDto);
            rcrmService.upsertClient(client);
        }
    }
}
