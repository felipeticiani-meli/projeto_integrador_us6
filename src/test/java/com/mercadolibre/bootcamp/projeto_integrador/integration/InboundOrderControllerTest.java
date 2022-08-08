package com.mercadolibre.bootcamp.projeto_integrador.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.bootcamp.projeto_integrador.dto.BatchRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.dto.InboundOrderRequestDto;
import com.mercadolibre.bootcamp.projeto_integrador.model.Manager;
import com.mercadolibre.bootcamp.projeto_integrador.model.Product;
import com.mercadolibre.bootcamp.projeto_integrador.model.Section;
import com.mercadolibre.bootcamp.projeto_integrador.model.Warehouse;
import com.mercadolibre.bootcamp.projeto_integrador.repository.IManagerRepository;
import com.mercadolibre.bootcamp.projeto_integrador.repository.IProductRepository;
import com.mercadolibre.bootcamp.projeto_integrador.repository.ISectionRepository;
import com.mercadolibre.bootcamp.projeto_integrador.repository.IWarehouseRepository;
import com.mercadolibre.bootcamp.projeto_integrador.util.GeneratorInboundOrderAndBatch;
import com.mercadolibre.bootcamp.projeto_integrador.util.GeneratorProducts;
import com.mercadolibre.bootcamp.projeto_integrador.util.GeneratorSection;
import com.mercadolibre.bootcamp.projeto_integrador.util.GeneratorWarehouseAndManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class InboundOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ISectionRepository sectionRepository;

    @Autowired
    private IWarehouseRepository warehouseRepository;

    @Autowired
    private IManagerRepository managerRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private DataSource dataSource;

    private final ObjectMapper objectMapper;
    private Warehouse warehouse = GeneratorWarehouseAndManager.newWarehouse();
    private Manager manager = GeneratorWarehouseAndManager.newManager();
    private Section section = GeneratorSection.getSection(warehouse, manager);
    private Product product = GeneratorProducts.newProductFresh();

    public InboundOrderControllerTest() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @BeforeAll
    void populateTheDatabase() {
        warehouseRepository.save(warehouse);
        managerRepository.save(manager);
        sectionRepository.save(section);
        productRepository.save(product);
    }

    @BeforeEach
    void cleanDatabase() throws SQLException {
        try (
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()
        ) {
            statement.execute("SET REFERENTIAL_INTEGRITY FALSE");
            ResultSet records = statement.executeQuery("SHOW TABLES");

            while (records.next()) {
                Statement truncateStatement = connection.createStatement();
                truncateStatement.executeUpdate("TRUNCATE TABLE " + records.getString(1));
                truncateStatement.close();
            }

            statement.execute("SET REFERENTIAL_INTEGRITY TRUE");
        }
    }

    @Test
    void createInboundOrder_returnsOk_whenIsGivenAValidInput() throws Exception {
        InboundOrderRequestDto requestDto = GeneratorInboundOrderAndBatch.newInboundRequestDTO();
        requestDto.getBatchStock().get(0).setProductId(product.getProductId());
        requestDto.setSectionCode(section.getSectionCode());

        mockMvc.perform(post("/api/v1/fresh-products/inboundorder")
                .content(asJsonString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void createInboundOrder_returnsError_whenIsGivenAnInvalidInput() throws Exception {
        System.out.println(manager.getManagerId());

        BatchRequestDto batchRequest = new BatchRequestDto();
        batchRequest.setProductId(product.getProductId());

        // Valores inválidos.
        batchRequest.setProductPrice(new BigDecimal("-100.99"));
        batchRequest.setCurrentTemperature(-1);
        batchRequest.setMinimumTemperature(-1);
        batchRequest.setDueDate(LocalDate.now().minusWeeks(1));
        batchRequest.setManufacturingTime(LocalDateTime.now().plusDays(1));
        batchRequest.setManufacturingDate(LocalDate.now().plusDays(1));
        batchRequest.setInitialQuantity(-1);

        InboundOrderRequestDto requestDto = new InboundOrderRequestDto();
        requestDto.setBatchStock(List.of(batchRequest));
        requestDto.setSectionCode(section.getSectionCode());

        mockMvc.perform(post("/api/v1/fresh-products/inboundorder")
                .content(asJsonString(requestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    private String asJsonString(final Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}
