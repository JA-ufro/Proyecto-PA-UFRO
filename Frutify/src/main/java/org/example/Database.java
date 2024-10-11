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

    /**
     * Establece una conexión con la base de datos utilizando los parámetros de conexión configurados.
     * Si la conexión se realiza con éxito, muestra un mensaje en la consola; de lo contrario, muestra un error.
     *
     * @throws SQLException si ocurre un error al intentar conectar con la base de datos.
     */
    public void conectar(){
        try{
            connection = DriverManager.getConnection(url,user,password);
            System.out.println("Conectado");
        } catch (SQLException e) {
            System.out.println("Error al conectar: "+e.getMessage());
        }
    }

    /**
     * Cierra la conexión con la base de datos si está abierta. Si la desconexión es exitosa,
     * muestra un mensaje en la consola; de lo contrario, muestra un error.
     *
     * @throws SQLException si ocurre un error al intentar cerrar la conexión.
     */
    public void desconectar(){
        if(connection != null){
            try{
                connection.close();
                System.out.println("Desconectado");
            } catch (SQLException e) {
                System.out.println("Error al desconectar: "+e.getMessage());
            }
        }
    }

    /**
     * Intenta iniciar sesión con el nombre de usuario y la contraseña proporcionados.
     *
     * @param nombre El nombre del usuario.
     * @param contrasena La contraseña del usuario.
     * @return Un objeto {@code Usuario} si las credenciales son correctas; {@code null} en caso contrario.
     * @throws SQLException si ocurre un error al intentar ejecutar la consulta de autenticación.
     */
    public Usuario iniciarSesion(String nombre, String contrasena){
        String sql = "Select nombre, contrasena from usuario where nombre = ? and contrasena = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,nombre);
            statement.setString(2,contrasena);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    System.out.println("Inicio Exitoso");
                    return new Usuario(nombre,contrasena);
                }else {
                    System.out.println("Usuario o contraseña incorrecto");
                }
            }
        }catch (SQLException e){
            System.out.println("Error al iniciar sesion: "+e.getMessage());
        }
        return null;
    }

    /**
     * Crea una nueva playlist asociada con el usuario dado.
     *
     * @param nombre El nombre de la playlist.
     * @param usuario El objeto {@code Usuario} que está creando la playlist.
     * @throws SQLException si ocurre un error al intentar insertar la nueva playlist en la base de datos.
     */
    public void crearPlaylist(String nombre, Usuario usuario){
        String sql = "Insert into playlist (nombre, usuario) values (?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            int usuario_id = getUsuarioID(usuario.getNombre());
            if(usuario_id == -1){
                statement.setString(1,nombre);
                statement.setInt(2,usuario_id);
                statement.executeUpdate();
                System.out.println("Playlist creada");
            }else {
                System.out.println("Usuario no encontrado");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el ID del usuario a partir del nombre de usuario.
     *
     * @param nombre El nombre del usuario.
     * @return El ID del usuario si se encuentra; {@code -1} si no se encuentra o si ocurre un error.
     * @throws SQLException si ocurre un error al intentar recuperar el ID del usuario.
     */
    private int getUsuarioID(String nombre){
        String sql = "Select id from usuario where nombre = ?";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,nombre);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt("id");
            }else{
                return -1;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
    }

}
