package com.mercadolibre.bootcamp.projeto_integrador.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long warehouseCode;

    @Column(length = 50)
    private String location;

    @Column
    private String city;
    @Column
    private String district;
    @Column(length = 2)
    private String state;
}
