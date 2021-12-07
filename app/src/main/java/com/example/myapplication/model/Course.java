package com.example.myapplication.model;

import java.io.Serializable;

public class Course implements Serializable {
private String course_title;
private String desc;

    public Course() {
        course_title="";
        desc="";
    }

    public Course(String course_title, String desc) {
        this.course_title = course_title;
        this.desc = desc;
    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public String getDesc() {
        return desc;
    }


    @Override
    public String toString() {
        return "Course{" +
                "course_title='" + course_title + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
