package com.example.raynetcrm.cron;

import com.example.raynetcrm.entity.Client;
import com.example.raynetcrm.entity.CronLock;
import com.example.raynetcrm.service.ClientService;
import com.example.raynetcrm.service.CronLockService;
import com.example.raynetcrm.service.RCRMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClientUpsertTask {
    private final CronLockService cronLockService;
    private final ClientService clientService;
    private final RCRMService rcrmService;
    @Scheduled(cron = "0 0 * * * *")
    public void upsertTask() {
        log.info("Starting ClientUpsertTask.");

        if (!cronLockService.acquireLock()) {
            CronLock lock = cronLockService.createLock();
            List<Client> clientsReadyForUpsert = clientService.findAllReadyForUpsert();
            int listSize = clientsReadyForUpsert.size();

            for (int i = 0; i < listSize; i++) {
                Client client = clientsReadyForUpsert.get(i);
                boolean isLastRecord = i == listSize - 1;

                rcrmService.upsertClient(client, isLastRecord);
            }

            cronLockService.releaseLock(lock);
        }
        log.info("Finished ClientUpsertTask.");
    }
}
