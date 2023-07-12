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
 * 1 - Что означает буква L из SOLID?======================================================================================
 * означает принцип подстановки Лисков, в котором мы должны использовать любой производный класс вместо родительского класса
 * и вести себя с ним таким же образом без внесения изменений, то есть дочерний класс не должен нарушать тип родительского
 * класса и его поведение. Методы, которые принимают/возвращают значения с типом суперкласса должны принимать/возвращать значения
 * типами которых являются его подклассы.
 * 2 - В чём разница между обёрткой и адаптером. А между фабрикой и билдером?================================================
 * обёртка - это класс, расширяющий функциональность другого класса без использования наследования, представляется
 * оригинальным классом, но интерфейс может не изменять или расширять, а также может расширять функциональность
 *
 * адаптер - объект обеспечивающий взаимодействие двух других объектов, один из которых использует, а другой
 * предоставляет несовместимый с первым интерфейс, использовать когда есть класс с нужными данными и поведением, но с неподходящим интерфейсом.
 * (интерфейс изменяет, а функциональность не изменяет)
 *
 * билдер - нужен для создания сложного объекта, который требует пошаговой настройки в несколько этапов,
 * использовать когда для инициализации требуется большое количество параметров, методов
 *
 * фабрика - создает объекты определенного типа напрямую, использует абстрактные классы или интерфейсы
 * в качестве параметров для конструторов, использовать если надо несколько экземпляров класса, но с разными свойствами
 * 3 - Какую проблему решает полиморфизм?===================================================================================
 * позволяет указать объекту что-то сделать, и результирующее действие будет зависеть от класса объекта.
 * используется диспетчер динамических методов, с помощью которого разрешается вызов переопределенного метода
 * во время выполнения.
 *
 * когда переопределенный метод вызывается через ссылку на суперкласс java определяет, какая версия (в суперклассе или подклассе)
 * этого метода должна быть выполнена, в зависимости от типа объекта на который ссылаются в момент вызова.
 *
 * во время выполнения всё зависит от типа объекта, на который делается ссылка (не от типа ссылочной переменной),
 * который определяет, какая версия замещаемого метода будет выполнена.
 *
 * ссылочная переменная суперкласса может ссылаться на объект подкласса(как апкастинг)
 * java использует это для разрешения вызовов переопределенных методов во время выполнения.
 * 4 - В чём недостатки ORM? Как их решать\избегать?=======================================================================
 * -можно получить более эффективные запросы в sql.
 * -дополнительное обучение как использовать какую-нибудь не знакомую orm(включая все настройки)
 * -надо понимать, что происходит под капотом, из-за orm можно избежать понимания баз данных и sql
 * 5 - Когда применяется денормализация? Когда не следует добавлять индекс?
 * -применяется когда необходимо сделать повышение производительности запросов
 * -присутствует большое количество таблиц
 * -использование сложных расчетов
 * -есть длинные поля включая например blob
 *
 * индекс не следует использовать:
 * -если база данных или поля постоянно обновляются
 * -если база данных небольшая
 * -маленькая уникальность данных
 * -если поле не используется с where
 * -если запрос не будет применяться часто
 *
 * 6 - Какая сложность вставки в дерево? Какая сложность бинарного поиска?
 * O(log(n)) - для деревьев
 * O(log(n)) - для бинарного поиска
 *
 * 7 - В чём недостатки аннотаций и рефлексии?
 * аннотации недостатки:
 * -реализация функциональности вне объекта
 * -требуется перекомпиляция, создаются во время компиляции и не могут быть изменены
 * рефлексия недостатки:
 * -работает в динамическом режиме, постоянно сканирует классы для загрузки нужного, снижает скорость работы всего приложения.
 * -можно воздействовать на часть кода, которую не нужно воздействовать(разрешение на закрытые поля классов и изменение значения).
 * -поддержка кода иногда сложная, надо понять и попытаться отладить
 * 8 - Генерировали ли классы из xsd схем?
 * нет, но уже посмотрел, не должно быть ничего сложного
 * 9 - Как напишите интеграционный тест?
 * смотря что тестировать, сделать конфигурацию теста если надо поднять контекст, либо через юнит-тестирование,
 * создать объекты, добавить данные, выполнить некоторое необходимое действие с объектами
 * и сравнить фактический результат с ожидаемым
 *
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
