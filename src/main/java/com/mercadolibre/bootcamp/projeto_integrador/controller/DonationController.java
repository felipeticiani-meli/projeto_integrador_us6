package com.mercadolibre.bootcamp.projeto_integrador.controller;

import com.mercadolibre.bootcamp.projeto_integrador.dto.BatchFoundationResponseDto;
import com.mercadolibre.bootcamp.projeto_integrador.model.Donation;
import com.mercadolibre.bootcamp.projeto_integrador.service.IDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class DonationController {

    @Autowired
    private IDonationService service;

    @GetMapping("/foundations")
    public ResponseEntity<BatchFoundationResponseDto> getAvailable(@RequestParam String state,
                                                                   @RequestParam String city,
                                                                   @RequestHeader("Foundation-Id") Long foundationId) {
        return ResponseEntity.ok(service.getAvailable(state, city, foundationId));
    }

    @PostMapping("/foundations")
    public ResponseEntity<Donation> create(@RequestParam long batchNumber, @RequestHeader("Foundation-Id") Long foundationId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(batchNumber, foundationId));
    }
}
