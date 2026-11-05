package services;

import controllers.ReadXMLDOM;
import models.Departamento;
import models.Empleado;
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

    // TODO ¿Void o lista?
    public List<Departamento> loadDepartamentosXml(Path path) throws IOException, SAXException {
        ReadXMLDOM readDepartamentoXML = new ReadXMLDOM();

        // Lista de departamentos
        List<Departamento> departamentos = readDepartamentoXML.readDepartamentoXML(path);
        if (departamentos != null) {
            EmpresaRepository.setListaDepartamentos(departamentos);
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
    public String assignDepartments(Empleado empleado, Integer idDep) {
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
}
}
