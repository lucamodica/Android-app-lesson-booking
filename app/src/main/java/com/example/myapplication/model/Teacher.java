package com.example.myapplication.model;

import java.io.Serializable;

public class Teacher  implements Serializable {
    private String name;
    private String surname;
 private String id_number;

    public Teacher(String name, String surname, String id_number) {
        this.name = name;
        this.surname = surname;
        this.id_number = id_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", id_number='" + id_number + '\'' +
                '}';
    }
}
