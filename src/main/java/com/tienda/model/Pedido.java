package com.tienda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    private int idPedido;
    private LocalDate fechaPedido;
    private int cantidad;
    private int idProducto;
    private String nombreProducto;

}
