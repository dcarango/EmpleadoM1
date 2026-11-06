package ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import models.Departamento;
import models.Empleado;
import models.EmpleadosWrapper;
import myLib.UserMethods;
import repositories.EmpresaRepository;
import services.DepartamentoServices;
import services.EmpleadoServices;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private Scanner scanner = new Scanner(System.in);
    private boolean exitMenu = false;

    public void showMenu() {
        UserMethods userMethods = new UserMethods();
        do{
            userMethods.myPrinter("Choose an option: ");
            userMethods.myPrinter("1. Añadir empleado.");
            userMethods.myPrinter("2. Leer empleados (.json).");
            //userMethods.myPrinter("3. Leer departamentos (.xml).");
            userMethods.myPrinter("3. Asignar departamentos.");
            userMethods.myPrinter("4. Generar .json.");
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
            //case "3" -> loadDepartamentOption();
            case "3" -> assignDepartmentsOption();
            case "4" -> generateJsonOption();
            case "5" -> generateXmlOption();
        }
    }

    // Añadir empleado
    public void addEmployeesOption () {
        UserMethods userMethods = new UserMethods();
        EmpleadoServices empleadoServices = new EmpleadoServices();
        boolean exitAddEmployees = false;

        while (!exitAddEmployees) {
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
    // AÑADIR AL POM LA DEPDENCIA!!!!!!
    private void loadDepartamentOption() {
        UserMethods userMethods = new UserMethods();
        DepartamentoServices departamentoServices = new DepartamentoServices();
        List<Departamento> departamentos = departamentoServices.loadDepartamentosXml();

        if (departamentos.isEmpty()) {
            userMethods.myPrinter("Error a leer departamentos.");
        }
    }

    // Cargar nuevosEmpleados.json
    // AÑADIR AL POM LA DEPDENCIA!!!!!!
    private void loadEmployeesOption() {
        UserMethods userMethods = new UserMethods();
        EmpleadoServices empleadoServices = new EmpleadoServices();
        List<Empleado> empleados = empleadoServices.readEmpleadosJson();

        if (empleados.isEmpty()) {
            userMethods.myPrinter("Error a cargar empleados.");
        }
    }

    // Asignar departamentos
    public void assignDepartmentsOption() {
        UserMethods userMethods = new UserMethods();
        DepartamentoServices departamentoServices = new DepartamentoServices();
        EmpleadoServices empleadoServices = new EmpleadoServices();

        // Obtenemos info
        List<Empleado> empleados = empleadoServices.readEmpleadosCsv();
        List<Departamento> departamentos = departamentoServices.loadDepartamentosXml();

        departamentoServices.assignDepartment(empleados,departamentos);
    }

    private void generateJsonOption() {
        UserMethods userMethods = new UserMethods();
        EmpleadoServices empleadoServices = new EmpleadoServices();
        userMethods.myPrinter(empleadoServices.generateJson());
    }
    private void generateXmlOption() {
        UserMethods userMethods = new UserMethods();
        EmpleadoServices empleadoServices = new EmpleadoServices();
        userMethods.myPrinter(empleadoServices.generateXml());
    }

}