package com.example.remedy;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.remedy.DataBase.DataBaseUtilities;
import com.example.remedy.DataBase.SQLConnection;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class DialogNewTaskGroup extends BottomSheetDialogFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Set the Dialog visible even if the keyboard is On
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return inflater.inflate(R.layout.dialog_new_taskgroup, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton btnAdd = view.findViewById(R.id.dialogSubmit);
        EditText editText = view.findViewById(R.id.dialogText);
        try {
            if (getArguments().getInt("idCase")==0){
                String option = getArguments().getString("nameCase");
                switch (option){
                    case "Add":
                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (editText.getText().toString().isEmpty()){
                                    Toast.makeText(v.getContext(), "Task Group name required", Toast.LENGTH_SHORT).show();
                                }else{
                                    addTaskGroupDB(getNameGroup(view));
                                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_dialogNewTaskGroup_to_Home);
                                }
                            }
                        });
                        break;
                    case "Edit":
                        editText.setText(getArguments().getString("taskGroupName"));
                        //Toast.makeText(getContext(),getArguments().getString("taskGroupName"),Toast.LENGTH_SHORT).show();
                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editGroup(view);
                                Bundle bundle = new Bundle();
                                bundle.putInt("id",getArguments().getInt("taskGroupId"));
                                bundle.putString("name",getNameGroup(view));
                                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_dialogNewTaskGroup_to_taskGroup,bundle);
                            }
                        });
                        break;
                }
            }
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String getNameGroup(View view){
        EditText editText = view.findViewById(R.id.dialogText);
        String value = editText.getText().toString();
        return value;
    }

    private void addTaskGroupDB(String nameGroup){
        SQLConnection connection = new SQLConnection(getActivity(),"bdRemedy",null,1);
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("taskGroupName",nameGroup);
        db.insert(DataBaseUtilities.dbTaskGroupTable,DataBaseUtilities.dbTaskGroup_Id,values);
        db.close();
    }

    public void editGroup(View view){
        SQLConnection connection = new SQLConnection(getActivity(),"bdRemedy",null,1);
        SQLiteDatabase db = connection.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("taskGroupName",getNameGroup(view));
        db.update("taskGroup",content,"idTaskGroup = "+getArguments().getInt("taskGroupId"),null);
        db.close();
    }

}