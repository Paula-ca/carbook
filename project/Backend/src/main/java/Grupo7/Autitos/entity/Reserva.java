package Grupo7.Autitos.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalTime hora_comienzo;

    @Column
    private LocalDate fecha_inicial;

    @Column
    private LocalDate fecha_final;

    @Column
    private LocalDate borrado;

    @Column
    private Integer precio;

    @Column
    private String estado_pago;

    @Column
    private String estado;

    @Column
    private Long pago_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto")
    private Producto producto;

    //
    // @JsonIncludeProperties(value = "id")
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public Reserva() {
    }

    public Reserva(LocalTime hora_comienzo, LocalDate fecha_ingreso, LocalDate fecha_final, LocalDate borrado,Integer precio,String estado,String estado_pago,Long pago_id) {
        this.hora_comienzo = hora_comienzo;
        this.fecha_inicial = fecha_ingreso;
        this.fecha_final = fecha_final;
        this.borrado = borrado;
        this.precio= precio;
        this.estado = estado;
        this.estado_pago = estado_pago;
        this.pago_id = pago_id;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getHora_comienzo() {
        return hora_comienzo;
    }

    public void setHora_comienzo(LocalTime hora_comienzo) {
        this.hora_comienzo = hora_comienzo;
    }

    public LocalDate getFecha_ingreso() {
        return fecha_inicial;
    }

    public void setFecha_ingreso(LocalDate fecha_ingreso) {
        this.fecha_inicial = fecha_ingreso;
    }

    public LocalDate getFecha_final() {
        return fecha_final;
    }

    public void setFecha_final(LocalDate fecha_final) {
        this.fecha_final = fecha_final;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getBorrado() {
        return borrado;
    }

    public void setBorrado(LocalDate borrado) {
        this.borrado = borrado;
    }

    public Integer getPrecio(){return precio;}

    public void setPrecio(Integer precio){this.precio=precio;}

    public String getEstado() {return estado;}

    public void setEstado(String estado) {this.estado=estado;}

    public String getEstado_pago() {return estado_pago;}

    public void setEstado_pago(String estado_pago) {this.estado_pago=estado_pago;};

    public Long getPago_id() {return pago_id;};

    public void setPago_id(Long pago_id) {this.pago_id = pago_id;};

}
