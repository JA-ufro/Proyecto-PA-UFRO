package org.example.service;

import org.example.model.Cancion;
import org.example.model.Playlist;
import org.example.model.Usuario;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
@Service
public class DatabaseService {
    private Connection connection;
    private String user = "root";
    private String password = "rayen";
    private String url = "jdbc:mysql://localhost:3306/Frutify";
    private final List<Playlist> playlists = new ArrayList<>();
    private final List<Usuario> usuarios = new ArrayList<>();

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
        String sql = "SELECT id, nombre, contrasena FROM usuario WHERE nombre = ? AND contrasena = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            // Limpia espacios y convierte el nombre a minúsculas
            statement.setString(1, nombre);
            statement.setString(2, contrasena);

            // Depuración: imprime las credenciales ingresadas
            System.out.println("Credenciales ingresadas: Nombre - " + nombre + ", Contraseña - " + contrasena);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    String nombreUsuario = resultSet.getString("nombre");
                    String passwordUsuario = resultSet.getString("contrasena");
                    System.out.println("Inicio de sesión exitoso para usuario: " + nombreUsuario);
                    return new Usuario(id, nombreUsuario, passwordUsuario); // Devuelve el usuario con ID
                } else {
                    System.out.println("Usuario o contraseña incorrectos");
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

    public List<Playlist> obtenerPlaylists(Usuario usuario) {
        List<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT id, nombre FROM playlist WHERE usuario_id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setLong(1, usuario.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Playlist playlist = new Playlist();
                    playlist.setId(resultSet.getLong("id"));
                    playlist.setNombre(resultSet.getString("nombre"));
                    playlists.add(playlist);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playlists;
    }


    public Playlist obtenerPlaylistPorId(Long id) {
        for (Playlist playlist : playlists) {
            if (playlist.getId().equals(id)) {
                return playlist;
            }
        }
        return null;
    }

    public void eliminarPlaylist(Long id) {
        playlists.removeIf(playlist -> playlist.getId().equals(id));
    }

    public Usuario obtenerUsuarioPorId(Long id) {
        String sql = "SELECT id, nombre, contrasena FROM usuario WHERE id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(resultSet.getLong("id"));
                    usuario.setNombre(resultSet.getString("nombre"));
                    usuario.setContrasena(resultSet.getString("contrasena"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Devuelve null si no se encuentra
    }


    /**
     * Ordena las canciones de todas las playlists de un usuario alfabéticamente.
     *
     * @param usuario El usuario cuyas playlists serán ordenadas.
     */
    public void ordenarPlaylists(Usuario usuario) {
        for (Playlist playlist : playlists) {
            if (playlist != null && playlist.getCanciones() != null) {
                playlist.getCanciones().sort(Comparator.comparing(cancion -> cancion.getNombre().toLowerCase()));
            }
        }
        System.out.println("Playlists ordenadas alfabéticamente.");
    }

    public void agregarCancionAPlaylist(Long playlistId, String nombreCancion, String rutaArchivo) {
        Playlist playlist = obtenerPlaylistPorId(playlistId);
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist no encontrada");
        }

        Cancion nuevaCancion = new Cancion();
        nuevaCancion.setNombre(nombreCancion);
        nuevaCancion.setRutaArchivo(rutaArchivo);

        playlist.getCanciones().add(nuevaCancion);

        // Aquí debes guardar los cambios en la base de datos.
        try (Connection connection = getConnection()) {
            String insertCancionSql = "INSERT INTO cancion (nombre, ruta_archivo) VALUES (?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(insertCancionSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, nombreCancion);
                ps.setString(2, rutaArchivo);
                ps.executeUpdate();

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long cancionId = generatedKeys.getLong(1);

                        String insertRelacionSql = "INSERT INTO playlist_cancion (playlist_id, cancion_id) VALUES (?, ?)";
                        try (PreparedStatement psRelacion = connection.prepareStatement(insertRelacionSql)) {
                            psRelacion.setLong(1, playlistId);
                            psRelacion.setLong(2, cancionId);
                            psRelacion.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al agregar la canción a la playlist");
        }
}
}

