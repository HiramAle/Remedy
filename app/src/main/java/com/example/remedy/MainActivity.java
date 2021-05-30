package com.example.remedy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.remedy.Adapter.Adapter;
import com.example.remedy.DataBase.SQLConnection;
import com.example.remedy.Model.TaskModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Hide Title Bar
        this.getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        SQLConnection connection = new SQLConnection(this,"bdRemedy",null,1);

    }

}