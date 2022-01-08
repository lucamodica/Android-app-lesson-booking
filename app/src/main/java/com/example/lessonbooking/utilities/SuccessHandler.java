package com.example.lessonbooking.utilities;

import org.json.JSONException;
import org.json.JSONObject;

public interface SuccessHandler {
    void handle(JSONObject obj) throws JSONException;
}
