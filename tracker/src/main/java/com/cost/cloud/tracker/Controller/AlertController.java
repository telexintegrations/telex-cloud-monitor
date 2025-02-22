package com.cost.cloud.tracker.Controller;

import com.cost.cloud.tracker.Service.TelexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/alerts")
public class AlertController {
    private final TelexService telexService;

    @Autowired
    public AlertController(TelexService telexService) {
        this.telexService = telexService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendAlert(@RequestBody Map<String, String> request) {
        String eventName = request.get("event_name");
        String message = request.get("message");
        String status = request.get("status");
        String username = request.get("username");

        if (eventName == null || message == null || status == null || username == null) {
            return ResponseEntity.badRequest().body("❌ Missing required fields!");
        }

        telexService.sendAlert(eventName, message, status, username);
        return ResponseEntity.ok("✅ Alert sent successfully");
    }
}
