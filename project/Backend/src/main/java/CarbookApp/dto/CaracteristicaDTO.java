package CarbookApp.dto;

public class CaracteristicaDTO {
    private Long id;
    private String titulo;
    private String icono;

    public CaracteristicaDTO() {
    }

    public CaracteristicaDTO(Long id, String titulo, String icono) {
        this.id = id;
        this.titulo = titulo;
        this.icono = icono;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getIcono() {
        return icono;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }
}
