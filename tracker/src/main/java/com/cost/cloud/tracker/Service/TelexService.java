package com.cost.cloud.tracker.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class TelexService {
    private static final Logger logger = LoggerFactory.getLogger(TelexService.class);
    private final RestTemplate restTemplate;

    @Value("${telex.webhook.url}")
    private String telexWebhookUrl;

    public TelexService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendAlert(String eventName, String message, String status, String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Creating the JSON payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("event_name", eventName);
        payload.put("message", message);
        payload.put("status", status);
        payload.put("username", username);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    telexWebhookUrl, HttpMethod.POST, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("✅ Alert successfully sent to Telex: {}", payload);
            } else {
                logger.warn("⚠️ Failed to send alert. HTTP Status: {} | Response: {}",
                        response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            logger.error("❌ Error sending alert to Telex: {}", e.getMessage(), e);
        }
    }

    public void sendAlert(String message) {
    }
}
