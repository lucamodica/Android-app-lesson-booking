package com.example.lessonbooking.model;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Course implements Model, Serializable {
    private String course_title;
    private String desc;

    public Course(String course_title, String desc) {
        this.course_title = course_title;
        this.desc = desc;
    }

    public Course(JSONObject obj) throws JSONException {
        this.course_title = obj.getString("title");
        this.desc = obj.getString("desc");
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

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @NonNull
    @Override
    public String toString() {
        return course_title;
    }

    @Override
    public String getModelId() {
        return course_title;
    }
}
