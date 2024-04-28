package com.example.raynetcrm.service.impl;

import com.example.raynetcrm.entity.Client;
import com.example.raynetcrm.mail.EmailSender;
import com.example.raynetcrm.service.ClientService;
import com.example.raynetcrm.service.RCRMService;
import com.example.raynetcrm.util.JsonUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class RCRMServiceImpl implements RCRMService {
    private final RestTemplate restTemplate;
    private final ClientService clientService;
    private final EmailSender emailSender;

    @Value("${rcrm.api.url}")
    private String url;
    @Value("${rcrm.api.key}")
    private String key;
    @Value("${rcrm.api.username}")
    private String username;
    @Value("${rcrm.api.instancename}")
    private String instancename;
    private int rateLimitCount;
    private static int RATE_LIMIT_TRESHOLD = 10;
    public void upsertClient(Client client, boolean lastRecord) {
        if (canSendRequest()) {
            Optional<List<Long>> idsToUpdateOptional = Optional.ofNullable(isAlreadyRegistered(client));
            idsToUpdateOptional.ifPresent(idsToUpdate -> {
                if (!idsToUpdate.isEmpty()) {
                    idsToUpdate.forEach(id -> updateClient(id, client));
                } else {
                    createClient(client);
                }
            });
        } else {
            log.error("Exceeded the daily rate limit, will retry in scheduled job.");
            return;
        }

        if (lastRecord) {
            log.info("Processed all records.");
            emailSender.sendEmail(
                    "Successfully processed all clients imports.",
                    "We have successfully processed all imported clients."
            );
        }
    }
    private void createClient(Client client) {
        Map<String, Object> requestBody = createHttpRequestBody(client);
        HttpEntity<String> httpEntity = getHttpEntity(requestBody);
        ResponseEntity<String> response = restTemplate.exchange(
                url + "/company/",
                HttpMethod.PUT,
                httpEntity,
                String.class
        );
        updateRateLimitCount(response.getHeaders());
        if (response.getStatusCode() != HttpStatus.CREATED) {
            log.warn("There was an error creating a client with regNumber: {}", client.getRegNumber());
            return;
        }
        clientService.markClientAsUpserted(client);
    }

    private void updateClient(Long id, Client client) {
        Map<String, Object> requestBody = createHttpRequestBody(client);
        HttpEntity<String> httpEntity = getHttpEntity(requestBody);
        ResponseEntity<String> response = restTemplate.exchange(
                url + "/company/" + id,
                HttpMethod.POST,
                httpEntity,
                String.class
        );
        updateRateLimitCount(response.getHeaders());
        if (response.getStatusCode() != HttpStatus.OK) {
            log.warn("There was an error updating a client with regNumber: {}", client.getRegNumber());
            return;
        }
        clientService.markClientAsUpserted(client);
    }

    private HttpEntity<String> getHttpEntity(Map<String, Object> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        String encodedCredentials = Base64.getEncoder().encodeToString((username + ":" + key).getBytes());
        headers.set("X-Instance-Name", instancename);
        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + encodedCredentials);
        return new HttpEntity<String>(JsonUtil.toJson(requestBody), headers);
    }

    private Map<String, Object> createHttpRequestBody(Client client) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", client.getTitle());
        map.put("regNumber", client.getRegNumber());
        map.put("rating", "A");
        map.put("state", "B_ACTUAL");
        map.put("role", "B_PARTNER");
        return map;
    }

    private List<Long> isAlreadyRegistered(Client client) {
        HttpEntity<String> entity = getHttpEntity(new HashMap<>());
        ResponseEntity<String> response = restTemplate.exchange(
                url + "company/?regNumber=" + client.getRegNumber(),
                HttpMethod.GET,
                entity,
                String.class
        );
        updateRateLimitCount(response.getHeaders());
        if (response.getStatusCode() != HttpStatus.OK) {
            log.warn("There was an error fetching an existing client  with regNumber: {}", client.getRegNumber());
            return null;
        }

        Map map = JsonUtil.fromJson(response.getBody(), Map.class);
        return extractDuplicateIds(map);
    }

    private List<Long> extractDuplicateIds(Map<String, Object> response) {
        List<Long> ids = new ArrayList<>();

        if (response.containsKey("totalCount") && response.containsKey("data")) {
            Number totalCount = (Number) response.get("totalCount");
            if (totalCount != null && totalCount.longValue() > 0) {
                List<Map<String, Object>> dataList = (List<Map<String, Object>>) response.get("data");
                for (Map<String, Object> item : dataList) {
                    Object idObject = item.get("id");
                    if (idObject instanceof Number) {
                        ids.add(((Number) idObject).longValue());
                    }
                }
            }
        }
        return ids;
    }

    private void updateRateLimitCount(HttpHeaders headers) {
        String limitRemaining = Objects.requireNonNull(headers.get("X-Ratelimit-Remaining")).getFirst();
        rateLimitCount = Integer.parseInt(limitRemaining);
    }

    @PostConstruct
    private void setInitialRateLimitCount() {
        HttpEntity<String> entity = getHttpEntity(new HashMap<>());
        ResponseEntity<String> response = restTemplate.exchange(
                url + "company/",
                HttpMethod.GET,
                entity,
                String.class
        );
        if (response.getStatusCode() != HttpStatus.OK) {
            log.warn("There was an error calling an initial request");
            return;
        }
        updateRateLimitCount(response.getHeaders());
    }

    private boolean canSendRequest() {
        return rateLimitCount > RATE_LIMIT_TRESHOLD;
    }

}
