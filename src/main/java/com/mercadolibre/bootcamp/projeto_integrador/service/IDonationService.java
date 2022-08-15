package com.mercadolibre.bootcamp.projeto_integrador.service;

import com.mercadolibre.bootcamp.projeto_integrador.dto.BatchFoundationResponseDto;
import com.mercadolibre.bootcamp.projeto_integrador.model.Donation;

public interface IDonationService {
    /**
     * Cria uma nova doação, gerando um UUID como token para retirada no centro de distribuição.
     * @param batchNumber ID no batch
     * @param foundationId ID da instituição
     * @return Donation criada
     */
    Donation create(long batchNumber, long foundationId);

    /**
     * Verifica e retorna os produtos disponíveis para doação.
     * @param state Estado do armazém
     * @param city Cidade do armazém
     * @param foundationId ID da instituição
     * @return Lista de Batch que expiram em até 21 dias.
     */
    BatchFoundationResponseDto getAvailable(String state, String city, long foundationId);
}
