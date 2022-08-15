package com.mercadolibre.bootcamp.projeto_integrador.service;

import com.mercadolibre.bootcamp.projeto_integrador.dto.BatchBuyerResponseDto;
import com.mercadolibre.bootcamp.projeto_integrador.dto.BatchDueDateResponseDto;
import com.mercadolibre.bootcamp.projeto_integrador.dto.BatchRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.model.Batch;
import com.mercadolibre.bootcamp.projeto_integrador.model.InboundOrder;

import java.time.LocalDate;
import java.util.List;

public interface IBatchService {
    List<Batch> createAll(List<BatchRequestDto> batchesDto, InboundOrder order);

    List<Batch> updateAll(InboundOrder order, List<BatchRequestDto> batchesDto);

    @Deprecated
    Batch update(InboundOrder order, Batch batch);

    List<BatchBuyerResponseDto> findAll();

    List<BatchBuyerResponseDto> findBatchByCategory(String categoryCode);

    List<BatchDueDateResponseDto> findBatchBySection(long sectionCode, int numberOfDays, long managerId);

    List<BatchDueDateResponseDto> findBatchByCategoryAndDueDate(String categoryCode, int numberOfDays, String orderDir, long managerId);

    /**
     * @param state Estado do armazém
     * @param city Cidade do armazém
     * @param maxDueDate Data máxima de expiração
     * @return Lista de Batch filtrados
     */
    List<Batch> findByLocationAndDueDate(String state, String city, LocalDate maxDueDate);
}
