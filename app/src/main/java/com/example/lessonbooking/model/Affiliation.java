package com.example.lessonbooking.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Affiliation implements Serializable {
     private String teacher_id;
     private String  course_title;

    public Affiliation(String teacher_id, String course_title) {
        this.teacher_id = teacher_id;
        this.course_title = course_title;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    @NonNull
    @Override
    public String toString() {
        return "Affiliation{" +
                "teacher_id='" + teacher_id + '\'' +
                ", course_title='" + course_title + '\'' +
                '}';
    }
}
