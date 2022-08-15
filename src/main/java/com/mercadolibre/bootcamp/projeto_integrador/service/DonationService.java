package com.mercadolibre.bootcamp.projeto_integrador.service;

import com.mercadolibre.bootcamp.projeto_integrador.dto.BatchBuyerResponseDto;
import com.mercadolibre.bootcamp.projeto_integrador.dto.BatchFoundationResponseDto;
import com.mercadolibre.bootcamp.projeto_integrador.exceptions.BadRequestException;
import com.mercadolibre.bootcamp.projeto_integrador.exceptions.BatchOutOfStockException;
import com.mercadolibre.bootcamp.projeto_integrador.exceptions.NotFoundException;
import com.mercadolibre.bootcamp.projeto_integrador.model.Batch;
import com.mercadolibre.bootcamp.projeto_integrador.model.Donation;
import com.mercadolibre.bootcamp.projeto_integrador.model.Foundation;
import com.mercadolibre.bootcamp.projeto_integrador.model.Warehouse;
import com.mercadolibre.bootcamp.projeto_integrador.repository.IBatchRepository;
import com.mercadolibre.bootcamp.projeto_integrador.repository.IDonationRepository;
import com.mercadolibre.bootcamp.projeto_integrador.repository.IFoundationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DonationService implements IDonationService {
    @Autowired
    private IDonationRepository donationRepository;
    @Autowired
    private IBatchService batchService;
    @Autowired
    private IFoundationRepository foundationRepository;

    @Override
    public Donation create(long batchNumber, long foundationId) {
        Optional<Foundation> foundation = foundationRepository.findById(foundationId);
        if (foundation.isEmpty()) throw new NotFoundException("Foundation");

        Batch batch = batchService.findById(batchNumber);
        if (batch.getCurrentQuantity() <= 0) throw new BatchOutOfStockException(batchNumber);
        if (batch.getDueDate().isAfter(LocalDate.now().plusDays(21))) throw new BadRequestException("Produto indisponível para doação.");

        Donation donation = new Donation(UUID.randomUUID(), batch.getCurrentQuantity(), LocalDate.now(), foundation.get(), batch);
        batchService.updateCurrentQuantity(batchNumber, batch.getCurrentQuantity());
        donationRepository.save(donation);
        return donation;
    }

    @Override
    public BatchFoundationResponseDto getAvailable(String state, String city, long foundationId) {
        Optional<Foundation> foundation = foundationRepository.findById(foundationId);
        if (foundation.isEmpty()) throw new NotFoundException("Foundation");

        LocalDate maxDueDate = LocalDate.now().plusDays(21);
        List<Batch> batches = batchService.findByLocationAndDueDate(state, city, maxDueDate);
        Warehouse warehouse = batches.get(0).getInboundOrder().getSection().getWarehouse();
        return new BatchFoundationResponseDto(warehouse, batches);
    }
}
