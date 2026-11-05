package models;

import com.fasterxml.jackson.annotation.*;

/**
 * Empleado -
 * Clase utilizada para crear "empleados.csv"
 * Ojo, no sirve para poder crear el "empresaDatos.json", ya que tiene una propiedad
 * idDep, y se pensó que sería mejor que tuviese una propiedad Departamento
 * para modelar mejor "empresaDatos.json"
 *
 * Actualización - Ahora sirve para crear "empresaDatos.json"
 */

// Orden en el que deberían aparecer las propiedades en JSON/XML
@JsonPropertyOrder({ "nombre", "sueldo", "año", "antiguedad", "departamento" })
// Evita que falle si el JSON de entrada contiene claves que no existen como atributos
@JsonIgnoreProperties(ignoreUnknown = true)
public class Empleado {

    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("sueldo")
    private Double sueldo;
    @JsonProperty("año")
    private Integer año;
    @JsonProperty("antiguedad")
    private Integer antiguedad;
    @JsonProperty("departamento")
    private Departamento departamento;

    // Constructor vacío
    public Empleado() {}

    // Getters
    public String getNombre() { return nombre; }
    public Double getSueldo() { return sueldo; }
    public Integer getAño() { return año; }
    public Departamento getDepartamento() { return departamento; }
    public Integer getAntiguedad() { return antiguedad; }

    // Oculta la propiedad idDep mientras se escribe el JSON (evita duplicar el objeto departamento)
    @JsonIgnore
    public Integer getIdDep() {
        Integer id = null;
        if (this.departamento != null) {
            id = this.departamento.getId();
        }
        return id;
    }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setSueldo(Double sueldo) { this.sueldo = sueldo; }
    public void setAño(Integer año) { this.año = año; }
    public void setDepartamento(Departamento departamento) { this.departamento = departamento; }
    public void setAntiguedad(Integer antiguedad) { this.antiguedad = antiguedad; }

    // Fuerza a Jackson a ejecutar este método cuando encuentre la clave "idDep" al leer JSON
    @JsonSetter("idDep")
    public void setIdDep(Integer idDep) {
        if (idDep != null) {
            if (this.departamento == null) {
                this.departamento = new Departamento();
            }
            this.departamento.setId(idDep);
        }
    }

    // Función para formatear el empleado a una línea CSV
    public String toCsv() {
        String idTexto;
        if (getIdDep() != null) {
            idTexto = String.valueOf(getIdDep());
        } else {
            idTexto = "";
        }
        return nombre + ";" + sueldo + ";" + año + ";" + antiguedad + ";" + idTexto;
    }
}