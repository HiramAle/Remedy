package com.example.remedy;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.remedy.Adapter.TaskGroupAdapter;
import com.example.remedy.DataBase.DataBaseUtilities;
import com.example.remedy.DataBase.SQLConnection;
import com.example.remedy.Model.TaskGroupModel;
import com.example.remedy.Model.TaskModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    List<TaskGroupModel> elements;
    TaskGroupAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Navigation Controller
        final NavController navController = Navigation.findNavController(view);
        //Button to Settings
        FloatingActionButton btnSettings = view.findViewById(R.id.fabSettings);
        //Button to Add a Task Group
        FloatingActionButton btnAddGroupTask = view.findViewById(R.id.fabAddGroup);
        //Button to All Tasks
        FloatingActionButton btnAllTask = view.findViewById(R.id.fabAllTask);
        //Button to History Tasks (Completed)
        FloatingActionButton btnHistory = view.findViewById(R.id.fabHistory);
        //Click Listener Settings
        btnSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_Home_to_settings);
            }
        });
        //Click Listener Add Group Task
        btnAddGroupTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("idCase",0);
                bundle.putString("nameCase","Add");
                navController.navigate(R.id.action_Home_to_dialogNewTaskGroup,bundle);
            }
        });
        //Click Listener get All Tasks
        btnAllTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskGroupModel all = new TaskGroupModel();
                all.setIdTaskGroup(0);
                all.setTaskGroupName("All");
                moveToTaskGroup(all);
            }
        });
        //Click Listener get Completed Tasks
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskGroupModel completed = new TaskGroupModel();
                completed.setIdTaskGroup(0);
                completed.setTaskGroupName("Completed");
                moveToTaskGroup(completed);
            }
        });
        //Set the Task Group List



        dbTaskGroupList(view);
    }

    public void dbTaskGroupList(View view) {
        SQLConnection connection = new SQLConnection(getActivity(), "bdRemedy", null, 1);
        SQLiteDatabase db = connection.getReadableDatabase();
        TaskGroupModel taskGroup = null;
        Cursor cursor = db.rawQuery("SELECT * FROM "+ DataBaseUtilities.dbTaskGroupTable, null);
        elements = new ArrayList<>();
        while (cursor.moveToNext()) {
            taskGroup = new TaskGroupModel();
            taskGroup.setIdTaskGroup(cursor.getInt(0));
            taskGroup.setTaskGroupName(cursor.getString(1));
            elements.add(taskGroup);
        }
        listAdapter = new TaskGroupAdapter(elements, getContext(), new TaskGroupAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TaskGroupModel item) {
                moveToTaskGroup(item);
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.taskGroupRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setAdapter(listAdapter);
        db.close();
        cursor.close();
    }

    public void moveToTaskGroup(TaskGroupModel item){
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getIdTaskGroup());
        bundle.putString("name",item.getTaskGroupName());
        final NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.action_Home_to_taskGroup,bundle);
    }
}