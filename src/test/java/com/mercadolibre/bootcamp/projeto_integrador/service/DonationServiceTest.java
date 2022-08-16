package com.mercadolibre.bootcamp.projeto_integrador.service;

import com.mercadolibre.bootcamp.projeto_integrador.dto.BatchFoundationResponseDto;
import com.mercadolibre.bootcamp.projeto_integrador.exceptions.NotFoundException;
import com.mercadolibre.bootcamp.projeto_integrador.model.*;
import com.mercadolibre.bootcamp.projeto_integrador.repository.IDonationRepository;
import com.mercadolibre.bootcamp.projeto_integrador.repository.IFoundationRepository;
import com.mercadolibre.bootcamp.projeto_integrador.util.BatchGenerator;
import com.mercadolibre.bootcamp.projeto_integrador.util.FoundationGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DonationServiceTest {

    @InjectMocks
    DonationService service;

    @Mock
    IDonationRepository donationRepository;
    @Mock
    IBatchService batchService;
    @Mock
    IFoundationRepository foundationRepository;

    private List<Batch> batches;
    private Section section;
    private Foundation foundation;
    private Warehouse warehouse;

    @BeforeEach
    void setup() {
        batches = BatchGenerator.newBatchList();
        section = batches.get(0).getInboundOrder().getSection();
        warehouse = section.getWarehouse();
        foundation = FoundationGenerator.newFoundation();
    }

    @Test
    void create() {
    }

    @Test
    void getAvailable_returnBatches_whenBatchesExists() {
        // Arrange
        batches.remove(0);
        when(foundationRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(foundation));
        when(batchService.findByLocationAndDueDate(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(batches);

        // Act
        BatchFoundationResponseDto returnDto = service.getAvailable(warehouse.getState(), warehouse.getCity(), foundation.getFoundationId());

        // Assert
        assertThat(returnDto.getBatches()).isNotEmpty();
        assertEquals(batches.size(), returnDto.getBatches().size());
        assertEquals(warehouse.getDistrict(), returnDto.getDistrict());
        assertEquals(warehouse.getLocation(), returnDto.getAddress());
        assertEquals(batches.get(0).getBatchNumber(), returnDto.getBatches().get(0).getBatchNumber());
        assertEquals(batches.get(1).getBatchNumber(), returnDto.getBatches().get(1).getBatchNumber());
        assertThat(returnDto.getBatches().get(0).getDueDate()).isBefore(LocalDate.now().plusDays(21));
        assertThat(returnDto.getBatches().get(1).getDueDate()).isBefore(LocalDate.now().plusDays(21));
    }

    @Test
    void getAvailable_returnNotFoundException_whenInvalidFoundation() {
        // Arrange
        when(foundationRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.getAvailable(warehouse.getState(), warehouse.getCity(), foundation.getFoundationId()));

        // Assert
        assertThat(exception.getName()).contains("Foundation not found");
        assertThat(exception.getMessage()).contains("There is no foundation with the specified id");
        verify(batchService, never()).findByLocationAndDueDate(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(), ArgumentMatchers.any());
    }
}