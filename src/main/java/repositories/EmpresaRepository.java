package repositories;

import models.Departamento;
import models.Empleado;

import java.util.ArrayList;
import java.util.List;

public class EmpresaRepository {

    private static List<Empleado> listaEmpleados = new ArrayList<>();
    private static List<Departamento> listaDepartamentos = new ArrayList<>();

    // Getters
    public static List<Empleado> getListaEmpleados() {return listaEmpleados;}
    public static List<Departamento> getListaDepartamentos() {return listaDepartamentos;}

    // Setter
    public static void setListaEmpleados(List<Empleado> empleados) {listaEmpleados = empleados;}
    public static void setListaDepartamentos(List<Departamento> departamentos) {listaDepartamentos = departamentos;}

    // Funciones de añadido
    public static void addEmpleado(Empleado emp) {listaEmpleados.add(emp);}
    public static void addDepartamento(Departamento dep) {listaDepartamentos.add(dep);}
}