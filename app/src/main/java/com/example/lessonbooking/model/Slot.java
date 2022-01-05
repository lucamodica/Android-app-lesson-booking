package com.example.lessonbooking.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class Slot implements Serializable {
    String time_slot;
    String id_number;
    String teacher_name;
    String teacher_surname;
    String course;
    String day;

    public Slot(String time_slot, String id_number, String t_name,
                String t_surname, String course) {
        this.time_slot = time_slot;
        this.id_number = id_number;
        this.teacher_name = t_name;
        this.teacher_surname = t_surname;
        this.course = course;
    }

    public Slot(String time_slot, String id_number, String t_name,
                String t_surname, String course, String day) {
        this.time_slot = time_slot;
        this.id_number = id_number;
        this.teacher_name = t_name;
        this.teacher_surname = t_surname;
        this.course = course;
        this.day = day;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getTeacher_surname() {
        return teacher_surname;
    }

    public void setTeacher_surname(String teacher_surname) {
        this.teacher_surname = teacher_surname;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Slot slot = (Slot) o;
        return time_slot.equals(slot.time_slot) && id_number.equals(slot.id_number)
                && course.equals(slot.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(time_slot, id_number, course);
    }

    @NonNull
    @Override
    public String toString() {
        return "Slot{" +
                "time_slot='" + time_slot + '\'' +
                ", id_number='" + id_number + '\'' +
                ", t_name='" + teacher_name + '\'' +
                ", t_surname='" + teacher_surname + '\'' +
                ", course='" + course + '\'' +
                '}';
    }
}