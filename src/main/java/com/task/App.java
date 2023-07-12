package com.task;

import com.task.models.Company;
import com.task.models.Employee;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Выбрал jaxb проверить как он работает опыта не было, можно было бы xstream, через dom слишком объемно
 */
public class App {
    public static void main(String[] args) {
        /**
         * метод для создания файла xml
         */
        serializeToXML();
        /**
         * метод для получения данных из файла xml
         */
        deserializeFromXML();
    }

    /**
     * метод для сериализации объектов java в файл xml
     */
    public static void serializeToXML() {
        try {
            ArrayList<Company> companies = new ArrayList<>();
            companies.add(new Company(1, "Apple", LocalDate.of(1989, 2, 7), true));
            companies.add(new Company(2, "Mars", LocalDate.of(1989, 10, 11), false));
            companies.add(new Company(3, "BMW", LocalDate.of(1903, 12, 29), true));
            companies.add(new Company(4, "Microsoft", LocalDate.of(1970, 6, 8), false));

            ArrayList<Employee> employees = new ArrayList<>();
            employees.add(new Employee(1, "Nikolay", LocalDate.of(1989, 2, 7)));
            employees.add(new Employee(2, "Alexey", LocalDate.of(1989, 10, 11)));
            employees.add(new Employee(3, "Valeriy", LocalDate.of(1992, 12, 29)));
            employees.add(new Employee(4, "Max", LocalDate.of(1999, 6, 8)));
            employees.add(new Employee(5, "Yuriy", LocalDate.of(1989, 10, 11)));

            companies.get(0).setEmployees(employees.subList(1, 4));
            companies.get(2).setEmployees(employees.subList(3, 5));

            AllCompanies newList = new AllCompanies();
            newList.setDataComp(companies);

            /**
             * создание контекста jaxb и маршаллера для сериализации объектов java в файл xml
             */
            JAXBContext jaxbContext = JAXBContext.newInstance(AllCompanies.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(newList, new File("serialized.xml"));

            /**
             * создание файла xsd
             */
            File xsdFile = new File("serialized.xsd");
            try {
                jaxbContext.generateSchema(new SchemaOutputResolver() {
                    @Override
                    public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
                        StreamResult result = new StreamResult(new FileOutputStream(xsdFile));
                        result.setSystemId(xsdFile.getAbsoluteFile());
                        return result;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    /**
     * общий класс для работы с коллекцией
     */
    @XmlRootElement
    public static class AllCompanies {
        private ArrayList<Company> dataComp;

        @XmlElement(name = "company")
        public ArrayList<Company> getDataComp() {
            return dataComp;
        }

        public void setDataComp(ArrayList<Company> dataComp) {
            this.dataComp = dataComp;
        }
    }

    /**
     * метод для десериализации из файла xml в объекты java
     */
    public static void deserializeFromXML() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(AllCompanies.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            AllCompanies openList = (AllCompanies) unmarshaller.unmarshal(new File("serialized.xml"));
            System.out.println(openList.getDataComp().stream().filter(Company::getIsOpened).collect(Collectors.toList()));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
