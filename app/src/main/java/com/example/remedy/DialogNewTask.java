package com.example.remedy;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.widget.TextViewCompat;
import androidx.navigation.fragment.NavHostFragment;

import com.example.remedy.DataBase.DataBaseUtilities;
import com.example.remedy.DataBase.SQLConnection;
import com.example.remedy.Notification.AlertReceiver;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class DialogNewTask extends BottomSheetDialogFragment {

    int idTaskGroup;
    String TaskGroupName;
    Calendar calendarAlarm = Calendar.getInstance();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Set the Dialog visible even if the keyboard is On
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return inflater.inflate(R.layout.dialog_new_task, container, false);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getGroup(view);
        getDate(view);
        getTime(view);
        getReminder(view);

        Button btnAdd = view.findViewById(R.id.submitDatabase);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskDB();
                Bundle bundle = new Bundle();
                bundle.putInt("id",idTaskGroup);
                bundle.putString("name",TaskGroupName);
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_dialogNewTask_to_taskGroup,bundle);
            }
        });

    }

    public void getReminder(View view){
        ImageButton btnReminder = view.findViewById(R.id.reminderButton);
        TextView textReminder = view.findViewById(R.id.dialogNewTaskReminderText);
        btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menusito = new PopupMenu(getContext(),btnReminder);
                menusito.getMenuInflater().inflate(R.menu.reminder_menu,menusito.getMenu());
                menusito.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        textReminder.setText(item.getTitle());
                        return true;
                    }
                });
                menusito.show();
            }
        });
    }

    public void getGroup(View view){
        TextView taskGroupName = view.findViewById(R.id.dialogNewTaskGroupName);
        try {
            idTaskGroup=getArguments().getInt("id");
            TaskGroupName=getArguments().getString("name");
        }catch (Exception e){

        }
        taskGroupName.setText(TaskGroupName);
    }

    public void getDate(View view){
        ImageButton btnDate = view.findViewById(R.id.dialogDateButton);
        TextView dateText = view.findViewById(R.id.dialogNewTaskDateText);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int tYear = calendar.get(Calendar.YEAR);
                int tMonth = calendar.get(Calendar.MONTH);
                int tDay = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int pYear, int pMonth, int pDayOfMonth) {
                        dateText.setText(String.valueOf(pDayOfMonth)+"/"+String.valueOf(pMonth)+"/"+String.valueOf(pYear));
                        calendarAlarm.set(Calendar.YEAR,pYear);
                        calendarAlarm.set(Calendar.MONTH,pMonth);
                        calendarAlarm.set(Calendar.DAY_OF_MONTH,pDayOfMonth);
                    }
                },tYear,tMonth,tDay);
                datePicker.show();
            }
        });
    }

    public void getTime(View view){
        ImageButton btnTime = view.findViewById(R.id.timeButton);
        TextView timeText = view.findViewById(R.id.dialogNewTaskTimeText);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeText.setText(String.valueOf(hourOfDay)+":"+String.valueOf(minute));
                        calendarAlarm.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendarAlarm.set(Calendar.MINUTE,minute);
                        calendarAlarm.set(Calendar.SECOND,0);
                    }
                },hour,minutes,false);
                timePickerDialog.show();
            }
        });
    }

    private void addTaskDB(){
        EditText eTaskName = getView().findViewById(R.id.editTextTaskName);
        EditText eTaskText = getView().findViewById(R.id.editTextTaskText);
        TextView dateText = getView().findViewById(R.id.dialogNewTaskDateText);
        TextView timeText = getView().findViewById(R.id.dialogNewTaskTimeText);
        TextView reminderText = getView().findViewById(R.id.dialogNewTaskReminderText);
        SQLConnection connection = new SQLConnection(getActivity(),"bdRemedy",null,1);
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("taskName",eTaskName.getText().toString());
        values.put("taskText",eTaskText.getText().toString());
        values.put(DataBaseUtilities.dbTask_TaskGroupId,idTaskGroup);
        values.put("date",dateText.getText().toString());
        values.put("time",timeText.getText().toString());
        values.put("reminder",reminderText.getText().toString());
        //values.put("reminder","true");
        db.insert("task","idTask",values);
        //Toast.makeText(getActivity().getApplicationContext(), "ID: "+idDB,Toast.LENGTH_LONG).show();
        db.close();
        setReminder(reminderText.getText().toString());
    }

    public void setReminder(String option){
        switch (option){
            case "No Reminder":
                break;
            case "On Date":
                startAlarm(calendarAlarm);
                break;
            case "10 Minutes Before":
                int tenminutes;
                int minutes = calendarAlarm.get(Calendar.MINUTE);
                if(minutes<10){
                    tenminutes = 60 + minutes -10;
                }else{
                    tenminutes = calendarAlarm.get(Calendar.MINUTE)-10;
                }
                calendarAlarm.set(Calendar.MINUTE,tenminutes);
                startAlarm(calendarAlarm);
                break;
            case "1 Day Before":
                int oneday;
                int days = calendarAlarm.get(Calendar.DAY_OF_MONTH-1);
                calendarAlarm.set(Calendar.DAY_OF_MONTH,days);
                startAlarm(calendarAlarm);
                break;
        }

    }


    public void startAlarm(Calendar c){
        EditText eTaskName = getView().findViewById(R.id.editTextTaskName);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getContext().ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        Bundle bundle = new Bundle();

        bundle.putString("Title",eTaskName.getText().toString());
        bundle.putString("Message",TaskGroupName);


        intent.putExtra("bundle",bundle);

        Toast.makeText(getContext(),TaskGroupName,Toast.LENGTH_SHORT).show();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),1,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),pendingIntent);
    }





}