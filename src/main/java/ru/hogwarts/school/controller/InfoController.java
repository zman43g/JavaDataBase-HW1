package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/port")

public class InfoController {
    @Value("${server.port}")
    private String portNumber;

    @GetMapping
    @Operation(summary = "Получение данных о номере порта ")
    public ResponseEntity<String> getPortInfo() {
        return ResponseEntity.ok(portNumber);
    }
}
