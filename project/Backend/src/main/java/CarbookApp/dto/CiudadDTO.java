package CarbookApp.dto;

public class CiudadDTO {
    private Long id;
    private String titulo;
    private String pais;

    public CiudadDTO() {
    }

    public CiudadDTO(Long id,String titulo, String pais) {
        this.id = id;
        this.titulo = titulo;
        this.pais = pais;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
