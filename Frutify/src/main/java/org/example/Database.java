package org.example;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

public class Database {
    private HashMap<String,Usuario> usuarios;
    private HashMap<Usuario, List<Playlist>> playlists;
    private Connection connection;
    private String user = "usuario";
    private String password = "contrasena";
    private String url = "jdbc:mysql://localhost:3306/Frutify";

    public void conectar(){
        try{
            connection = DriverManager.getConnection(url,user,password);
            System.out.println("Conectado");
        } catch (SQLException e) {
            System.out.println("Error al conectar: "+e.getMessage());
        }
    }

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
