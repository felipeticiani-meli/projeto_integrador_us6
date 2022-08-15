package com.mercadolibre.bootcamp.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Donation {
    @Id
    @Type(type = "uuid-char")
    private UUID donationId;

    @Column
    private int quantity;

    @Column
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "foundationId")
    @JsonIgnore
    private Foundation foundation;

    @ManyToOne
    @JoinColumn(name = "batchNumber")
    @JsonIgnore
    private Batch batch;

    public Donation(UUID id, int quantity, LocalDate date, Foundation foundation, Batch batch) {
        this.donationId = id;
        this.quantity = quantity;
        this.date = date;
        this.foundation = foundation;
        this.batch = batch;
    }
}
