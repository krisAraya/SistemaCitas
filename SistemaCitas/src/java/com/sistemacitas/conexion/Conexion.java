package com.sistemacitas.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = "jdbc:mysql://localhost:3306/sistema_citas";
    private static final String USER = "root";
    private static final String PASSWORD = "MySql@2026!"; // pon tu password si tienes

    public static Connection getConexion() {
        Connection con = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexión exitosa a la base de datos");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Error: Driver no encontrado");
        } catch (SQLException e) {
            System.out.println("❌ Error de conexión: " + e.getMessage());
        }

        return con;
    }
}
