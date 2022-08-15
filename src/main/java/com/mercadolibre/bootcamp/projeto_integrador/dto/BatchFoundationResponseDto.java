package com.mercadolibre.bootcamp.projeto_integrador.dto;

import com.mercadolibre.bootcamp.projeto_integrador.model.Batch;
import com.mercadolibre.bootcamp.projeto_integrador.model.Section;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
public class BatchFoundationResponseDto {
    private long batchNumber;
    private String productName;
    private String brand;
    private Section.Category category;
    private int quantity;
    private LocalDate dueDate;

    public BatchFoundationResponseDto(Batch batch) {
        this.batchNumber = batch.getBatchNumber();
        this.productName = batch.getProduct().getProductName();
        this.brand = batch.getProduct().getBrand();
        this.category = batch.getProduct().getCategory();
        this.quantity = batch.getCurrentQuantity();
        this.dueDate = batch.getDueDate();
    }
}
