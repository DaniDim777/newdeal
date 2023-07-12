package com.task.models;

import com.task.config.LocalDateAdapter;
import lombok.*;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * класс для создания объекта сотрудника
 */
@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Employee {

    @XmlAttribute
    private int id;
    @XmlElement
    private String name;
    @XmlElement
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class, type = LocalDateTime.class)
    private LocalDate birthday;
    @XmlElement
    private List<Company> companies = new ArrayList<>();

    public Employee(int id, String name, LocalDate birthday) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
    }
}
