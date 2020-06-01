package com.example.tasks;

import com.orm.SugarRecord;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Task extends SugarRecord<Task> {

    String title;
    String description;
    String date;
    String begin;
    String end;
    int priority;
    boolean notification;
    String completed;

    public Task(){
    }

    public Task(String title, String description, String date, String begin, String end, int priority, boolean notification){
        this.title = title;
        this.description = description;
        this.date = date;
        this.begin = begin;
        this.end = end;
        this.priority = priority;
        this.notification = notification;
    }

    public String getFormatPriority(){

        String formatPriority = "";

        switch (this.priority){
            case 0:
                formatPriority = "Normal";
                break;
            case 1:
                formatPriority = "Medium";
                break;
            case 2:
                formatPriority = "High";
                break;
        }

        return formatPriority;
    }

    public String getFormatDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(Long.parseLong(this.date));

        return date;
    }

    public String getFormatTime(String time){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String formatTime = dateFormat.format(Long.parseLong(time));

        return formatTime;
    }

}
