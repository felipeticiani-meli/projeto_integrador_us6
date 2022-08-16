package com.mercadolibre.bootcamp.projeto_integrador.integration;

import com.mercadolibre.bootcamp.projeto_integrador.dto.BatchRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.dto.InboundOrderRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.integration.listeners.ResetDatabase;
import com.mercadolibre.bootcamp.projeto_integrador.model.*;
import com.mercadolibre.bootcamp.projeto_integrador.service.IBatchService;
import com.mercadolibre.bootcamp.projeto_integrador.service.IInboundOrderService;
import com.mercadolibre.bootcamp.projeto_integrador.service.ISectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private Foundation foundation;
    @Autowired
    IInboundOrderService inboundOrderService;
    @Autowired
    IBatchService batchService;

    @BeforeEach
    void setup() {
        warehouse = getSavedWarehouse();
        manager = getSavedManager();
        section = getSavedSection(warehouse, manager, Section.Category.FRESH);
        product = getSavedProduct(Section.Category.FRESH);
        foundation = getSavedFoundation();
        validInboundOrderRequest = getValidInboundOrderRequestDtoWithBatchList(section, getValidListBatchRequest(product));
        validInboundOrderRequest.getBatchStock().get(0).setBatchNumber(1);
        validInboundOrderRequest.getBatchStock().get(0).setDueDate(LocalDate.now().plusDays(15));
        validInboundOrderRequest.getBatchStock().get(1).setBatchNumber(2);
    }

    @Test
    void getAvailable_returnBatches_whenValidParameters() throws Exception {
        inboundOrderService.create(validInboundOrderRequest, manager.getManagerId());

        mockMvc.perform(get("/api/v1/foundations")
                        .param("state", warehouse.getState())
                        .param("city", warehouse.getCity())
                        .header("Foundation-Id", foundation.getFoundationId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.district").value(warehouse.getDistrict()))
                .andExpect(jsonPath("$.address").value(warehouse.getLocation()))
                .andExpect(jsonPath("$.address").isNotEmpty())
                .andExpect(jsonPath("$.batches.length()").value(validInboundOrderRequest.getBatchStock().size() - 1))
                .andExpect(jsonPath("$.batches[0].batchNumber").value(validInboundOrderRequest.getBatchStock().get(0).getBatchNumber()));
    }

    @Test
    void getAvailable_returnNotFoundException_whenInvalidFoundation() throws Exception {
        inboundOrderService.create(validInboundOrderRequest, manager.getManagerId());

        mockMvc.perform(get("/api/v1/foundations")
                        .param("state", warehouse.getState())
                        .param("city", warehouse.getCity())
                        .header("Foundation-Id", foundation.getFoundationId() + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.name", containsString("Foundation not found")))
                .andExpect(jsonPath("$.message", containsString("There is no foundation with the specified id")));
    }

    @Test
    void create_returnDonation_whenValidRequest() throws Exception {
        inboundOrderService.create(validInboundOrderRequest, manager.getManagerId());
        BatchRequestDto batch = validInboundOrderRequest.getBatchStock().get(0);

        mockMvc.perform(post("/api/v1/foundations")
                        .param("batchNumber", String.valueOf(batch.getBatchNumber()))
                        .header("Foundation-Id", foundation.getFoundationId()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.donationId").isString())
                .andExpect(jsonPath("$.donationId").isNotEmpty())
                .andExpect(jsonPath("$.quantity").value(batch.getInitialQuantity()))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()));

        Batch foundBatch = batchService.findById(batch.getBatchNumber());
        assertEquals(0, foundBatch.getCurrentQuantity());
    }

    @Test
    void create_returnNotFoundException_whenInvalidFoundation() throws Exception {
        BatchRequestDto batch = validInboundOrderRequest.getBatchStock().get(0);
        inboundOrderService.create(validInboundOrderRequest, manager.getManagerId());

        mockMvc.perform(post("/api/v1/foundations")
                        .param("batchNumber", String.valueOf(batch.getBatchNumber()))
                        .header("Foundation-Id", foundation.getFoundationId() + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.name", containsString("Foundation not found")))
                .andExpect(jsonPath("$.message", containsString("There is no foundation with the specified id")));

        Batch foundBatch = batchService.findById(batch.getBatchNumber());
        assertEquals(batch.getInitialQuantity(), foundBatch.getCurrentQuantity());
    }

    @Test
    void create_returnBatchOutOfStockException_whenCurrentQuantityIsZero() throws Exception {
        BatchRequestDto batch = validInboundOrderRequest.getBatchStock().get(0);
        batch.setInitialQuantity(0);
        inboundOrderService.create(validInboundOrderRequest, manager.getManagerId());

        mockMvc.perform(post("/api/v1/foundations")
                        .param("batchNumber", String.valueOf(batch.getBatchNumber()))
                        .header("Foundation-Id", foundation.getFoundationId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("is out of stock")))
                .andExpect(jsonPath("$.message", containsString(String.valueOf(batch.getBatchNumber()))));

        Batch foundBatch = batchService.findById(batch.getBatchNumber());
        assertEquals(batch.getInitialQuantity(), foundBatch.getCurrentQuantity());
    }

    @Test
    void create_returnBadRequestException_whenInvalidDueDate() throws Exception {
        BatchRequestDto batch = validInboundOrderRequest.getBatchStock().get(0);
        batch.setDueDate(LocalDate.now().plusDays(30));
        inboundOrderService.create(validInboundOrderRequest, manager.getManagerId());

        mockMvc.perform(post("/api/v1/foundations")
                        .param("batchNumber", String.valueOf(batch.getBatchNumber()))
                        .header("Foundation-Id", foundation.getFoundationId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name", containsString("Bad request")))
                .andExpect(jsonPath("$.message", containsString("Produto indisponível para doação.")));

        Batch foundBatch = batchService.findById(batch.getBatchNumber());
        assertEquals(batch.getInitialQuantity(), foundBatch.getCurrentQuantity());
    }
}