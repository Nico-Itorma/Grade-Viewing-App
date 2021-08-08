package com.nicanoritorma.gradeview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import http_url_connection.PutData;
/*
    activity to handle adding course for professor
 */
public class Activity_AddCourse extends AppCompatActivity {

    private EditText et_course, et_program, et_college, et_grade_level, et_section;
    private final String COURSE_ADDED = "Course added";
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        Toolbar AC_toolbar = findViewById(R.id.AC_toolbar);
        setSupportActionBar(AC_toolbar);
        AC_toolbar.setTitle("Create class");
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        preferences = getSharedPreferences("USERDATA", Context.MODE_PRIVATE);
        et_course = findViewById(R.id.et_course);
        et_program = findViewById(R.id.et_program);
        et_grade_level= findViewById(R.id.et_grade_level);
        et_section = findViewById(R.id.et_section);
        et_college = findViewById(R.id.et_college);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.assign_grade_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_save_grade)
        {
            String course = et_course.getText().toString().trim();
            String professorName = preferences.getString("NAME", "");
            String program = et_program.getText().toString().trim();
            String gradeLevel = et_grade_level.getText().toString().trim();
            String section = et_section.getText().toString().trim();
            String college = et_college.getText().toString().trim();

            CheckFields(course, professorName, program, gradeLevel, section, college);
        }
        return super.onOptionsItemSelected(item);
    }

    //check the edittext fields if there is blank space
    private void CheckFields(String course, String profName, String program, String gradeLevel, String section, String college) {
        String EMPTY_ERROR = "Required field";
        if (course.isEmpty()) {
            et_course.setError(EMPTY_ERROR);
        }
        else {
            String newProgram = program + " " +gradeLevel + " " +section;
            AddCourse(course, profName, newProgram, college);
        }
    }

    private void AddCourse(String course, String profName, String yearAndBlock, String Department) {
        Handler handler = new Handler();
        handler.post(() -> {
            String[] field = new String[4];
            field[0] = "subject_name";
            field[1] = "ProfName";
            field[2] = "YearAndBlock";
            field[3] = "Department";

            String[] data = new String[4];
            data[0] = course;
            data[1] = profName;
            data[2] = yearAndBlock;
            data[3] = Department;

            //TODO: change URL address
            PutData putData = new PutData("http://192.168.8.100/grade_view/addProfCourse.php", "POST", field, data);
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();
                    if (result.equals(COURSE_ADDED)) {
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                        //progress.setVisibility(View.GONE);
                        et_course.setText("");
                        et_program.setText("");
                        et_grade_level.setText("");
                        et_section.setText("");
                        et_college.setText("");

                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    }
                    Log.d("RESULT", result);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Add Course Activity ", "is onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Add Course Activity ", "is onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Add Course Activity ", "is onDestroy");
    }
}