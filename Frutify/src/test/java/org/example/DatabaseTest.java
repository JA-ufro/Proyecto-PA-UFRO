package org.example;

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
    private Database database;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para iniciarSesion()
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

    // Test para crearPlaylist() cuando el usuario existe
    @Test
    void crearPlaylist_UsuarioExiste_CreaPlaylist() throws SQLException {
        Usuario usuario = new Usuario("usuario", "contrasena");

        // Mock de getUsuarioID
        when(connection.prepareStatement("Select id from usuario where nombre = ?")).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);

        when(connection.prepareStatement("Insert into playlist (nombre, usuario) values (?,?)")).thenReturn(preparedStatement);

        database.crearPlaylist("Mi Playlist", usuario);

        verify(preparedStatement).setString(1, "Mi Playlist");
        verify(preparedStatement).setInt(2, 1);
        verify(preparedStatement).executeUpdate();
    }

    // Test para excepciones SQL en crearPlaylist()
    @Test
    void crearPlaylist_ExcepcionSQLException_LanzaError() throws SQLException {
        Usuario usuario = new Usuario("usuario", "contrasena");
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error SQL"));

        database.crearPlaylist("Mi Playlist", usuario);

        verify(preparedStatement, never()).executeUpdate();
    }
}