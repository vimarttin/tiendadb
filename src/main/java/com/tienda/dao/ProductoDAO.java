package com.tienda.dao;

import com.tienda.model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductoDAO {

    public void insertar(Producto p) throws SQLException {
        String sql = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";
        try (Connection c = ConexionDB.getConexion();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setInt(3, p.getStock());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setIdProducto(rs.getInt(1));
                }
            }
        }
    }

    public Producto buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM productos WHERE id_producto = ?";
        try (Connection c = ConexionDB.getConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getInt("id_producto"),
                            rs.getString("nombre"),
                            rs.getDouble("precio"),
                            rs.getInt("stock"));
                }
            }
        }
        return null;
    }

    public boolean actualizarStock(int id, int nuevoStock) throws SQLException {
        String sql = "UPDATE productos SET stock = ? WHERE id_producto = ?";
        try (Connection c = ConexionDB.getConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, nuevoStock);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }
}

