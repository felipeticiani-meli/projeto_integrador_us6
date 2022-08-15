package com.mercadolibre.bootcamp.projeto_integrador.service;

import com.mercadolibre.bootcamp.projeto_integrador.dto.BatchBuyerResponseDto;
import com.mercadolibre.bootcamp.projeto_integrador.dto.BatchFoundationResponseDto;
import com.mercadolibre.bootcamp.projeto_integrador.exceptions.NotFoundException;
import com.mercadolibre.bootcamp.projeto_integrador.model.Batch;
import com.mercadolibre.bootcamp.projeto_integrador.model.Foundation;
import com.mercadolibre.bootcamp.projeto_integrador.model.Warehouse;
import com.mercadolibre.bootcamp.projeto_integrador.repository.IBatchRepository;
import com.mercadolibre.bootcamp.projeto_integrador.repository.IFoundationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DonationService implements IDonationService {

    @Autowired
    private IBatchService batchService;
    @Autowired
    private IFoundationRepository foundationRepository;

    @Override
    public BatchFoundationResponseDto getAvailable(String state, String city, Long foundationId) {
        Optional<Foundation> foundation = foundationRepository.findById(foundationId);
        if (foundation.isEmpty()) throw new NotFoundException("Foundation");

        LocalDate maxDueDate = LocalDate.now().plusDays(21);
        List<Batch> batches = batchService.findByLocationAndDueDate(state, city, maxDueDate);
        Warehouse warehouse = batches.get(0).getInboundOrder().getSection().getWarehouse();
        return new BatchFoundationResponseDto(warehouse, batches);
    }
}
