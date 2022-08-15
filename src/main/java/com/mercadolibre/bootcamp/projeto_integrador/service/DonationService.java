package com.mercadolibre.bootcamp.projeto_integrador.service;

import com.mercadolibre.bootcamp.projeto_integrador.dto.BatchBuyerResponseDto;
import com.mercadolibre.bootcamp.projeto_integrador.dto.BatchFoundationResponseDto;
import com.mercadolibre.bootcamp.projeto_integrador.model.Batch;
import com.mercadolibre.bootcamp.projeto_integrador.model.Warehouse;
import com.mercadolibre.bootcamp.projeto_integrador.repository.IBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DonationService implements IDonationService {

    @Autowired
    private IBatchService batchService;

    @Override
    public BatchFoundationResponseDto getAvailable(String state, String city, Long foundationId) {
        LocalDate maxDueDate = LocalDate.now().plusDays(21);
        List<Batch> batches = batchService.findByLocationAndDueDate(state, city, maxDueDate);
        Warehouse warehouse = batches.get(0).getInboundOrder().getSection().getWarehouse();
        return new BatchFoundationResponseDto(warehouse, batches);
    }
}
