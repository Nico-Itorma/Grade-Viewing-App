package com.nicanoritorma.gradeview;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import http_url_connection.PutData;

/*
    activity to add student to the selected course in professor user account
 */

public class Activity_AddStudent extends AppCompatActivity implements View.OnClickListener{

    private TextInputEditText student_idNum;
    private String subject_name;
    private ProgressBar progress;
    private String yearAndBlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        initUI();
    }

    private void initUI() {
        SharedPreferences preferences = getSharedPreferences("SUBJECT_INFO", MODE_PRIVATE);
        Toolbar add_student_tb = findViewById(R.id.add_student_tb);
        setSupportActionBar(add_student_tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Add student");
        subject_name = preferences.getString("SUBJECT_NAME", "");
        yearAndBlock = preferences.getString("SUBJECT_YEARANDBLOCK", "");

        TextView subject = findViewById(R.id.subject_name);
        TextView subject_yearAndBlock = findViewById(R.id.subject_yearAndBlock);
        student_idNum = findViewById(R.id.et_student_idNum);
        Button add_student = findViewById(R.id.btn_addStudent);
        progress = findViewById(R.id.progress_addStudent);

        String LABEL1 = "Subject name: ";
        String LABEL2 = "Program and Section: ";
        String SUBJECT_NAME = LABEL1 + " " + subject_name;
        String Y_AND_B = LABEL2 + " " + yearAndBlock;
        subject.setText(SUBJECT_NAME);
        subject_yearAndBlock.setText(Y_AND_B);
        add_student.setOnClickListener(this);
    }

    //fab button click handler
    @Override
    public void onClick(View v) {
        AddStudent(student_idNum.getText().toString().trim());
    }

    //function to add the student in the subject clicked
    private void AddStudent(String idNum) {
        //TODO: change URL address
        String URL = "http://192.168.8.100/grade_view/addStudentToSubject.php?student_idNum=" + idNum + "&subject_name=" + subject_name + "&YandB=" + yearAndBlock;
        progress.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.post(() -> {
            String[] field = new String[3];
            field[0] = "student_idNum";
            field[1] = "subject_name";
            field[2] = "YandB";

            String[] data = new String[3];
            data[0] = idNum;
            data[1] = subject_name;
            data[2] = yearAndBlock;

            PutData putData = new PutData(URL, "GET", field, data);
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();
                    Log.d("ADD STUDENT ", result);
                    switch (result) {
                        case "Student added":
                            student_idNum.setText("");
                            progress.setVisibility(View.GONE);
                            startActivity(new Intent(Activity_AddStudent.this, Activity_StudentList_in_Course.class));
                            finish();
                            break;
                        case "ID number does not belong to a student":
                            progress.setVisibility(View.GONE);
                            alertDialog(result);
                            break;
                        case "Student already exist":
                            progress.setVisibility(View.GONE);
                            alertDialog(result);
                            break;
                        default:
                            progress.setVisibility(View.GONE);
                            alertDialog("Student ID number not found");
                            break;
                    }
                }
            }
        });
    }

    //Popup alert dialog if student_id number is not found
    private void alertDialog(String message) {

        AlertDialog dlg = new AlertDialog.Builder(Activity_AddStudent.this).setTitle("Incorrect student ID number")
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                    student_idNum.setText("");
                }).create();
        dlg.show();
    }
}