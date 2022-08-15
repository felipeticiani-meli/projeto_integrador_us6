package com.mercadolibre.bootcamp.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
public class Foundation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long foundationId;

    @Column(length = 50)
    private String username;

    @OneToMany(mappedBy = "foundation")
    private List<Donation> donation;
}
