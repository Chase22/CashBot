package org.chase.telegram.cashbot;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @RequestMapping("/")
    public ResponseEntity health() {
        return ResponseEntity.noContent().build();
    }
}
