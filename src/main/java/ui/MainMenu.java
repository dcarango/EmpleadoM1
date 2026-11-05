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

    private final Path csvPath = Path.of("src/main/resources", "empleados.csv");
    private final Path depPath = Path.of("src/main/resources", "departamentos.xml");
    private final Path jsonPath = Path.of("src/main/resources", "empresaDatos.json");
    private final Path xmlPath = Path.of("src/main/resources", "empresaDatos.xml");

    public void showMenu() {
        UserMethods userMethods = new UserMethods();
        do{
            userMethods.myPrinter("Choose an option: ");
            userMethods.myPrinter("1. Añadir empleado.");
            userMethods.myPrinter("2. Cargar empleados (.json).");
            userMethods.myPrinter("3. Cargar departamentos (.xml) .");
            userMethods.myPrinter("4. Asignar departamentos.");
            userMethods.myPrinter("5. Generar .json.");
            userMethods.myPrinter("5. Generar .xml.");
            userMethods.myPrinter("0. Exit.");
            processOption(requestOption());
        }while(!exitMenu);
    }

    private String requestOption() { return this.scanner.nextLine(); }

    private void processOption(String option) {
        switch (option) {
            case "0" -> exitMenu = true;
            case "1" -> addEmployeesOption();
            case "2" -> loadEmployeesOption();
            case "3" -> loadDepartamentOption();
            case "4" -> assignDepartmentsOption();
            case "5" -> generateJsonOption();
            case "6" -> generateXmlOption();
        }
    }

    // Añadir empleado
    public void addEmployeesOption () {
        UserMethods userMethods = new UserMethods();
        EmpleadoServices empleadoServices = new EmpleadoServices();

        boolean exitAddEmployees = false;

        while (!exitAddEmployees) {
            //  Solicitar nombre
            String option = userMethods.requestStringRegex("Introduce el nombre (o escribe 'X' para salir)");

            if (option.equalsIgnoreCase("X")) {
                exitAddEmployees = true;
            } else {
                Empleado emp = new Empleado();
                emp.setNombre(option);
                emp.setSueldo(userMethods.requestDouble("Sueldo:"));
                emp.setAño(userMethods.requesYear("Año de nacimiento:"));
                emp.setAntiguedad(userMethods.requesYear("Año de entrada:"));

                userMethods.myPrinter(empleadoServices.saveEmpleadoCsv(emp));
            }
        }
    }

    // Cargar departamentos.xml
    private void loadDepartamentOption() {
        UserMethods userMethods = new UserMethods();
        ReadXMLDOM readXMLDOM = new ReadXMLDOM();

        try {
            listaDepartamentos = readXMLDOM.readDepartamentoXML(depPath);
            userMethods.myPrinter("Departamentos cargados correctamente.");
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


    private void generateJsonOption() {

    }
    private void generateXmlOption() {

    }

}