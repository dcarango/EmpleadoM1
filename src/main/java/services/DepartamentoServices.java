package services;

import controllers.ReadXMLDOM;
import models.Departamento;
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
            EmpresaRepository.setListaDepartamentos(departamentos);
        } catch (IOException e) {
            userMethods.myPrinter("Something went wrong reading the file.");
        } catch (SAXException e) {
            userMethods.myPrinter("The xml file does not match the requirements.");
        }
        return departamentos;
    }

    // Busca un departamento por su nombre
    public Departamento findDepartmentByName(List<Departamento> departamentos, String nameDept) {
        Departamento departamento = null;

        for (Departamento dep : departamentos) {
            if (dep.getNombre() != null && dep.getNombre().trim().equalsIgnoreCase(nameDept.trim())) {
                departamento = dep;
            }
        }
        return departamento;
    }

    // TODO
    public String assignDepartments() {
        String message = "Feedback: \n";
        boolean found = false;

        // Accedemos directamente a la lista estática del repositorio
        for (Departamento dep : EmpresaRepository.getListaDepartamentos()) {
            if (dep.getId().equals(idDep)) {
                empleado.setIdDep(idDep);
                empleado.setDepartamento(dep); // Si usas el DTO o el objeto anidado
                found = true;
                message += "Departamento '" + dep.getNombre() + "' asignado con éxito a " + empleado.getNombre() + ".\n";
            }
        }

        if (!found) {
            message += "Error: No existe ningún departamento con el ID " + idDep + ".\n";
        }

        return message;
    }

    // TODO AISGNAR DEPARTAMENTOS DEBE RECIBIR LAS DOS LISTAS!!!! ¿¿ Qué devuelvo?
    // Comprobaciones ya hechas: Listas vacias
    public
}

