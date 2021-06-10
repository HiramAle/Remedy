package com.example.remedy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.example.remedy.DataBase.SQLConnection;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide Title Bar
        this.getSupportActionBar().hide();
        //Set the layout for the Navigation Host
        setContentView(R.layout.activity_main);
        //Create Data Base
        SQLConnection connection = new SQLConnection(this, "bdRemedy", null, 1);
    }











}

