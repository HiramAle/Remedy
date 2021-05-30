package com.example.remedy;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.example.remedy.Adapter.Adapter;
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


    public Home() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController(view);

        FloatingActionButton btnSettings = view.findViewById(R.id.fabSettings);
        FloatingActionButton btnAddGroupTask = view.findViewById(R.id.fabAddGroup);
        FloatingActionButton btnAllTask = view.findViewById(R.id.fabAllTask);
        FloatingActionButton btnDialog = view.findViewById(R.id.fabHistory);

        btnSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_Home_to_settings);
            }
        });



        btnAddGroupTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_Home_to_dialogNewTaskGroup);
            }
        });
        btnAllTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskGroupModel all = new TaskGroupModel();
                all.setIdTaskGroup(0);
                all.getTaskGroupName();
                moveToTask(all);
            }
        });

        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



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
                //Toast.makeText(getContext(),item.getTaskGroupName(),Toast.LENGTH_LONG).show();
                moveToTask(item);
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.taskGroupRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setAdapter(listAdapter);
        db.close();
    }

    public void moveToTask(TaskGroupModel item){
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getIdTaskGroup());
        bundle.putString("name",item.getTaskGroupName());
        final NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.action_Home_to_taskGroup,bundle);
    }
}