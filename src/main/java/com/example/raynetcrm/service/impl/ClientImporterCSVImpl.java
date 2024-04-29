package com.example.raynetcrm.service.impl;

import com.example.raynetcrm.dto.ClientCsvBean;
import com.example.raynetcrm.entity.Client;
import com.example.raynetcrm.service.ClientImporterCSV;
import com.example.raynetcrm.service.ClientService;
import com.example.raynetcrm.service.RCRMService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ClientImporterCSVImpl implements ClientImporterCSV {
    private final ClientService clientService;
    private final RCRMService rcrmService;
    @Async
    @Override
    public void processCsv(InputStream inputStream) {
        log.info("Start processing the CSV file.");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            CsvToBean<ClientCsvBean> csvReader = getCsvToBeanBuilder(reader).build();
            processCsvRecords(csvReader.iterator());
        } catch (IOException e) {
            log.error("Error processing CSV file: {}", e.getMessage());
        }
    }

    private void processCsvRecords(Iterator<ClientCsvBean> iterator) {
        while (iterator.hasNext()) {
            ClientCsvBean clientCsvBean = iterator.next();
            boolean isLastRecord = !iterator.hasNext();
            if (clientCsvBean.isValid()) {
                processClient(clientCsvBean, isLastRecord);
            }
        }
    }

    private CsvToBeanBuilder<ClientCsvBean> getCsvToBeanBuilder(BufferedReader reader) {
        return new CsvToBeanBuilder<ClientCsvBean>(reader)
                .withType(ClientCsvBean.class)
                .withSeparator(';')
                .withIgnoreEmptyLine(true)
                .withSkipLines(1);
    }

    private void processClient(ClientCsvBean clientCsvBean, boolean lastRecord) {
        boolean doesExistInDb = clientService.clientExists(clientCsvBean.getRegNumber());
        if (doesExistInDb) {
            List<Client> clients = clientService.updateClient(clientCsvBean);
            updateClients(clients, lastRecord);
        } else {
            Client client = clientService.createClient(clientCsvBean);
            rcrmService.upsertClient(client, lastRecord);
        }
    }

    private void updateClients(List<Client> clientList, boolean lastRecord) {
        int size = clientList.size();
        for (int i = 0; i < size; i++) {
            boolean lastRecordInList = lastRecord && (i == size - 1);
            rcrmService.upsertClient(clientList.get(i), lastRecordInList);
        }
    }
}
