package com.tienda;

import com.tienda.dao.PedidoDAO;
import com.tienda.dao.ProductoDAO;
import com.tienda.model.Pedido;
import com.tienda.model.Producto;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final ProductoDAO productoDAO = new ProductoDAO();
    private static final PedidoDAO pedidoDAO = new PedidoDAO();

    public static void main(String[] args) {
        int opcion = 0;
        do {
            mostrarMenu();
            try {
                opcion = Integer.parseInt(sc.nextLine().trim());
                switch (opcion) {
                    case 1 -> insertarProducto();
                    case 2 -> consultarProducto();
                    case 3 -> actualizarStock();
                    case 4 -> insertarPedido();
                    case 5 -> listarPedidos();
                    case 6 -> System.out.println("Hasta luego.");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número válido.");
                opcion = 0;
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }
        } while (opcion != 6);
    }

    private static void mostrarMenu() {
        System.out.println("\n===== TIENDA =====");
        System.out.println("1. Insertar producto");
        System.out.println("2. Consultar producto por id");
        System.out.println("3. Actualizar stock de producto");
        System.out.println("4. Insertar pedido");
        System.out.println("5. Listado de pedidos");
        System.out.println("6. Salir");
        System.out.print("Elige una opción: ");
    }

    private static void insertarProducto() throws SQLException {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Precio: ");
        double precio = Double.parseDouble(sc.nextLine().replace(",", "."));
        System.out.print("Stock: ");
        int stock = Integer.parseInt(sc.nextLine());

        Producto p = new Producto(0, nombre, precio, stock);
        productoDAO.insertar(p);
        System.out.println("Producto insertado con id " + p.getIdProducto());
    }

    private static void consultarProducto() throws SQLException {
        System.out.print("Id del producto: ");
        int id = Integer.parseInt(sc.nextLine());
        Producto p = productoDAO.buscarPorId(id);
        if (p == null) {
            System.out.println("El producto no existe.");
        } else {
            System.out.println(p);
        }
    }

    private static void actualizarStock() throws SQLException {
        System.out.print("Id del producto: ");
        int id = Integer.parseInt(sc.nextLine());
        System.out.print("Nuevo stock: ");
        int stock = Integer.parseInt(sc.nextLine());
        if (productoDAO.actualizarStock(id, stock)) {
            System.out.println("Stock actualizado.");
        } else {
            System.out.println("El producto no existe.");
        }
    }

    private static void insertarPedido() throws SQLException {
        System.out.print("Id del producto: ");
        int id = Integer.parseInt(sc.nextLine());

        Producto producto = productoDAO.buscarPorId(id);
        if (producto == null) {
            System.out.println("El producto no existe.");
            return;
        }

        System.out.print("Cantidad: ");
        int cantidad = Integer.parseInt(sc.nextLine());

        try {
            pedidoDAO.insertar(new Pedido(0, LocalDate.now(), cantidad, id, null));
            Producto actualizado = productoDAO.buscarPorId(id);
            System.out.println("Pedido insertado. Stock de " + actualizado.getNombre()
                    + ": " + producto.getStock() + " -> " + actualizado.getStock());
        } catch (SQLException e) {
            if ("Stock insuficiente".equals(e.getMessage())) {
                System.out.println("No hay stock suficiente. Disponible: " + producto.getStock());
            } else {
                throw e;
            }
        }
    }

    private static void listarPedidos() throws SQLException {
        List<Pedido> pedidos = pedidoDAO.listar();
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos.");
            return;
        }
        for (Pedido p : pedidos) {
            System.out.println("Pedido " + p.getIdPedido()
                    + " | " + p.getFechaPedido()
                    + " | " + p.getNombreProducto()
                    + " | cantidad: " + p.getCantidad());
        }
    }
}
