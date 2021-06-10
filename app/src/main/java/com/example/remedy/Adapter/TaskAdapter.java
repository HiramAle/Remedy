package com.example.remedy.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remedy.Model.TaskModel;
import com.example.remedy.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TaskAdapter  extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<TaskModel> listData;
    private LayoutInflater inflater;
    private Context adapterContext;

    public TaskAdapter (List <TaskModel> list, Context context){

        this.inflater= LayoutInflater.from(context);
        this.adapterContext = context;
        this.listData = list;

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.task_card, parent,false);
        return new TaskAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final TaskAdapter.ViewHolder holder, final int position) {
        holder.bindData(listData.get(position));
    }



    public void setItems(List <TaskModel> items){
        listData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        androidx.appcompat.widget.AppCompatTextView taskName,taskText, taskDate, taskTime,taskReminder,taskContact;
        ImageButton contact;
        ImageView dateIcon,timeIcon,reminderIcon,contactIcon;
        TextView taskGroupName = itemView.findViewById(R.id.taskTitle);


        ViewHolder(View itemView){
            super(itemView);
            contact = itemView.findViewById(R.id.cvContactOptions);
            taskName=itemView.findViewById(R.id.cvTaskName);
            taskText=itemView.findViewById(R.id.cvTaskText);
            taskDate=itemView.findViewById(R.id.cvDate);
            taskTime=itemView.findViewById(R.id.cvTime);
            taskReminder=itemView.findViewById(R.id.cvReminder);
            taskContact=itemView.findViewById(R.id.cvContact);
            dateIcon=itemView.findViewById(R.id.cvDateIcon);
            timeIcon=itemView.findViewById(R.id.cvTimeIcon);
            reminderIcon=itemView.findViewById(R.id.cvReminderIcon);
            contactIcon=itemView.findViewById(R.id.cvContactIcon);
        }


        void bindData(final TaskModel item){
            taskName.setText(item.getTaskName());
            if(item.getTaskText().isEmpty()){
                taskText.setVisibility(View.GONE);
            }else{
                taskText.setText(item.getTaskText());
            }
            if(item.getTaskDate().isEmpty()){
                dateIcon.setVisibility(View.GONE);
                taskDate.setVisibility(View.GONE);
            }else{
                taskDate.setText(item.getTaskDate());
            }
            if (item.getTaskTime().isEmpty()){
                timeIcon.setVisibility(View.GONE);
                taskTime.setVisibility(View.GONE);
            }else{
                taskTime.setText(item.getTaskTime());
            }
            if (item.getReminder().isEmpty()){
                reminderIcon.setVisibility(View.GONE);
                taskReminder.setVisibility(View.GONE);
            }else{
                taskReminder.setText(item.getReminder());
            }
            if (item.getContactName().isEmpty()){
                contactIcon.setVisibility(View.GONE);
                taskContact.setVisibility(View.GONE);
                contact.setVisibility(View.GONE);
            }else{
                taskContact.setText(item.getContactName());
            }
            
            taskName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    Bundle bundle = new Bundle();
                    bundle.putInt("idCase",0);
                    bundle.putString("nameCase","Edit");
                    bundle.putInt("taskId",item.getIdTask());
                    bundle.putString("taskName",item.getTaskName());
                    bundle.putString("taskText",item.getTaskText());
                    bundle.putString("taskDate",item.getTaskDate());
                    bundle.putString("taskTime",item.getTaskTime());
                    bundle.putString("taskReminder",item.getReminder());
                    bundle.putString("contactName",item.getContactName());
                    bundle.putString("contactNumber",item.getContactNumber());

                    bundle.putInt("idTaskGroup",item.getIdTaskGroup());
                    //Toast.makeText(adapterContext, String.valueOf(item.getIdTaskGroup()), Toast.LENGTH_SHORT).show();

                    final NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_taskGroup_to_dialogNewTask,bundle);



                    return false;
                }
            });





            contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(adapterContext, item.getContactNumber(), Toast.LENGTH_SHORT).show();
                    PopupMenu menusito = new PopupMenu(adapterContext,contact);
                    menusito.getMenuInflater().inflate(R.menu.contact_options_menu,menusito.getMenu());
                    menusito.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            String option = menuItem.getTitle().toString();
                            switch (option){
                                case "Call":
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:"+item.getContactNumber()));
                                    v.getContext().startActivity(intent);
                                    break;
                                case "Send Message":
                                    Intent intent1 = new Intent(Intent.ACTION_SENDTO);
                                    intent1.setData(Uri.parse("smsto:"+item.getContactNumber()));
                                    v.getContext().startActivity(intent1);
                                    break;
                            }
                            return true;
                        }
                    });
                    menusito.show();
                }
            });
        }

        
    }
}
