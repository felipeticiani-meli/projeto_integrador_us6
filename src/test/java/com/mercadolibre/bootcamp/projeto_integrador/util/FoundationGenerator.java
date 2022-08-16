package com.mercadolibre.bootcamp.projeto_integrador.util;

import com.mercadolibre.bootcamp.projeto_integrador.model.Foundation;

public class FoundationGenerator {

    public static Foundation newFoundation() {
        Foundation foundation = new Foundation();
        foundation.setFoundationId(1);
        foundation.setUsername("nurrevi-sj");
        return foundation;
    }
}
