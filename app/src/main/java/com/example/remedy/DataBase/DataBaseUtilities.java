package com.example.remedy.DataBase;

public class DataBaseUtilities {
    //Task table name
    public static final String dbTaskTable = "task";
    //Task table data
    public static final String dbTaskId = "idTask";
    public static final String dbTask_Name = "taskName";
    public static final String dbTask_Text = "taskText";
    public static final String dbTask_Date = "taskDate";
    public static final String dbTask_Reminder = "taskReminder";
    public static final String dbTask_Status = "taskStatus";
    public static  final String dbTask_TaskGroupId = "taskGroupId";
    public static  final String dbTask_ContactName = "contactName";
    public static  final String dbTask_ContactNumber = "contactNumber";
    //DataBase task table
    public static final String dbCreateTaskTable = "CREATE TABLE task(" +
            "idTask INTEGER PRIMARY KEY   AUTOINCREMENT, " +
            "taskName TEXT, " +
            "taskText TEXT, " +
            "date TEXT, " +
            "time TEXT , " +
            "reminder TEXT, " +
            "status INTEGER DEFAULT 1, " +
            "taskGroupId INTEGER, " +
            "contactName TEXT, " +
            "contactNumber TEXT)";

    //TaskGroup table name
    public static final String dbTaskGroupTable = "taskGroup";
    //TaskGroup table data
    public static final String dbTaskGroup_Id = "idTaskGroup";
    public static final String dbTaskGroup_Name = "taskGroupName";
    //DataBase task table
    public static final String dbCreateTaskGroupTable = "CREATE TABLE taskGroup(" +
            "taskGroupId INTEGER PRIMARY KEY   AUTOINCREMENT, " +
            "taskGroupName TEXT)";
}
