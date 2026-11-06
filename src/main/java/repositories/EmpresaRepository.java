package repositories;

import models.Departamento;
import models.Empleado;

import java.util.ArrayList;
import java.util.List;

public class EmpresaRepository {

    // Se quita el 'static' de las listas
    private List<Empleado> listaEmpleados = new ArrayList<>();
    private List<Departamento> listaDepartamentos = new ArrayList<>();

    // Constructor implícito o explícito
    public EmpresaRepository() {}

    // Getters
    public List<Empleado> getListaEmpleados() { return listaEmpleados; }
    public List<Departamento> getListaDepartamentos() { return listaDepartamentos; }

    // Funciones
    public void addEmpleado(Empleado emp) { this.listaEmpleados.add(emp); }
    public void addDepartamento(Departamento dep) { this.listaDepartamentos.add(dep); }
}