package com.example.lessonbooking.utilities;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class GenericUtils {

    public static List<String> getLessonTimeSlots(){
        return Arrays.asList("15:00-16:00", "16:00-17:00",
                "17:00-18:00", "18:00-19:00");
    }

    public static List<String> getLessonDays(){
        return Arrays.asList("Lunedi", "Martedi",
                "Mercoledi", "Giovedi", "Venerdi");
    }

    public static List<String> getLessonStatus(){
        return Arrays.asList("attiva", "effettuata", "disdetta");
    }

    public static List<String> getUserType(){
        return Arrays.asList("utente", "amministratore");
    }

    public static String getNetworkTag(){
        return "DEMAND";
    }

}
