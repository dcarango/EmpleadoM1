package services;

import models.Empleado;
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

    // Funcion que escribe un empleado (Lo reescribe)
    public boolean saveEmpleadoCsv(Empleado empleado) {
        boolean success = false;

        // Llama al metodo de clase que reorganiza la info del parametro y la escriibe con en el Files.writeString
        try {
            String line = empleado.toCsv() + "\n";
            Files.writeString(csvPath, line, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            success = true;
        } catch (IOException e) {
            System.err.println("Error al escribir en CSV: " + e.getMessage());
        }

        return success;
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
}
