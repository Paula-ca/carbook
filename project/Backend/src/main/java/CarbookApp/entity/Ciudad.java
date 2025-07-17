package CarbookApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "ciudades")
public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String titulo;

    @Column
    private String pais;

    @OneToMany(mappedBy = "ciudad",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Producto> productos;

    public Ciudad(){

    }

    public Ciudad(String titulo, String pais) {
        this.titulo = titulo;
        this.pais = pais;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Set<Producto> getProductos() {
        return productos;
    }

    public void setProductos(Set<Producto> productos) {
        this.productos = productos;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
