package com.example.lessonbooking.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Lesson implements Serializable {
    private String teacher;
    private String course;
    private String t_slot;
    private String day;
    private String user;
    private String status;
    private String teacher_name;
    private String teacher_surname;

    public Lesson(String teacher, String course, String t_slot,
                  String day, String user, String status,
                  String teacher_name, String teacher_surname) {
        this.teacher = teacher;
        this.course = course;
        this.t_slot = t_slot;
        this.day = day;
        this.user = user;
        this.status = status;
        this.teacher_name = teacher_name;
        this.teacher_surname = teacher_surname;
    }

    public Lesson() {
        this.teacher = "";
        this.course = "";
        this.t_slot = "";
        this.day = "";
        this.user = "";
        this.status = "";
        this.teacher_name = "";
        this.teacher_surname = "";
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getT_slot() {
        return t_slot;
    }

    public void setT_slot(String t_slot) {
        this.t_slot = t_slot;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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


    @NonNull
    @Override
    public String toString() {
        return "Lessons{" +
                "teacher='" + teacher + '\'' +
                ", course='" + course + '\'' +
                ", t_slot='" + t_slot + '\'' +
                ", day='" + day + '\'' +
                ", user='" + user + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
