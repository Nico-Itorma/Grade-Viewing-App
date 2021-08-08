package com.nicanoritorma.gradeview;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import http_url_connection.PutData;

public class Activity_ViewGradeByStudent extends AppCompatActivity {

    private String student_username,subject_name, yearAndBlock, profName;
    private TextView tv_subject_name, tv_YandB, tv_profName, tv_finalGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_grade_by_student);

        SharedPreferences preferences = getSharedPreferences("SUBJECT_INFO", MODE_PRIVATE);
        SharedPreferences pref = getSharedPreferences("USERDATA", MODE_PRIVATE);
        student_username = pref.getString("USERNAME", "");
        subject_name = preferences.getString("SUBJECT_NAME", "");
        yearAndBlock = preferences.getString("SUBJECT_YEARANDBLOCK", "");
        profName = preferences.getString("SUBJECT_PROFNAME", "");

        tv_subject_name = findViewById(R.id.tv_gradeView_subjectName);
        tv_YandB = findViewById(R.id.tv_gradeView_YandB);
        tv_profName = findViewById(R.id.tv_gradeView_profName);
        tv_finalGrade = findViewById(R.id.tv_gradeView_finalGrade);

       initUI();
    }

    private void initUI() {
        Toolbar toolbar = findViewById(R.id.grade_view);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Final Grade");
        tv_subject_name.setText(subject_name);
        tv_YandB.setText(yearAndBlock);
        tv_profName.setText(profName);

        getStudentGrade();
    }

    private void getStudentGrade()
    {
        //TODO: change URL address
        String URL = "http://192.168.8.100/grade_view/getStudentGrade.php?student_username="+student_username+"&subject_name="+subject_name+"&subject_YandB="+yearAndBlock;

        String [] field = new String[3];
        field[0] = "student_username";
        field[1] = "subject_name";
        field[2] = "subject_YandB";

        String [] data = new String[3];
        data[0] = student_username;
        data[1] = subject_name;
        data[2] = yearAndBlock;

        PutData putData = new PutData(URL, "GET", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                Log.d("RESULT ", result);
                tv_finalGrade.setText(result);
            }
        }
    }

}