package com.mercadolibre.bootcamp.projeto_integrador.integration;

import com.mercadolibre.bootcamp.projeto_integrador.dto.InboundOrderRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.integration.listeners.ResetDatabase;
import com.mercadolibre.bootcamp.projeto_integrador.model.*;
import com.mercadolibre.bootcamp.projeto_integrador.service.IInboundOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ResetDatabase
class DonationControllerTest extends BaseControllerTest {
    private Manager manager;
    private Section section;
    private Warehouse warehouse;
    private InboundOrderRequestDto validInboundOrderRequest;
    private Product product;
    @Autowired
    IInboundOrderService service;

    @BeforeEach
    void setup() {
        warehouse = getSavedWarehouse();
        manager = getSavedManager();
        section = getSavedSection(warehouse, manager, Section.Category.FRESH);
        product = getSavedProduct(Section.Category.FRESH);
        validInboundOrderRequest = getValidInboundOrderRequestDtoWithBatchList(section, getValidListBatchRequest(product));
        validInboundOrderRequest.getBatchStock().get(0).setBatchNumber(1);
        validInboundOrderRequest.getBatchStock().get(1).setBatchNumber(2);
    }

    @Test
    void getAvailable_returnBatches_whenValidParameters() throws Exception {
        service.create(validInboundOrderRequest, manager.getManagerId());
        String state = section.getWarehouse().getState();
        String city = section.getWarehouse().getCity();

        mockMvc.perform(get("/api/v1/foundations")
                        .param("state", state)
                        .param("city", city)
                        .header("Foundation-Id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(validInboundOrderRequest.getBatchStock().size()))
                .andExpect(jsonPath("$[0].batchNumber")
                        .value(validInboundOrderRequest.getBatchStock().get(0).getBatchNumber()))
                .andExpect(jsonPath("$[1].batchNumber")
                        .value(validInboundOrderRequest.getBatchStock().get(1).getBatchNumber()));
    }

    @Test
    void create() {
    }
}