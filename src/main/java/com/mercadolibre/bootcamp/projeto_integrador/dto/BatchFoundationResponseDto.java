package com.mercadolibre.bootcamp.projeto_integrador.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mercadolibre.bootcamp.projeto_integrador.model.Batch;
import com.mercadolibre.bootcamp.projeto_integrador.model.Section;
import com.mercadolibre.bootcamp.projeto_integrador.model.Warehouse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class BatchFoundationResponseDto {
    private String district;
    private String address;
    @JsonIgnoreProperties("productPrice")
    private List<BatchBuyerResponseDto> batches;

    public BatchFoundationResponseDto(Warehouse warehouse, List<Batch> batches) {
        this.district = warehouse.getDistrict();
        this.address = warehouse.getLocation();
        List<BatchBuyerResponseDto> batchDto = new ArrayList<>();
        batches.forEach(b -> batchDto.add(new BatchBuyerResponseDto(b)));
        this.batches = batchDto;
    }
}
