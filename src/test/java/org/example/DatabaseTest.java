package org.example;

import org.example.model.Usuario;
import org.example.service.DatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private DatabaseService database;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests para iniciarSesion()
    @Test
    void iniciarSesion_CredencialesCorrectas_RetornaUsuario() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        Usuario usuario = database.iniciarSesion("usuario", "contrasena");

        assertNotNull(usuario);
        assertEquals("usuario", usuario.getNombre());
        verify(preparedStatement).setString(1, "usuario");
        verify(preparedStatement).setString(2, "contrasena");
    }

    @Test
    void iniciarSesion_CredencialesIncorrectas_RetornaNull() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Usuario usuario = database.iniciarSesion("usuario", "incorrecta");

        assertNull(usuario);
        verify(preparedStatement).setString(1, "usuario");
        verify(preparedStatement).setString(2, "incorrecta");
    }

    @Test
    void iniciarSesion_ExcepcionSQLException_LanzaError() throws SQLException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error SQL"));

        Usuario usuario = database.iniciarSesion("usuario", "contrasena");

        assertNull(usuario);
    }

}