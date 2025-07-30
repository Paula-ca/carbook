package CarbookApp.dto;

public class ImagenDTO {
    private Long id;
    private String url;
    private String titulo;

    public ImagenDTO() {
    }

    public ImagenDTO(Long id, String url, String titulo) {
        this.id = id;
        this.url = url;
        this.titulo = titulo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
