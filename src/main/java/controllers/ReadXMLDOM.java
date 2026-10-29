package controllers;

import models.Departamento;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ReadXMLDOM {

    // Read departamentos
    public List<Departamento> readDepartamentoXML (Path path) throws IOException, SAXException {
        List<Departamento> listaDepartamentos = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder parser = null;

        try {
            parser = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            listaDepartamentos = null;
        }

        Document document = parser.parse(path.toFile());
        Element root = document.getDocumentElement();

        // Listas de nodos paralelas obtenidas desde la raíz
        NodeList departamentos = root.getElementsByTagName("departamento");
        NodeList nombres = root.getElementsByTagName("nombre");
        NodeList localidades = root.getElementsByTagName("localidad");

        // Departamento aux para guardar la info
        Departamento auxDep;

        for (int i = 0; i < departamentos.getLength(); i++) {
            auxDep = new Departamento();

            // Contenido de los nodos de texto
            auxDep.setNombre(nombres.item(i).getTextContent());
            auxDep.setLocalidad(localidades.item(i).getTextContent());

            // Transformación a Element para leer el atributo 'id'
            Element elementDep = (Element) departamentos.item(i);
            auxDep.setId(Integer.valueOf(elementDep.getAttribute("id")));

            listaDepartamentos.add(auxDep);
        }
        return listaDepartamentos;
    }
}
