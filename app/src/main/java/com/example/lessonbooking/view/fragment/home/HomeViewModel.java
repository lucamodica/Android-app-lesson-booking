package com.example.lessonbooking.view.fragment.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lessonbooking.model.Lesson;
import com.example.lessonbooking.model.Slot;

import java.util.List;
import java.util.Objects;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<Lesson>> lessons;

    public HomeViewModel() {
        lessons = new MutableLiveData<>();
    }

    public LiveData<List<Lesson>> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> newLessons){
        lessons.setValue(newLessons);
    }

    public void setNewStatus(int lessonIndex, String newStatus){
        Objects.requireNonNull(getLessons().getValue()).
                get(lessonIndex).setStatus(newStatus);
    }

}