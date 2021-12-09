package com.example.lessonbooking.connectivity;

public class NetworkData {
    static String jsessionid = "";

    public static String getJsessionid() {
        return jsessionid;
    }

    public static void setJsessionid(String newJsessionid) {
        jsessionid = newJsessionid;
    }
}
