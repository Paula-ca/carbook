package CarbookApp.dto;

import java.time.LocalDate;

public class ReservaDTO {
    private Long id;
    private LocalDate borrado;
    private String estado;
    private ProductoDTO producto;
    private LocalDate fechaIngreso;
    private LocalDate fechaFinal;
    private UsuarioDTO usuario;

    public ReservaDTO() {
    }

    public ReservaDTO(Long id, LocalDate borrado, String estado, ProductoDTO producto, LocalDate fechaIngreso, LocalDate fechaFinal, UsuarioDTO usuario) {
        this.id = id;
        this.borrado = borrado;
        this.estado = estado;
        this.producto = producto;
        this.fechaIngreso = fechaIngreso;
        this.fechaFinal = fechaFinal;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getBorrado() {
        return borrado;
    }

    public void setBorrado(LocalDate borrado) {
        this.borrado = borrado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(LocalDate fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }
}
