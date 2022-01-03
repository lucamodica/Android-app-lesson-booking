package com.example.lessonbooking.model;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Teacher  implements Serializable {

    private String name;
    private String surname;
    private String id_number;

    public Teacher(String id_number, String name, String surname) {
        this.id_number = id_number;
        this.name = name;
        this.surname = surname;
    }

    public Teacher(JSONObject obj) throws JSONException {
        this.id_number = obj.getString("id_number");
        this.name = obj.getString("name");
        this.surname = obj.getString("surname");
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

    @NonNull
    @Override
    public String toString() {
        return name + " " + surname + " (" +
                id_number + ")";
    }
}
