package models;

public class Empleado {
    private String nombre;
    private Double sueldo;
    private Integer año;
    private Integer idDep;
    private Integer antiguedad;

    public Empleado() {
    }

    // Getters
    public String getNombre() {return nombre;}
    public Double getSueldo() {return sueldo;}
    public Integer getAño() {return año;}
    public Integer getIdDep() {return idDep;}
    public Integer getAntiguedad() { return antiguedad; }

    // Setters
    public void setNombre(String nombre) {this.nombre = nombre;}
    public void setSueldo(Double sueldo) {this.sueldo = sueldo;}
    public void setAño(Integer año) {this.año = año;}
    public void setIdDep(Integer idDep) {this.idDep = idDep;}
    public void setAntiguedad(Integer antiguedad) { this.antiguedad = antiguedad; }

    // Función para formatear el empleado a una línea csv
    public String toCsv() {
        String line = nombre + ";" + sueldo + ";" + año + ";" + antiguedad;

        // toCsv sigue valiendo para cuando quiera reescribir con un empleado con idDep
        if (idDep != null) {
            line += ";" + idDep;
        }
        return line;
    }
}


