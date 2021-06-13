package com.example.remedy;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;
import androidx.navigation.fragment.NavHostFragment;

import com.example.remedy.DataBase.DataBaseUtilities;
import com.example.remedy.DataBase.SQLConnection;
import com.example.remedy.Model.TaskGroupModel;
import com.example.remedy.Notification.AlertReceiver;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.Calendar;

public class DialogNewTask extends BottomSheetDialogFragment {

    private static final int CONTACT_PERMISSION_CODE = 1;
    private static final int CONTACT_PICK_CODE = 2;
    int idTaskGroup;
    String TaskGroupName;
    Calendar calendarAlarm = Calendar.getInstance();
    String contactNumber;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Set the Dialog visible even if the keyboard is On
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return inflater.inflate(R.layout.dialog_new_task, container, false);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton btnAdd = view.findViewById(R.id.submitDatabase);
        getGroup(view);
        getDate(view);
        getTime(view);
        getReminder(view);
        getContact(view);
        try {
            if(getArguments().getString("nameCase")=="Edit"){
                EditText taskName = view.findViewById(R.id.editTextTaskName);
                EditText taskText= view.findViewById(R.id.editTextTaskText);
                TextView taskDate = view.findViewById(R.id.dialogNewTaskDateText);
                TextView taskTime = view.findViewById(R.id.dialogNewTaskTimeText);
                TextView taskReminder = view.findViewById(R.id.dialogNewTaskReminderText);
                TextView taskContact = view.findViewById(R.id.dialogNewTaskContactText);
                TextView taskGroup = view.findViewById(R.id.dialogNewTaskGroupName);
                //Toast.makeText(getContext(), "Tarea+"+getArguments().getString("taskDate"), Toast.LENGTH_SHORT).show();
                taskName.setText(getArguments().getString("taskName"));
                taskText.setText(getArguments().getString("taskText"));
                taskDate.setText(getArguments().getString("taskDate"));
                taskTime.setText(getArguments().getString("taskTime"));
                taskReminder.setText(getArguments().getString("taskReminder"));
                taskContact.setText(getArguments().getString("contactName"));

                //Toast.makeText(getContext(), getArguments().getInt("idTaskGroup"), Toast.LENGTH_SHORT).show();

                SQLConnection connection = new SQLConnection(getActivity(),"bdRemedy",null,1);
                SQLiteDatabase db = connection.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT taskGroupName FROM taskGroup WHERE taskGroupId = "+getArguments().getInt("idTaskGroup") ,null);
                while(cursor.moveToNext()){
                    taskGroup.setText(cursor.getString(0));
                    //Toast.makeText(getContext(), "Yes", Toast.LENGTH_SHORT).show();
                }
                db.close();





                //taskGroup.setText(getArguments().getString("task"));
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SQLConnection connection = new SQLConnection(getActivity(),"bdRemedy",null,1);
                        SQLiteDatabase db = connection.getWritableDatabase();
                        ContentValues content = new ContentValues();
                        content.put("taskName",taskName.getText().toString());
                        content.put("taskText",taskText.getText().toString());
                        //content.put(DataBaseUtilities.dbTask_TaskGroupId,idTaskGroup);
                        content.put("date",taskDate.getText().toString());
                        content.put("time",taskTime.getText().toString());
                        content.put("reminder",taskReminder.getText().toString());
                        content.put("contactName",taskContact.getText().toString());
                        content.put("contactNumber",contactNumber);
                        db.update("task",content,"idTask = "+getArguments().getInt("taskId"),null);
                        db.close();

                        Bundle bundle = new Bundle();
                        bundle.putInt("id",getArguments().getInt("idTaskGroup"));
                        bundle.putString("name",taskGroup.getText().toString());
                        //Toast.makeText(getContext(), "NewTaskDialog: "+), Toast.LENGTH_SHORT).show();

                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_dialogNewTask_to_taskGroup,bundle);
                    }
                });
            }else{
                EditText eTaskName = getView().findViewById(R.id.editTextTaskName);
                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (eTaskName.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "Task Name Required", Toast.LENGTH_SHORT).show();
                        }else{
                            addTaskDB();
                            Bundle bundle = new Bundle();
                            bundle.putInt("id",idTaskGroup);
                            bundle.putString("name",TaskGroupName);
                            NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_dialogNewTask_to_taskGroup,bundle);
                        }

                    }
                });
            }
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();

        }






    }

    public void putArguments(View view){

    }



    public void getContact(View view){
        ImageButton btnContact = view.findViewById(R.id.contactButton);
        TextView textContact = view.findViewById(R.id.dialogNewTaskContactText);
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkContactPermission()){
                    pickContactIntent();
                }else{
                    requestContactPermission();
                }
            }
        });
    }

    private boolean checkContactPermission(){
        boolean result = ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_CONTACTS) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestContactPermission(){
        String [] permission = {Manifest.permission.READ_CONTACTS};
        ActivityCompat.requestPermissions(getActivity(),permission,CONTACT_PERMISSION_CODE);
    }

    private void pickContactIntent(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent,CONTACT_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CONTACT_PERMISSION_CODE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                pickContactIntent();
            }else{
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1){
            if (requestCode == CONTACT_PICK_CODE){


                Cursor cursor1,cursor2;
                Uri uri = data.getData();

                cursor1 = getActivity().getContentResolver().query(uri,null,null,null,null);
                if (cursor1.moveToFirst()){
                    String contactId = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
                    String contactName = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String idResults = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    int idResultHold = Integer.parseInt(idResults);
                    TextView textContact = getView().findViewById(R.id.dialogNewTaskContactText);
                    textContact.setText(contactName);


                    if (idResultHold==1){
                        cursor2 = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = "+contactId,
                                null,
                                null);
                        while (cursor2.moveToNext()){
                            contactNumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            //textContact.append(","+contactNumber);
                        }
                        cursor2.close();
                    }
                    cursor1.close();

                }
            }
        }else{

        }
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
                        if (minute==0){
                            timeText.setText(String.valueOf(hourOfDay)+":"+String.valueOf(minute)+"0");
                        }else {
                            timeText.setText(String.valueOf(hourOfDay)+":"+String.valueOf(minute));
                        }
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
        TextView eContactName = getView().findViewById(R.id.dialogNewTaskContactText);
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
        values.put("contactName",eContactName.getText().toString());
        values.put("contactNumber",contactNumber);
        //values.put("reminder","true");
        db.insert("task","idTask",values);
        Toast.makeText(getActivity().getApplicationContext(), "ID: "+contactNumber,Toast.LENGTH_LONG).show();
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

        Toast.makeText(getContext(),"Reminder Set",Toast.LENGTH_SHORT).show();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),1,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),pendingIntent);
    }





}