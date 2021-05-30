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
        Button btnAdd = view.findViewById(R.id.dialogSubmit);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskGroupDB(getNameGroup(view));
                Bundle bundle = new Bundle();
                NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_dialogNewTaskGroup_to_Home);
            }
        });
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

}