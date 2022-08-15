package com.mercadolibre.bootcamp.projeto_integrador.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter @Setter
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long donationId;

    @Column
    private int quantity;

    @Column
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "foundationId")
    private Foundation foundation;

    @ManyToOne
    @JoinColumn(name = "batchNumber")
    private Batch batch;
}
