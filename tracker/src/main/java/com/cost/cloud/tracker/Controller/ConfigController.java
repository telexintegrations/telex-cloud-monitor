package com.cost.cloud.tracker.Controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @GetMapping("/telex-integration")
    public ResponseEntity<String> getTelexIntegrationJson() {
        try {
            // Load the JSON file from resources
            Resource resource = new ClassPathResource("telex-integration.json");
            byte[] jsonData = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String jsonString = new String(jsonData, StandardCharsets.UTF_8);

            // Set HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

            return new ResponseEntity<>(jsonString, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Failed to load JSON\"}");
        }
    }
}
