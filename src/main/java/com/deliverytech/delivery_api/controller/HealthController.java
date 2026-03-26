package com.deliverytech.delivery_api.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, String> health(){
        return Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now().toString(),
            "service", "Delivery Api",
            "javaVersion", System.getProperty("java.version")
        );   
    }

    @GetMapping("/info")
    public AppInfo info(){
        return new AppInfo(
            "Delivery Tech Api",
            "1.0.0",
            "Elaine Soares",
            "JDK 21",
            "Spring Boot"
        );
    }

    public record AppInfo(
        String application,
        String version,
        String developer,
        String javaVersion,
        String framework
    ){}





}


