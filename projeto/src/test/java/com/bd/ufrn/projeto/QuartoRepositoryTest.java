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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuartoRepositoryTest {

    @Mock
    private ConnectionFactory connectionFactory;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @InjectMocks
    private QuartoRepository quartoRepository;

    @BeforeEach
    void setUp() throws SQLException {
        when(connectionFactory.connection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        // Removed: when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        // This will now be set up in individual test methods where executeQuery is called.
    }

    @Test
    void findById_shouldReturnQuarto_whenFound() throws SQLException {
        // Arrange
        int quartoNumero = 101;
        Quarto expectedQuarto = Quarto.builder()
                .numero(quartoNumero)
                .naoPerturbe(false)
                .ocupado(true)
                .marcadoPraLimpeza(false)
                .tipo(TipoQuarto.SIMPLES)
                .build();

        // Specific stubbing for this test: When executeQuery is called, return mockResultSet
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // Configure mockResultSet behavior for a single result
        when(mockResultSet.next()).thenReturn(true); // Only expect it once for 'if' condition
        when(mockResultSet.getInt("numero")).thenReturn(expectedQuarto.getNumero());
        when(mockResultSet.getBoolean("nao_perturbe")).thenReturn(expectedQuarto.isNaoPerturbe());
        when(mockResultSet.getBoolean("ocupado")).thenReturn(expectedQuarto.isOcupado());
        when(mockResultSet.getBoolean("marcado_para_limpeza")).thenReturn(expectedQuarto.isMarcadoPraLimpeza());
        when(mockResultSet.getString("tipo")).thenReturn(expectedQuarto.getTipo().name());

        // Act
        Quarto actualQuarto = quartoRepository.findById(quartoNumero);

        // Assert
        assertNotNull(actualQuarto);
        assertEquals(expectedQuarto.getNumero(), actualQuarto.getNumero());
        assertEquals(expectedQuarto.isOcupado(), actualQuarto.isOcupado());
        assertEquals(expectedQuarto.getTipo(), actualQuarto.getTipo());

        // Verify that specific JDBC methods were called
        verify(mockConnection).prepareStatement("SELECT * FROM quarto WHERE numero = ?");
        verify(mockPreparedStatement).setInt(1, quartoNumero);
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet, times(1)).next(); // Verified only once
        verify(mockResultSet).getInt("numero");
        verify(mockResultSet).getString("tipo");
    }

    @Test
    void findById_shouldReturnNull_whenNotFound() throws SQLException {
        // Arrange
        int quartoNumero = 999;

        // Specific stubbing for this test
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // Configure mockResultSet to indicate no results
        when(mockResultSet.next()).thenReturn(false); // No rows found

        // Act
        Quarto actualQuarto = quartoRepository.findById(quartoNumero);

        // Assert
        assertNull(actualQuarto);

        // Verify that specific JDBC methods were called
        verify(mockConnection).prepareStatement("SELECT * FROM quarto WHERE numero = ?");
        verify(mockPreparedStatement).setInt(1, quartoNumero);
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet).next(); // Only called once to check for the first row
        // Removed: verifyNoMoreInteractions(mockResultSet); // This was causing the error
    }

    @Test
    void save_shouldExecuteUpdate() throws SQLException {
        // Arrange
        Quarto newQuarto = Quarto.builder()
                .numero(201)
                .naoPerturbe(false)
                .ocupado(false)
                .marcadoPraLimpeza(false)
                .tipo(TipoQuarto.SUITE)
                .build();

        // Configure mockPreparedStatement.executeUpdate() - no executeQuery here
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Act
        quartoRepository.save(newQuarto);

        // Assert
        verify(mockConnection).prepareStatement("INSERT INTO quarto (numero, nao_perturbe, ocupado, marcado_para_limpeza, tipo) VALUES (?, ?, ?, ?, ?)");
        verify(mockPreparedStatement).setInt(1, newQuarto.getNumero());
        verify(mockPreparedStatement).setBoolean(2, newQuarto.isNaoPerturbe());
        verify(mockPreparedStatement).setBoolean(3, newQuarto.isOcupado());
        verify(mockPreparedStatement).setBoolean(4, newQuarto.isMarcadoPraLimpeza());
        verify(mockPreparedStatement).setString(5, newQuarto.getTipo().toString());
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void findAll_shouldReturnListOfQuartos() throws SQLException {
        // Arrange
        Quarto quarto1 = Quarto.builder().numero(101).naoPerturbe(false).ocupado(true).marcadoPraLimpeza(false).tipo(TipoQuarto.SIMPLES).build();
        Quarto quarto2 = Quarto.builder().numero(102).naoPerturbe(true).ocupado(false).marcadoPraLimpeza(true).tipo(TipoQuarto.SUITE).build();

        // Specific stubbing for this test
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // Configure mockResultSet for multiple results
        when(mockResultSet.next()).thenReturn(true, true, false); // Two rows, then end
        when(mockResultSet.getInt("numero"))
                .thenReturn(quarto1.getNumero())
                .thenReturn(quarto2.getNumero());
        when(mockResultSet.getBoolean("nao_perturbe"))
                .thenReturn(quarto1.isNaoPerturbe())
                .thenReturn(quarto2.isNaoPerturbe());
        when(mockResultSet.getBoolean("ocupado"))
                .thenReturn(quarto1.isOcupado())
                .thenReturn(quarto2.isOcupado());
        when(mockResultSet.getBoolean("marcado_para_limpeza"))
                .thenReturn(quarto1.isMarcadoPraLimpeza())
                .thenReturn(quarto2.isMarcadoPraLimpeza());
        when(mockResultSet.getString("tipo"))
                .thenReturn(quarto1.getTipo().name())
                .thenReturn(quarto2.getTipo().name());

        // Act
        List<Quarto> quartos = quartoRepository.findAll();

        // Assert
        assertNotNull(quartos);
        assertEquals(2, quartos.size());
        assertEquals(quarto1.getNumero(), quartos.get(0).getNumero());
        assertEquals(quarto2.getNumero(), quartos.get(1).getNumero());

        // Verify interactions
        verify(mockConnection).prepareStatement("SELECT * FROM quarto");
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet, times(3)).next(); // Called for each row + one more to check for end
        verify(mockResultSet, times(2)).getInt("numero");
        verify(mockResultSet, times(2)).getString("tipo");
        // Add more specific verifications for boolean values if desired
    }

    @Test
    void findByOcupado_shouldReturnOccupiedQuartos() throws SQLException {
        // Arrange
        Quarto occupiedQuarto = Quarto.builder().numero(101).naoPerturbe(false).ocupado(true).marcadoPraLimpeza(false).tipo(TipoQuarto.SIMPLES).build();

        // Specific stubbing for this test
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, false); // One occupied quarto, then no more
        when(mockResultSet.getInt("numero")).thenReturn(occupiedQuarto.getNumero());
        when(mockResultSet.getBoolean("nao_perturbe")).thenReturn(occupiedQuarto.isNaoPerturbe());
        when(mockResultSet.getBoolean("ocupado")).thenReturn(occupiedQuarto.isOcupado());
        when(mockResultSet.getBoolean("marcado_para_limpeza")).thenReturn(occupiedQuarto.isMarcadoPraLimpeza());
        when(mockResultSet.getString("tipo")).thenReturn(occupiedQuarto.getTipo().name());

        // Act
        List<Quarto> occupiedQuartos = quartoRepository.findByOcupado();

        // Assert
        assertNotNull(occupiedQuartos);
        assertEquals(1, occupiedQuartos.size());
        assertTrue(occupiedQuartos.get(0).isOcupado());

        // Verify
        verify(mockConnection).prepareStatement("SELECT * FROM quarto WHERE ocupado = ?");
        verify(mockPreparedStatement).setBoolean(1, true);
        verify(mockPreparedStatement).executeQuery();
        verify(mockResultSet, times(2)).next(); // Called twice: once for the row, once to check for the next (which is false)
    }

    @Test
    void delete_shouldExecuteUpdate() throws SQLException {
        // Arrange
        Quarto quartoToDelete = Quarto.builder().numero(101).build();

        // No executeQuery here, only executeUpdate
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        // Act
        quartoRepository.delete(quartoToDelete);

        // Assert
        verify(mockConnection).prepareStatement("DELETE FROM quarto WHERE numero = ?");
        verify(mockPreparedStatement).setInt(1, quartoToDelete.getNumero());
        verify(mockPreparedStatement).executeUpdate();
    }
}