package com.essenciadafruta.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.essenciadafruta.model.Polpa;
import com.essenciadafruta.model.dao.EstoqueDAO;
import com.essenciadafruta.model.dao.PolpaDAO;
import com.essenciadafruta.util.Database;

class VendasControllerTest {

    private final VendasController vendas = new VendasController();
    private final EstoqueDAO estoque = new EstoqueDAO();
    private int polpaId;

    @BeforeEach
    void prepararEstoque() throws Exception {
        try (Connection c = Database.getConnection(); Statement s = c.createStatement()) {
            s.execute("DELETE FROM saida");
            s.execute("DELETE FROM polpa");
        }
        LocalDate hoje = LocalDate.now();
        new PolpaDAO().inserir(new Polpa(0, "Maracujá", 500, "500g", 10, 10, 12.0, hoje, hoje.plusMonths(6), "L1"));
        polpaId = estoque.getPolpaId("Maracujá", "500g");
    }

    @Test
    void vendaBaixaOEstoqueERegistraASaida() throws Exception {
        vendas.registrarVenda("Ana", "Maracujá", "500g", 4, 12.0, null, null, "PIX", null);

        assertEquals(6, estoque.getQuantidadeDisponivel(polpaId));
        assertEquals(1, vendas.listarTodas().size());
    }

    @Test
    void vendaAcimaDoEstoqueNaoAlteraNada() throws Exception {
        assertThrows(IllegalArgumentException.class,
                () -> vendas.registrarVenda("Ana", "Maracujá", "500g", 11, 12.0, null, null, "PIX", null));

        assertEquals(10, estoque.getQuantidadeDisponivel(polpaId));
        assertEquals(0, vendas.listarTodas().size());
    }

    @Test
    void duasVendasNaoDeixamOEstoqueNegativo() throws Exception {
        vendas.registrarVenda("Ana", "Maracujá", "500g", 7, 12.0, null, null, "PIX", null);
        assertThrows(IllegalArgumentException.class,
                () -> vendas.registrarVenda("Bia", "Maracujá", "500g", 7, 12.0, null, null, "PIX", null));

        assertEquals(3, estoque.getQuantidadeDisponivel(polpaId));
        assertEquals(1, vendas.listarTodas().size());
    }
}
