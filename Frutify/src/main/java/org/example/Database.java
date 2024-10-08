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
    private String url = "jdbc:mysql://localhost:3306/Frunify";

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
        String sql = "Select * from usuario where nombre = ? and contrasena = ?";
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

}
