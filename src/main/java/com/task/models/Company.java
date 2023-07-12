package com.task.models;

import com.task.config.LocalDateAdapter;
import lombok.*;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "company")
@XmlAccessorType(XmlAccessType.FIELD)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Company {

    @XmlAttribute
    private int id;
    @XmlElement
    private String title;
    @XmlElement
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class, type = LocalDateTime.class)
    private LocalDate creationDate;
    @XmlElement
    private Boolean isOpened;
    @XmlElement(name = "employee")
    @XmlElementWrapper(name = "employees")
    private List<Employee> employees = new ArrayList<>();

    public Company(int id, String title, LocalDate creationDate, Boolean isOpened) {
        this.id = id;
        this.title = title;
        this.creationDate = creationDate;
        this.isOpened = isOpened;
    }
}
