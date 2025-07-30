package CarbookApp.dto;


import java.util.List;

public class ProductoDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private String coordenadas;
    private String ubicacion;
    private int rating;
    private int precio;

    private CategoriaDTO categoria;
    private CiudadDTO ciudad;
    private List<ImagenDTO> imagenes;
    private List<CaracteristicaDTO> caracteristicas;
    private List<PoliticaDTO> politicas;

    public ProductoDTO() {
    }

    public ProductoDTO(Long id, String titulo, String descripcion, String coordenadas, String ubicacion, int rating, int precio, CategoriaDTO categoria, CiudadDTO ciudad, List<ImagenDTO> imagenes, List<CaracteristicaDTO> caracteristicas, List<PoliticaDTO> politicas) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.coordenadas = coordenadas;
        this.ubicacion = ubicacion;
        this.rating = rating;
        this.precio = precio;
        this.categoria = categoria;
        this.ciudad = ciudad;
        this.imagenes = imagenes;
        this.caracteristicas = caracteristicas;
        this.politicas = politicas;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public int getRating() {
        return rating;
    }

    public int getPrecio() {
        return precio;
    }

    public CategoriaDTO getCategoria() {
        return categoria;
    }

    public CiudadDTO getCiudad() {
        return ciudad;
    }

    public List<ImagenDTO> getImagenes() {
        return imagenes;
    }

    public List<CaracteristicaDTO> getCaracteristicas() {
        return caracteristicas;
    }

    public List<PoliticaDTO> getPoliticas() {
        return politicas;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setPoliticas(List<PoliticaDTO> politicas) {
        this.politicas = politicas;
    }

    public void setCaracteristicas(List<CaracteristicaDTO> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public void setImagenes(List<ImagenDTO> imagenes) {
        this.imagenes = imagenes;
    }

    public void setCiudad(CiudadDTO ciudad) {
        this.ciudad = ciudad;
    }

    public void setCategoria(CategoriaDTO categoria) {
        this.categoria = categoria;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
}
