package com.example.remedy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remedy.Model.TaskGroupModel;
import com.example.remedy.R;

import java.util.List;

public class TaskGroupAdapter extends RecyclerView.Adapter<TaskGroupAdapter.ViewHolder> {

    private List<TaskGroupModel> listData;
    private LayoutInflater inflater;
    private Context adapterContext;
    final TaskGroupAdapter.OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(TaskGroupModel item);
    }


    public TaskGroupAdapter (List <TaskGroupModel> list, Context context, TaskGroupAdapter.OnItemClickListener listener){
        this.inflater= LayoutInflater.from(context);
        this.adapterContext = context;
        this.listData = list;
        this.listener=listener;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public TaskGroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.task_group, parent,false);

        return new TaskGroupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TaskGroupAdapter.ViewHolder holder, final int position) {
        holder.bindData(listData.get(position));

    }



    public void setItems(List <TaskGroupModel> items){
        listData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        androidx.appcompat.widget.AppCompatTextView taskGroupName;
        LinearLayoutCompat c;

        ViewHolder(View itemView){
            super(itemView);

            taskGroupName=itemView.findViewById(R.id.cvTaskGroupName);

        }
        void bindData(final TaskGroupModel item){

            taskGroupName.setText(item.getTaskGroupName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });

        }

    }


}
