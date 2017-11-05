package dev.majed.checkdo;


import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SingleEntry implements Serializable{
    private String taskName;
    private Long taskTime;
    private Long taskId;
    private String taskDay;
    private boolean isDone;

    public String getTaskDay() {
        return taskDay;
    }

    public void setTaskDay(String taskDay) {
        Log.e("hurray","ClassCalled" +taskDay);
        this.taskDay = taskDay;
    }

    public SingleEntry(String taskName, Long taskTime, Long taskId) {
        this.taskName = taskName;
        this.taskTime = taskTime;
        this.taskId=taskId;
        this.isDone=false;
        Date d = new Date(taskTime);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        String dateText = df2.format(d);
        this.taskDay = dateText;
    }

    public boolean getDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Long getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(Long taskTime) {
        this.taskTime = taskTime;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

 /*   @Override
    public int compare(Object o, Object t1) {
        SingleEntry one = (SingleEntry)o;
        SingleEntry two = (SingleEntry)t1;
        return one.getTaskTime().compareTo(two.getTaskTime());
    }*/





}
