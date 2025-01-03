package org.example;

import java.sql.*;

/**
 * La clase {@code Database} proporciona métodos para conectar y desconectar de una base de datos,
 * así como realizar operaciones relacionadas con la autenticación de usuarios y la creación de playlists.
 * Esta clase utiliza JDBC para interactuar con una base de datos MySQL.
 *
 * <p> El nombre de usuario, la contraseña y la URL de conexión están predefinidos para una base de datos específica,
 * pero pueden modificarse si es necesario. </p>
 *
 * @author JA-Ufro
 * @version 1.0
 */
public class Database {
    private Connection connection;
    private String user = "usuario";
    private String password = "contrasena";
    private String url = "jdbc:mysql://localhost:3306/Frutify";

    // Obtener conexión para uso externo
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    public void desconectar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Desconectado");
            }
        } catch (SQLException e) {
            System.out.println("Error al desconectar: " + e.getMessage());
        }
    }

    public Usuario iniciarSesion(String nombre, String contrasena) {
        String sql = "SELECT nombre, contrasena FROM usuario WHERE nombre = ? AND contrasena = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, nombre);
            statement.setString(2, contrasena);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Usuario(nombre, contrasena);
                } else {
                    System.out.println("Usuario o contraseña incorrecto");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al iniciar sesión: " + e.getMessage());
        }
        return null;
    }

    public void crearPlaylist(String nombre, Usuario usuario) {
        String sql = "INSERT INTO playlist (nombre, usuario_id) VALUES (?, ?)";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            int usuarioId = getUsuarioID(usuario.getNombre());
            if (usuarioId != -1) {
                statement.setString(1, nombre);
                statement.setInt(2, usuarioId);
                statement.executeUpdate();
                System.out.println("Playlist creada");
            } else {
                System.out.println("Usuario no encontrado");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getUsuarioID(String nombre) {
        String sql = "SELECT id FROM usuario WHERE nombre = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, nombre);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
