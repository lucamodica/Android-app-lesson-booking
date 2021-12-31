package com.example.lessonbooking.utilities;

import android.content.Context;
import android.widget.TextView;

import com.example.lessonbooking.R;

import java.util.Arrays;
import java.util.List;

public class GenericUtils {

    //List of all possible time slots
    public static List<String> getLessonTimeSlots(){
        return Arrays.asList("15:00-16:00", "16:00-17:00",
                "17:00-18:00", "18:00-19:00");
    }

    //List of all possible days
    public static List<String> getLessonDays(){
        return Arrays.asList("Lunedi", "Martedi",
                "Mercoledi", "Giovedi", "Venerdi");
    }

    //List of all possible status
    public static List<String> getLessonStatus(){
        return Arrays.asList("attiva", "effettuata", "disdetta");
    }

    //List of all possible user role
    public static List<String> getUserRoles(){
        return Arrays.asList("utente", "amministratore");
    }

    //Set the textColor to the status
    public static void setStatusColor(Context ctx, TextView statusText){
        int color;
        switch (statusText.getText().toString()){
            case "attiva":
                color = R.color.primary;
                break;

            case "effettuata":
                color = R.color.success;
                break;

            case "disdetta":
                color = R.color.danger;
                break;

            default:
                color = R.color.black;
        }

        statusText.setTextColor(ctx.getResources().getColor(color, ctx.getTheme()));
    }
}
