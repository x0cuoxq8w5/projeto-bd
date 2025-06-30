package com.bd.ufrn.projeto;

import com.bd.ufrn.projeto.enums.TipoQuarto;
import com.bd.ufrn.projeto.models.Quarto;
import com.bd.ufrn.projeto.repositories.QuartoRepository;
import com.bd.ufrn.projeto.services.ConnectionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuartoRepositoryTest {

    @Mock
    private ConnectionFactory connectionFactory;

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private QuartoRepository quartoRepository;

    private void setupConnectionMocks() throws SQLException {
        when(connectionFactory.connection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
    }

    @Test
    void testSave_Success() throws SQLException {
        setupConnectionMocks();
        Quarto quarto = Quarto.builder()
                .numero(101)
                .naoPerturbe(false)
                .ocupado(true)
                .marcadoPraLimpeza(false)
                .tipo(TipoQuarto.SIMPLES)
                .build();

        when(statement.executeUpdate(anyString())).thenReturn(1);

        quartoRepository.save(quarto);

        verify(connectionFactory).connection();
        verify(connection).createStatement();
        verify(statement).executeUpdate(
                "INSERT INTO quarto (numero, nao_perturbe, ocupado, marcado_para_limpeza, tipo) VALUES (101, false, true, false, 'SIMPLES')"
        );
        verify(statement).close();
        verify(connection).close();
    }

    @Test
    void testSave_WithAllBooleanFieldsTrue() throws SQLException {
        setupConnectionMocks();
        Quarto quarto = Quarto.builder()
                .numero(202)
                .naoPerturbe(true)
                .ocupado(true)
                .marcadoPraLimpeza(true)
                .tipo(TipoQuarto.SUITE)
                .build();

        when(statement.executeUpdate(anyString())).thenReturn(1);

        quartoRepository.save(quarto);

        verify(statement).executeUpdate(
                "INSERT INTO quarto (numero, nao_perturbe, ocupado, marcado_para_limpeza, tipo) VALUES (202, true, true, true, 'SUITE')"
        );
    }

    @Test
    void testSave_SQLException() throws SQLException {
        setupConnectionMocks();
        Quarto quarto = Quarto.builder()
                .numero(101)
                .naoPerturbe(false)
                .ocupado(true)
                .marcadoPraLimpeza(false)
                .tipo(TipoQuarto.SIMPLES)
                .build();

        when(statement.executeUpdate(anyString())).thenThrow(new SQLException("Database error"));

        assertDoesNotThrow(() -> quartoRepository.save(quarto));
        verify(statement).executeUpdate(anyString());
    }

    @Test
    void testFindById_Success() throws SQLException {
        setupConnectionMocks();
        Integer quartoId = 101;
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("numero")).thenReturn(101);
        when(resultSet.getBoolean("nao_perturbe")).thenReturn(false);
        when(resultSet.getBoolean("ocupado")).thenReturn(true);
        when(resultSet.getBoolean("marcado_pra_limpeza")).thenReturn(false);
        when(resultSet.getString("tipo")).thenReturn("SIMPLES");

        Quarto result = quartoRepository.findById(quartoId);

        assertNotNull(result);
        assertEquals(101, result.getNumero());
        assertFalse(result.isNaoPerturbe());
        assertTrue(result.isOcupado());
        assertFalse(result.isMarcadoPraLimpeza());
        assertEquals(TipoQuarto.SIMPLES, result.getTipo());

        verify(connectionFactory).connection();
        verify(connection).createStatement();
        verify(statement).executeQuery("SELECT * FROM quarto WHERE numero = 101");
        verify(statement).close();
        verify(connection).close();
    }

    @Test
    void testFindById_NotFound() throws SQLException {
        setupConnectionMocks();
        Integer quartoId = 999;
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Quarto result = quartoRepository.findById(quartoId);

        assertNull(result);
        verify(statement).executeQuery("SELECT * FROM quarto WHERE numero = 999");
        verify(statement).close();
        verify(connection).close();
    }

    @Test
    void testFindById_WithAllBooleanFieldsTrue() throws SQLException {
        setupConnectionMocks();
        Integer quartoId = 202;
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("numero")).thenReturn(202);
        when(resultSet.getBoolean("nao_perturbe")).thenReturn(true);
        when(resultSet.getBoolean("ocupado")).thenReturn(true);
        when(resultSet.getBoolean("marcado_pra_limpeza")).thenReturn(true);
        when(resultSet.getString("tipo")).thenReturn("SIMPLES");

        Quarto result = quartoRepository.findById(quartoId);

        assertNotNull(result);
        assertEquals(202, result.getNumero());
        assertTrue(result.isNaoPerturbe());
        assertTrue(result.isOcupado());
        assertTrue(result.isMarcadoPraLimpeza());
        assertEquals(TipoQuarto.SIMPLES, result.getTipo());
    }

    @Test
    void testFindById_SQLException() throws SQLException {
        setupConnectionMocks();
        Integer quartoId = 101;
        when(statement.executeQuery(anyString())).thenThrow(new SQLException("Database connection error"));

        Quarto result = quartoRepository.findById(quartoId);

        assertNull(result);
        verify(statement).executeQuery(anyString());
    }

    @Test
    void testFindById_ResultSetException() throws SQLException {
        setupConnectionMocks();
        Integer quartoId = 101;
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("numero")).thenThrow(new SQLException("Column not found"));

        Quarto result = quartoRepository.findById(quartoId);

        assertNull(result);
        verify(resultSet).getInt("numero");
    }

    @Test
    void testSave_WithDifferentTipoQuarto() throws SQLException {
        setupConnectionMocks();
        Quarto quartoSuite = Quarto.builder()
                .numero(301)
                .naoPerturbe(false)
                .ocupado(false)
                .marcadoPraLimpeza(true)
                .tipo(TipoQuarto.SUITE)
                .build();

        when(statement.executeUpdate(anyString())).thenReturn(1);

        quartoRepository.save(quartoSuite);

        verify(statement).executeUpdate(
                "INSERT INTO quarto (numero, nao_perturbe, ocupado, marcado_para_limpeza, tipo) VALUES (301, false, false, true, 'SUITE')"
        );
    }

    @Test
    void testConnect_Success() throws SQLException {
        when(connectionFactory.connection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);

        quartoRepository.connect();

        verify(connectionFactory).connection();
        verify(connection).createStatement();
    }

    @Test
    void testConnect_SQLException() throws SQLException {
        when(connectionFactory.connection()).thenThrow(new SQLException("Connection failed"));

        assertThrows(SQLException.class, () -> quartoRepository.connect());
    }

    @Test
    void testCloseConnection_Success() throws SQLException {
        when(connectionFactory.connection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        quartoRepository.connect();

        quartoRepository.closeConnection();

        verify(statement).close();
        verify(connection).close();
    }

    @Test
    void testCloseConnection_SQLException() throws SQLException {
        when(connectionFactory.connection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        quartoRepository.connect();
        doThrow(new SQLException("Close failed")).when(statement).close();

        assertDoesNotThrow(() -> quartoRepository.closeConnection());
        verify(statement).close();
    }

}