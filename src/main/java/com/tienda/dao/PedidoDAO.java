package com.tienda.dao;

import com.tienda.model.Pedido;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public void insertar(Pedido p) throws SQLException {
        String sqlPedido = "INSERT INTO pedidos (fecha_pedido, cantidad, id_producto) VALUES (?, ?, ?)";
        String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id_producto = ? AND stock >= ?";

        try (Connection c = ConexionDB.getConexion()) {
            c.setAutoCommit(false);
            try (PreparedStatement ps1 = c.prepareStatement(sqlPedido);
                 PreparedStatement ps2 = c.prepareStatement(sqlStock)) {

                ps1.setDate(1, Date.valueOf(p.getFechaPedido()));
                ps1.setInt(2, p.getCantidad());
                ps1.setInt(3, p.getIdProducto());
                ps1.executeUpdate();

                ps2.setInt(1, p.getCantidad());
                ps2.setInt(2, p.getIdProducto());
                ps2.setInt(3, p.getCantidad());
                if (ps2.executeUpdate() == 0) {
                    throw new SQLException("Stock insuficiente");
                }

                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    public List<Pedido> listar() throws SQLException {
        String sql = "SELECT p.id_pedido, p.fecha_pedido, p.cantidad, p.id_producto, pr.nombre "
                + "FROM pedidos p JOIN productos pr ON p.id_producto = pr.id_producto "
                + "ORDER BY p.id_pedido";
        List<Pedido> lista = new ArrayList<>();
        try (Connection c = ConexionDB.getConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Pedido(
                        rs.getInt("id_pedido"),
                        rs.getDate("fecha_pedido").toLocalDate(),
                        rs.getInt("cantidad"),
                        rs.getInt("id_producto"),
                        rs.getString("nombre")));
            }
        }
        return lista;
    }
}
