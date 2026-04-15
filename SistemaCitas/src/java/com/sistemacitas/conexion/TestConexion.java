package com.sistemacitas.conexion;

import java.sql.Connection;

public class TestConexion {

    public static void main(String[] args) {
        Connection con = Conexion.getConexion();

        if (con != null) {
            System.out.println("🎉 Conectado correctamente");
        } else {
            System.out.println("💥 No se pudo conectar");
        }
    }
}