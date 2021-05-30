package com.example.remedy.Model;

import java.util.Date;

public class TaskModel {
    private int idTask;
    private String taskName;
    private String taskText;
    private String taskDate;
    private int taskStatus;
    private String reminder;
    private int idTaskGroup;
    private String taskTime;

    public TaskModel(){

    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public int getIdTaskGroup() {
        return idTaskGroup;
    }

    public void setIdTaskGroup(int idTaskGroup) {
        this.idTaskGroup = idTaskGroup;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }
}
