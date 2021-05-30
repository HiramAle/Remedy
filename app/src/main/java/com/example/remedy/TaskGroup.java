package com.example.remedy;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.remedy.Adapter.Adapter;
import com.example.remedy.DataBase.DataBaseUtilities;
import com.example.remedy.DataBase.SQLConnection;
import com.example.remedy.Model.TaskGroupModel;
import com.example.remedy.Model.TaskModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskGroup extends Fragment {

    List<TaskModel> elements;
    Adapter listAdapter;
    int idTaskGroup;
    String TaskGroupName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_group, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        ImageButton btn = view.findViewById(R.id.btnMoreOptions);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menusito = new PopupMenu(getContext(),btn);
                menusito.getMenuInflater().inflate(R.menu.group_task_menu,menusito.getMenu());
                menusito.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(getContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        if (item.getTitle().equals("Delete")){
                            deleteTaskGroup(idTaskGroup);
                            navController.popBackStack();
                        }
                        return true;
                    }
                });
                menusito.show();
            }
        });


        try {
            idTaskGroup=getArguments().getInt("id");

            if (idTaskGroup==0){
                dbTaskList(view);
                btn.setVisibility(View.GONE);
                //Toast.makeText(getContext(),"100tra",Toast.LENGTH_LONG).show();
            }else{
                TaskGroupName=getArguments().getString("name");
                androidx.appcompat.widget.AppCompatTextView txt=getView().findViewById(R.id.taskTitle);
                txt.setText(getArguments().getString("name"));
                dbTask(view,idTaskGroup);
            }
        }catch (Exception e){

        }

        btnNewTask(view);
        //dbTaskList(view);

        // This callback will only be called when MyFragment is at least Started.
        /*OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                final NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_taskGroup_to_Home);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);*/





    }

    public void btnNewTask(View view) {
        final NavController navController = Navigation.findNavController(view);
        FloatingActionButton btn = getView().findViewById(R.id.fabAddTask);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToTaskForm();
            }
        });
    }

    public void dbTask(View view,int id){
        SQLConnection connection = new SQLConnection(getActivity(), "bdRemedy", null, 1);
        SQLiteDatabase db = connection.getReadableDatabase();
        TaskModel task = null;
        Cursor cursor = db.rawQuery("SELECT * FROM task WHERE "+DataBaseUtilities.dbTask_TaskGroupId+" = ?", new String[] {String.valueOf(id)});
        elements = new ArrayList<>();
        while (cursor.moveToNext()) {
            task = new TaskModel();
            task.setIdTask(cursor.getInt(0));
            task.setTaskName(cursor.getString(1));
            task.setTaskText(cursor.getString(2));
            task.setTaskDate(cursor.getString(3));
            task.setTaskTime(cursor.getString(4));
            task.setReminder(cursor.getString(5));
            elements.add(task);
        }
        listAdapter = new Adapter(elements, getContext());
        RecyclerView recyclerView = view.findViewById(R.id.taskRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);
        db.close();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void dbTaskList(View view) {
        SQLConnection connection = new SQLConnection(getActivity(), "bdRemedy", null, 1);
        SQLiteDatabase db = connection.getReadableDatabase();
        TaskModel task = null;
        Cursor cursor = db.rawQuery("SELECT * FROM task" ,null);
        elements = new ArrayList<>();
        while (cursor.moveToNext()) {
            task = new TaskModel();
            task.setIdTask(cursor.getInt(0));
            task.setTaskName(cursor.getString(1));
            task.setTaskText(cursor.getString(2));
            task.setTaskDate(cursor.getString(3));
            task.setTaskTime(cursor.getString(4));
            task.setReminder(cursor.getString(5));
            elements.add(task);
        }
        listAdapter = new Adapter(elements, getContext());
        RecyclerView recyclerView = view.findViewById(R.id.taskRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);
        db.close();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    public void deleteTaskGroup(int idTaskGroup){
        SQLConnection connection = new SQLConnection(getActivity(), "bdRemedy", null, 1);
        SQLiteDatabase db = connection.getWritableDatabase();
        String param = String.valueOf(idTaskGroup);
        db.delete(DataBaseUtilities.dbTaskTable, DataBaseUtilities.dbTask_TaskGroupId + "=" + param, null);
        db.delete(DataBaseUtilities.dbTaskGroupTable, DataBaseUtilities.dbTaskGroup_Id + "=" + param, null);
        db.close();

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
            return false;
        }
        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
            builder.setTitle("Completed Task");
            //builder.setMessage("Task completed?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int position = viewHolder.getAdapterPosition();
                    Toast.makeText(getContext(), elements.get(position).getTaskName() + " completed", Toast.LENGTH_LONG).show();
                    SQLConnection connection = new SQLConnection(getActivity(), "bdRemedy", null, 1);
                    SQLiteDatabase db = connection.getWritableDatabase();
                    String param = String.valueOf(elements.get(position).getIdTask());
                    db.delete(DataBaseUtilities.dbTaskTable, DataBaseUtilities.dbTaskId + "=" + param, null);
                    db.close();
                    elements.remove(position);
                    listAdapter.notifyItemRemoved(position);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            builder.show();
        }
    };

    public void moveToTaskForm(){
        Bundle bundle = new Bundle();
        bundle.putInt("id",idTaskGroup);
        bundle.putString("name",TaskGroupName);
        final NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.action_taskGroup_to_dialogNewTask,bundle);
    }
}