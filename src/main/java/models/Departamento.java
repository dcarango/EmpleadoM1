package models;

public class Departamento {
    private Integer id;
    private String nombre;
    private String localidad;

    public Departamento() {
    }

    // Getter
    public Integer getId() {return id;}
    public String getNombre() {return nombre;}
    public String getLocalidad() {return localidad;}

    // Setter
    public void setId(Integer id) {this.id = id;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setLocalidad(String localidad) {this.localidad = localidad;}
}
