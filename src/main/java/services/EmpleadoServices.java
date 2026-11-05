package services;

import models.Departamento;
import models.Empleado;
import myLib.UserMethods;
import repositories.EmpresaRepository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoServices {

    // Ruta
    private final Path csvPath = Path.of("src/main/resources", "empleados.csv");
    private final Path jsonPath = Path.of("src/main/resources", "nuevosEmpleados.json");
    String regexLetras = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$";

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

        // Comprobación de fechas
        if (antiguedad != null && año != null && antiguedad < año) {
            message += "Wrong date: \n";
            redFlag = true;
        }

        // Control de duplicados por nombre
        if (nombre != null) {
            for (Empleado emp : empresaRepository.getListaEmpleados()) {
                if (emp.getNombre() != null && emp.getNombre().trim().equalsIgnoreCase(nombre.trim())) {
                    message += "Employee already exists with name: " + nombre + "\n";
                    redFlag = true;
                    break; // Detenemos la búsqueda al encontrar la primera coincidencia
                }
            }
        }

        // 3. Escribir en CSV y añadir al repositorio si no hay fallos
        if (!redFlag) {
            try {
                String line = empleado.toCsv() + "\n";
                Files.writeString(csvPath, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                empresaRepository.addEmpleado(empleado); // Añade un empleado a la lista empleados

                message = "Empleado guardado correctamente.";
            } catch (IOException e) {
                System.err.println("Error al escribir en CSV: " + e.getMessage());
                message = "Error al escribir en el archivo CSV.";
            }
        }

        return message;
    }

    // Funcion que lee empleados.csv
    public List<Empleado> readEmpleadosCsv() {
        List<Empleado> lista = new ArrayList<>();

        if (Files.exists(csvPath)) {
            try {
                List<String> lines = Files.readAllLines(csvPath);
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
    public List<Empleado>  readEmpleadosJson() {
        List<Empleado> lista;
        int currentYear = Year.now().getValue(); // Año actual

        ObjectMapper mapper = new ObjectMapper();
        lista = mapper.readValue(jsonPath.toFile(), new  TypeReference<List<Empleado>>(){});

        for (Empleado empleado : lista) {
            if (empleado.getAntiguedad()==null) {
                empleado.setAntiguedad(currentYear);
            }
        }

        return lista;
    }

    public String assignDepartments() {
        String message = "Feedback: \n";
        boolean redFlag = false;

        EmpleadoServices empleadoServices = new EmpleadoServices();

        List<Empleado> empleados = empleadoServices.readEmpleadosCsv();
        List<Departamento> departamentos = EmpresaRepository.getListaDepartamentos();

        // Comprobaciones de seguridad
        if (departamentos.isEmpty()) {
            redFlag = true;
            message += "There are no departamentos to assign.\n";
        }
        if (empleados.isEmpty()) {
            redFlag = true;
            message += "There are no empleados to assign.\n";
        }

        // 3. Enlace automático por ID
        if (!redFlag) {
            int asignados = 0;

            for (Empleado emp : empleados) {
                Integer idDep = emp.getIdDep(); // ID leído desde el CSV

                if (idDep != null) {
                    // Buscamos el departamento correspondiente en la lista del repositorio
                    Departamento depEncontrado = departamentos.stream()
                            .filter(d -> d.getId() != null && d.getId().equals(idDep))
                            .findFirst()
                            .orElse(null);

                    if (depEncontrado != null) {
                        // Sustituimos el objeto cascarón por el objeto Departamento completo
                        emp.setDepartamento(depEncontrado);
                        asignados++;
                    }
                }
            }

            // 4. Actualizamos el repositorio central en memoria
            EmpresaRepository.getListaEmpleados().clear();
            EmpresaRepository.getListaEmpleados().addAll(empleados);

            message += "Se han enlazado " + asignados + " empleados con sus departamentos correctamente.";
        }
        return message;
    }
}

