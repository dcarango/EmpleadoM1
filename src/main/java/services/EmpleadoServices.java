package services;


import com.fasterxml.jackson.core.type.TypeReference;
import models.Empleado;
import models.EmpleadosWrapper;
import myLib.UserMethods;
import repositories.EmpresaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoServices {

    // Ruta
    private final Path empleadosCsvPath = Path.of("src/main/resources", "empleados.csv");
    private final Path nuevosEmpleadosjsonPath = Path.of("src/main/resources", "nuevosEmpleados.json");
    private final Path jsonPath = Path.of("src/main/resources", "datosEmpresa.json");
    private final Path xmlPath = Path.of("src/main/resources", "datosEmpresa.xml");

    // Repositorios
    EmpresaRepository empresaRepository = new EmpresaRepository();

    // Función que crea un empleado en "empleados.csv", utiliza la clase Empleado
    public String saveEmpleadoCsv(Empleado empleado) {
        String message = "Feedback: \n";
        boolean redFlag = false;

        // Recuperamos valores a manipular
        Integer año = empleado.getAño();
        Integer antiguedad = empleado.getAntiguedad();
        String nombre = empleado.getNombre();

        // Lista de empleados del csv para comprobaciones
        List<Empleado> empleados = readEmpleadosCsv();

        // Comprobación de fechas
        if (antiguedad != null && año != null && antiguedad < año) {
            message += "Wrong date: \n";
            redFlag = true;
        }

        // Control de duplicados por nombre
        if (nombre != null) {
            for (Empleado emp : empleados) {
                if (emp.getNombre() != null && emp.getNombre().trim().equalsIgnoreCase(nombre.trim())) {
                    message += "Employee already exists with name";
                    redFlag = true;
                    break; // Detenemos la búsqueda al encontrar la primera coincidencia
                }
            }
        }

        // Añade empleados al csv
        if (!redFlag) {
            try {
                String line = empleado.toCsv() + "\n";
                Files.writeString(empleadosCsvPath, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                message = "Empleado guardado correctamente.";

            } catch (IOException e) {
                message = "Error al escribir en el archivo CSV.";
            }
        }
        return message;
    }

    // Funcion que lee empleados.csv
    public List<Empleado> readEmpleadosCsv() {
        List<Empleado> lista = new ArrayList<>();

        if (Files.exists(empleadosCsvPath)) {
            try {
                List<String> lines = Files.readAllLines(empleadosCsvPath);
                for (String line : lines) {
                    if (!line.isBlank()) {
                        String[] parts = line.split(";");
                        Empleado empleado = new Empleado();
                        empleado.setNombre(parts[0]);
                        empleado.setSueldo(Double.valueOf(parts[1]));
                        empleado.setAño(Integer.valueOf(parts[2]));
                        empleado.setAntiguedad(Integer.valueOf(parts[3]));

                        // En caso de ya contar con un idDepartamento
                        if (parts.length > 4) {
                            empleado.setIdDep(Integer.valueOf(parts[4]));
                        }
                        lista.add(empleado);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error al leer empleados.csv: " + e.getMessage());
            }
        }
        return lista;
    }

    // Funcion que lee nuevosEmpleados.json
    public List<Empleado> readEmpleadosJson() {
        UserMethods userMethods = new UserMethods();
        ObjectMapper mapper = new ObjectMapper();
        int currentYear = Year.now().getValue(); // Año actual

        List<Empleado> empleados = null;

        try {
            empleados = mapper.readValue(nuevosEmpleadosjsonPath.toFile(), new  TypeReference<List<Empleado>>(){});

            // Empleados vienen sin fecha de antigueadad, se hace un for para añadrila.
            for (Empleado empleado : empleados) {
                if (empleado.getAntiguedad()==null) {
                    empleado.setAntiguedad(currentYear);
                    saveEmpleadoCsv(empleado); // Se guarda en el csv.
                }
            }
        } catch (Exception e) {
            userMethods.myPrinter("Error al leer empleados.json " + e.getMessage());
        }
        return empleados;
    }

    // Funcion que crea datosEmpresa.json
    public String generateJson() {
        String message = "";

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            mapper.writeValue(jsonPath.toFile(), empresaRepository.getListaEmpleados());
            message += "Json generado con exito.";
        } catch (IOException e) {
            message += "Error al generar datosEmpresa.json.";
        }
        return message;
    }

    // Funcion que crea datosEmpresa.xml
    public String generateXml() {
        String message = "";

        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            EmpleadosWrapper wrapper = new EmpleadosWrapper(empresaRepository.getListaEmpleados());
            xmlMapper.writeValue(xmlPath.toFile(), wrapper);
            message += "xml generado con exito.";
        } catch (IOException e) {
            message += "Error al generar datosEmpresa.xml.";
        }
        return message;
    }
}

