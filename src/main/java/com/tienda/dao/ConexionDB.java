package com.tienda.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:3306/tiendadb";
    private static final String USER = "root";
    private static final String PASS = "contraseñadementira";

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL,USER,PASS);
    }

}
