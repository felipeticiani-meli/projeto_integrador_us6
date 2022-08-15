package com.mercadolibre.bootcamp.projeto_integrador.service;

import com.mercadolibre.bootcamp.projeto_integrador.dto.BatchFoundationResponseDto;

public interface IDonationService {
    //void create();

    /**
     * author: Felipe Ticiani
     * @param state Estado do armazém
     * @param city Cidade do armazém
     * @param foundationId ID da instituição
     * @return Lista de Batch que expiram em até 21 dias.
     */
    BatchFoundationResponseDto getAvailable(String state, String city, Long foundationId);
}
