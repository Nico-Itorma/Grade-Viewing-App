package com.nicanoritorma.gradeview;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import http_url_connection.PutData;

public class Activity_Student_Grades extends AppCompatActivity {

    private String student_name, student_idNum, subject_name, subject_yAndB;
    private ActionBar ab;
    private TextView tv_student_name, tv_student_idNum, tv_subject_name, tv_subject_yAndB;
    private EditText et_final_grade;
    private boolean haveGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_grades);

        Toolbar course_clicked_toolbar = findViewById(R.id.add_grade_toolbar);
        setSupportActionBar(course_clicked_toolbar);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        student_name = getIntent().getStringExtra("student_name");
        student_idNum = getIntent().getStringExtra("student_idNum");
        SharedPreferences preferences = getSharedPreferences("SUBJECT_INFO", MODE_PRIVATE);
        subject_name = preferences.getString("SUBJECT_NAME", "");
        subject_yAndB = preferences.getString("SUBJECT_YEARANDBLOCK", "");
        tv_student_name = findViewById(R.id.tv_addGrade_studentName);
        tv_student_idNum = findViewById(R.id.tv_addGrade_studentIDNum);
        tv_subject_name = findViewById(R.id.tv_addGrade_subject_name);
        tv_subject_yAndB = findViewById(R.id.tv_addGrade_yearandblock);
        et_final_grade = findViewById(R.id.et_addGrade_finalGrade);

        initUI();
    }

    private void initUI() {
        haveGrade = false;
        //TODO: change URL address
        String URL = "http://192.168.8.100/grade_view/getStudentGrade.php?student_username=" + student_idNum + "&subject_name=" + subject_name + "&subject_YandB=" + subject_yAndB;
        ab.setTitle("Add grade");
        tv_student_name.setText(student_name);
        tv_student_idNum.setText(student_idNum);
        tv_subject_name.setText(subject_name);
        tv_subject_yAndB.setText(subject_yAndB);

        String[] field = new String[3];
        field[0] = "student_username";
        field[1] = "subject_name";
        field[2] = "subject_YandB";

        String[] data = new String[3];
        data[0] = student_idNum;
        data[1] = subject_name;
        data[2] = subject_yAndB;

        PutData putData = new PutData(URL, "GET", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                et_final_grade.setText(result);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.assign_grade_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_save_grade) {

            //check for value of edittext final grade
            if (et_final_grade.getText().toString().trim().isEmpty()) {
                et_final_grade.setError("Invalid input");
            } else {
                assignGrade();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void assignGrade() {

        String grade = et_final_grade.getText().toString().trim();

        //TODO: change URL address
        String URL = "http://192.168.8.100/grade_view/assignGrade.php?student_username=" + student_idNum + "&subject_name=" + subject_name + "&subject_YandB=" + subject_yAndB
                + "&student_grade=" + grade + "&haveGrade=" + haveGrade;
        Log.d("URL ", URL);
        String[] field = new String[4];
        field[0] = "student_username";
        field[1] = "subject_name";
        field[2] = "subject_YandB";
        field[3] = "student_grade";

        String[] data = new String[4];
        data[0] = student_idNum;
        data[1] = subject_name;
        data[2] = subject_yAndB;
        data[3] = grade;

        PutData putData = new PutData(URL, "GET", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                Log.d("RESULT ", result);
                switch (result) {
                    case "Grade added":
                        et_final_grade.setText("");
                        onBackPressed();
                        break;
                    case "Grade not added":
                        alertDialog(result, "Error assigning grade", "There is an error assigning grade.");
                        break;
                    case "Grade exist":
                        alertDialog(result, "Warning", "Student already have grade");
                        break;
                }
            }
        }
    }

    //Popup alert dialog regarding result
    private void alertDialog(String result, String title, String message) {
        String btn_text;
        if (result.equals("Grade exist")) {
            btn_text = "Change student grade anyway";

            //override assigned grade for student
            AlertDialog dlg = new AlertDialog.Builder(Activity_Student_Grades.this).setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(btn_text, (dialog, which) -> {
                        haveGrade = true;
                        assignGrade();
                        dialog.dismiss();
                    }).setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).create();
            dlg.show();
        } else {
            btn_text = "Try Again";
            AlertDialog dlg = new AlertDialog.Builder(Activity_Student_Grades.this).setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(btn_text, (dialog, which) -> {
                        et_final_grade.setText("");
                        dialog.dismiss();
                    }).create();
            dlg.show();
        }

        haveGrade = false;
    }
}