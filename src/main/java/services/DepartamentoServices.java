package services;

import controllers.ReadXMLDOM;
import models.Departamento;
import models.Empleado;
import myLib.UserMethods;
import repositories.EmpresaRepository;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class DepartamentoServices {

    // Repositorio
    EmpresaRepository empresaRepository = new EmpresaRepository();
    // Ruta
    private final Path depPath = Path.of("src/main/resources", "departamentos.xml");

    // TODO ¿Void, lista, String? (HACER ESTO REUTILIZABLE PARA EL EXÁMEN)
    public List<Departamento> loadDepartamentosXml() {
        UserMethods userMethods = new UserMethods();
        ReadXMLDOM readDepartamentoXML = new ReadXMLDOM();

        List<Departamento> departamentos = null;

        try {
            departamentos = readDepartamentoXML.readDepartamentoXML(depPath);
            for (Departamento dep: departamentos) {
                empresaRepository.addDepartamento(dep); // Añado dep a la lista del repositorio
            }
        } catch (IOException e) {
            userMethods.myPrinter("Something went wrong reading the file.");
        } catch (SAXException e) {
            userMethods.myPrinter("The xml file does not match the requirements.");
        }
        return departamentos;
    }

    public void assignDepartment(List<Empleado> empleados, List<Departamento> departamentos) {
        UserMethods userMethods = new UserMethods();
        EmpleadoServices empleadoServices = new EmpleadoServices();

        // Empleados procesados
        for (Empleado empleado : empleados) {

            // 1. Empleados que ya traen idDepartamento en el CSV pero no el objeto Departamento
            if (empleado.getIdDep() != null && empleado.getDepartamento() == null) {
                for (Departamento departamento : departamentos) {
                    if (empleado.getIdDep().equals(departamento.getId())) {
                        empleado.setDepartamento(departamento);
                        empresaRepository.addEmpleado(empleado);
                        break; // Salimos del for de departamentos al encontrar la coincidencia
                    }
                }
            }

            // 2. Empleados sin idDepartamento ni objeto Departamento asignado
            if (empleado.getDepartamento() == null) {
                userMethods.myPrinter("\nEmpleado: " + empleado.getNombre());
                boolean depFound = false;

                while (!depFound) {
                    String depToAssig = userMethods.requestString("Digite el departamento a asignar: ");

                    for (Departamento departamento : departamentos) {
                        if (departamento.getNombre() != null && departamento.getNombre().equalsIgnoreCase(depToAssig.trim())) {
                            empleado.setDepartamento(departamento);
                            userMethods.myPrinter("Departamento asignado correctamente.");
                            depFound = true;
                            empresaRepository.addEmpleado(empleado);
                            break; // Salimos del for de departamentos
                        }
                    }

                    // El aviso de error va FUERA del for, únicamente si no se encontró coincidencia
                    if (!depFound) {
                        userMethods.myPrinter("Departamento no existente, intenta otra vez.");
                    }
                }
            }
        }

        // 3. Sobrescribimos empleados.csv UNA SOLA VEZ al terminar de procesar todos los empleados
        empleadoServices.overwriteEmpleadosCsv(empleados);
    }

    public Departamento findDepartmentByName(String nombre, List<Departamento> departamentos) {
        Departamento departamento = null;

        for (Departamento dep : departamentos) {
            if (dep.getNombre() != null && dep.getNombre().trim().equalsIgnoreCase(nombre.trim())) {
                departamento = dep;
            }
        }
        return departamento;
    }
}

