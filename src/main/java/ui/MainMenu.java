package ui;

import controllers.ReadXMLDOM;
import models.Departamento;
import models.Empleado;
import myLib.UserMethods;
import org.xml.sax.SAXException;
import services.EmpleadoServices;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private Scanner scanner = new Scanner(System.in);
    private boolean exitMenu = false;
    static List<Departamento> listaDepartamentos;
    static List<Empleado> listaEmpleados;

    // Rutas
    private final Path depPath = Path.of("./target", "departamentos.xml");

    public void showMenu() {
        UserMethods userMethods = new UserMethods();
        do{
            userMethods.myPrinter("Choose an option: ");
            userMethods.myPrinter("1. Añadir empleado.");
            userMethods.myPrinter("2. Cargar departamentos.");
            userMethods.myPrinter("3. Asignar departamentos.");
            userMethods.myPrinter("4. Cargar empleados nuevos.");
            userMethods.myPrinter("0. Exit.");
            processOption(requestOption());
        }while(!exitMenu);
    }

    private String requestOption() { return this.scanner.nextLine(); }

    private void processOption(String option) {
        switch (option) {
            case "0" -> exitMenu = true;
            case "1" -> addEmployeesOption();
            case "2" -> loadDepartamentOption();
            case "3" -> assignDepartmentsOption();
            case "4" -> loadEmployeesOption();
        }
    }

    // Añadir empleado
    public void addEmployeesOption () {
        UserMethods userMethods = new UserMethods();
        EmpleadoServices empleadoServices = new EmpleadoServices();

        boolean exitAddEmployees = false;

        while (!exitAddEmployees) {
            //  Solicitar nombre
            String option = userMethods.requestStringRegex("Introduce el nombre (o escribe 'X' para salir) :","^(?!(?:CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])(?:\\..*)?$)[^\\\\/:*?\"<>|\\r\\n\\s](?:[^\\\\/:*?\"<>|\\r\\n]*[^\\\\/:*?\"<>|\\r\\n\\s])?$");

            if (option.equalsIgnoreCase("X")) {
                exitAddEmployees = true;
            } else {
                // Solicitar sueldo
                Double sueldo = userMethods.requestDouble("Introduce el sueldo:");
                // Solicitar fecha
                Integer año = userMethods.requestDate("Introduce el año de nacimiento:");
                // Solicitar año de incorporación
                Integer antiguedad = userMethods.requestDate("Introduce los años de antigüedad:");

                Empleado emp = new Empleado();
                emp.setNombre(option);
                emp.setSueldo(sueldo);
                emp.setAño(año);
                emp.setAntiguedad(antiguedad);

                empleadoServices.saveEmpleadoCsv(emp);
                userMethods.myPrinter("Empleado guardado correctamente.");
            }
        }
    }

    // Cargar departamentos.xml
    private void loadDepartamentOption() {
        UserMethods userMethods = new UserMethods();
        ReadXMLDOM readXMLDOM = new ReadXMLDOM();

        try {
            listaDepartamentos = readXMLDOM.readDepartamentoXML(depPath);
            for (Departamento dep : listaDepartamentos) {
                userMethods.myPrinter(dep.getId() + " - " + dep.getNombre() + " (" + dep.getLocalidad() + ")");
            }
        } catch (IOException e) {
            userMethods.myPrinter("Something went wrong reading the file.");
        } catch (SAXException e) {
            userMethods.myPrinter("The xml file does not match the requirements.");
        }

    }

    // Asignar departamentos
    public void assignDepartmentsOption() {
        UserMethods userMethods = new UserMethods();
        EmpleadoServices empleadoServices = new EmpleadoServices();

        List<Empleado> empleados = empleadoServices.readEmpleadosCsv();

        for (Empleado emp : empleados) {
            userMethods.myPrinter("\nEmpleado: " + emp.getNombre() + " (Sueldo: " + emp.getSueldo() + ")");
            userMethods.myPrinter("Selecciona el ID del departamento a asignar:");

            for (Departamento dep : listaDepartamentos) {
                userMethods.myPrinter("   " + dep.getId() + ". " + dep.getNombre() + " (" + dep.getLocalidad() + ")");
            }
            userMethods.myPrinter("Introduce ID del departamento:");
            Integer selectedId = Integer.parseInt(scanner.nextLine());
            emp.setIdDep(selectedId);

            // Guardamos directamente añadiendo la nueva línea al CSV
            empleadoServices.saveEmpleadoCsv(emp);
        }
        userMethods.myPrinter("Empleados guardados con su departamento asignado.");
    }

    // Cargar nuevosEmpleados.json
    // AÑADIR AL POM LA DEPDENCIA!!!!!!
    private void loadEmployeesOption() {
        UserMethods userMethods = new UserMethods();
        EmpleadoServices empleadoServices = new EmpleadoServices();

        try {
            listaEmpleados = empleadoServices.readEmpleadosJson();
            userMethods.myPrinter("- Empleados cargados - ");
            for (Empleado emp : listaEmpleados) {
                userMethods.myPrinter("Nombre: "+emp.getNombre()+"\n"+"Sueldo : "+emp.getSueldo()+"\n"+"Año: "+emp.getAño()+"\n"+"Antiguedad: "+emp.getAntiguedad()+"\n"+"Dep: "+emp.getIdDep()+"\n----------");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}