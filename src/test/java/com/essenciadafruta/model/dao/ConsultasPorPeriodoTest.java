package com.essenciadafruta.model.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.essenciadafruta.model.Custo;
import com.essenciadafruta.model.Polpa;
import com.essenciadafruta.model.Saida;
import com.essenciadafruta.util.Database;

/** Garante que as consultas por período funcionam no H2 (e não só no SQLite). */
class ConsultasPorPeriodoTest {

    private final PolpaDAO polpaDAO = new PolpaDAO();
    private final EstoqueDAO estoqueDAO = new EstoqueDAO();
    private final SaidaDAO saidaDAO = new SaidaDAO();
    private final CustoDAO custoDAO = new CustoDAO();

    private static final LocalDate HOJE = LocalDate.now();
    private static final LocalDate MES_PASSADO = HOJE.withDayOfMonth(1).minusDays(1);
    private static final LocalDate ANO_PASSADO = HOJE.minusYears(1).withDayOfMonth(1);

    private int polpaId;

    @BeforeEach
    void limparBanco() throws Exception {
        try (Connection c = Database.getConnection(); Statement s = c.createStatement()) {
            s.execute("DELETE FROM saida");
            s.execute("DELETE FROM polpa");
            s.execute("DELETE FROM custo");
        }
        polpaDAO.inserir(polpa("L-HOJE", HOJE));
        polpaDAO.inserir(polpa("L-MES-PASSADO", MES_PASSADO));
        polpaDAO.inserir(polpa("L-ANO-PASSADO", ANO_PASSADO));
        polpaId = estoqueDAO.getPolpaId("Morango", "500g");

        saidaDAO.inserir(saida(HOJE, 2, 10.0));
        saidaDAO.inserir(saida(MES_PASSADO, 1, 10.0));
        saidaDAO.inserir(saida(ANO_PASSADO, 1, 10.0));

        custoDAO.inserir(new Custo("Luz", "conta", 100, HOJE));
        custoDAO.inserir(new Custo("Luz", "conta", 50, MES_PASSADO));
        custoDAO.inserir(new Custo("Luz", "conta", 70, ANO_PASSADO));
    }

    @Test
    void polpaPorSemanaMesEAno() throws Exception {
        assertEquals(1, polpaDAO.listarPorSemana(HOJE).size());
        assertEquals(1, polpaDAO.listarPorMes(HOJE.getYear(), HOJE.getMonthValue()).size());
        assertEquals(1, polpaDAO.listarPorAno(ANO_PASSADO.getYear()).size());
    }

    @Test
    void estoquePorDiaSemanaMesEAno() throws Exception {
        assertEquals(1, estoqueDAO.listarPorDia(HOJE).size());
        assertEquals(1, estoqueDAO.listarPorSemana(HOJE).size());
        assertEquals(1, estoqueDAO.listarPorMes(HOJE.getYear(), HOJE.getMonthValue()).size());
        assertEquals(1, estoqueDAO.listarPorAno(ANO_PASSADO.getYear()).size());
    }

    @Test
    void vendasPorSemanaMesEAno() throws Exception {
        assertEquals(1, saidaDAO.listarPorSemana(HOJE).size());
        assertEquals(1, saidaDAO.listarPorMes(HOJE.getYear(), HOJE.getMonthValue()).size());
        assertEquals(1, saidaDAO.listarPorMes(MES_PASSADO.getYear(), MES_PASSADO.getMonthValue()).size());
        assertEquals(1, saidaDAO.listarPorAno(ANO_PASSADO.getYear()).size());
    }

    @Test
    void totalDeVendasDoMesAtualIgnoraOutrosMeses() throws Exception {
        assertEquals(20.0, saidaDAO.getTotalVendasMes(), 0.001);
    }

    @Test
    void custosPorMesEAno() throws Exception {
        assertEquals(1, custoDAO.listarPorMes(HOJE.getYear(), HOJE.getMonthValue()).size());
        assertEquals(1, custoDAO.listarMesAnterior().size());
        assertEquals(1, custoDAO.listarPorAno(ANO_PASSADO.getYear()).size());
    }

    private Polpa polpa(String lote, LocalDate producao) {
        return new Polpa(0, "Morango", 500, "500g", 10, 10, 12.0, producao, producao.plusMonths(6), lote);
    }

    private Saida saida(LocalDate data, int qtd, double preco) {
        return new Saida(polpaId, "Morango", "500g", qtd, preco, null, null, null, data, "PIX", "Cliente");
    }
}
