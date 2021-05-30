package com.example.remedy.Model;

import java.io.Serializable;

public class TaskGroupModel implements Serializable {

    private int idTaskGroup;
    private String taskGroupName;

    public TaskGroupModel() {
    }

    public int getIdTaskGroup() {
        return idTaskGroup;
    }

    public void setIdTaskGroup(int idTaskGroup) {
        this.idTaskGroup = idTaskGroup;
    }

    public String getTaskGroupName() {
        return taskGroupName;
    }

    public void setTaskGroupName(String taskGroupName) {
        this.taskGroupName = taskGroupName;
    }
}
