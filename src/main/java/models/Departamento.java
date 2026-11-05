package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "nombre", "localidad" })
public class Departamento {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("localidad")
    private String localidad;

    public Departamento() {}

    public Departamento(Integer id, String nombre, String localidad) {
        this.id = id;
        this.nombre = nombre;
        this.localidad = localidad;
    }

    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
    public String getLocalidad() { return localidad; }

    public void setId(Integer id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }
}