package dev.majed.checkdo;


import java.io.Serializable;

public class SingleEntry implements Serializable {
    private String taskName;
    private Long taskTime;
    private Long taskId;
    public SingleEntry(String taskName, Long taskTime,Long taskId) {
        this.taskName = taskName;
        this.taskTime = taskTime;
        this.taskId=taskId;
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
}
