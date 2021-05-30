package com.example.remedy.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remedy.Model.TaskModel;
import com.example.remedy.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Adapter  extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<TaskModel> listData;
    private LayoutInflater inflater;
    private Context adapterContext;

    public Adapter (List <TaskModel> list, Context context){

        this.inflater= LayoutInflater.from(context);
        this.adapterContext = context;
        this.listData = list;

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.task_card, parent,false);
        return new Adapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final Adapter.ViewHolder holder, final int position) {
        holder.bindData(listData.get(position));
    }


    public void setItems(List <TaskModel> items){
        listData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        androidx.appcompat.widget.AppCompatTextView taskName,taskText, taskDate, taskTime,taskReminder;


        ViewHolder(View itemView){
            super(itemView);

            taskName=itemView.findViewById(R.id.cvTaskName);
            taskText=itemView.findViewById(R.id.cvTaskText);
            taskDate=itemView.findViewById(R.id.cvDate);
            taskTime=itemView.findViewById(R.id.cvTime);
            taskReminder=itemView.findViewById(R.id.cvReminder);



        }


        void bindData(final TaskModel item){

            taskName.setText(item.getTaskName());
            taskText.setText(item.getTaskText());
            taskDate.setText(item.getTaskDate());
            taskTime.setText(item.getTaskTime());
            taskReminder.setText(item.getReminder());


        }

    }



}
