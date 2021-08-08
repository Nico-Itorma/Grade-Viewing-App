package com.nicanoritorma.gradeview;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nicanoritorma.gradeview.adapters.Adapter_StudentList;
import com.nicanoritorma.gradeview.dataModels.Student_DataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
    Professor usertype Activity
    Activity to fetch and display the students added in a subject
 **/
public class Activity_StudentList_in_Course extends AppCompatActivity implements View.OnClickListener, Adapter_StudentList.OnSubjectInCourseClickListener {

    private RecyclerView recyclerView_studentList;
    private ArrayList<Student_DataModel> studentList;
    private String subject_name = null;
    private String yearAndBlock;
    private Adapter_StudentList adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list_in_course);

        preferences = getSharedPreferences("SUBJECT_INFO", MODE_PRIVATE);
        subject_name = preferences.getString("SUBJECT_NAME", "");
        yearAndBlock = preferences.getString("SUBJECT_YEARANDBLOCK", "");

        Toolbar course_clicked_toolbar = findViewById(R.id.course_clicked_tb);
        setSupportActionBar(course_clicked_toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(subject_name);

        FloatingActionButton fab_addStudent = findViewById(R.id.fab_addStudent);
        recyclerView_studentList = findViewById(R.id.students_recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh_studentList);
        recyclerView_studentList.setHasFixedSize(true);
        fab_addStudent.setOnClickListener(this);

        if (subject_name != null)
        {
            JSONParser();
        }

        //trigger when user swipe refresh the layout
        swipeRefreshLayout.setOnRefreshListener(this::JSONParser);
    }

    //parse the JSON data from the database about the list of students in the subject clicked
    private void JSONParser() {
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        studentList = new ArrayList<>();
        //TODO: change URL address
        String URL = "http://192.168.8.100/grade_view/getStudentList.php?subject_name=" + subject_name + "&YandB=" +yearAndBlock ;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, response -> {
            try {
                JSONArray array = response.getJSONArray("Student_List");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    String student_name = data.getString("name");
                    String student_idNum = data.getString("username");
                    String student_grade = data.getString("student_grade");

                    //add fetched data to arraylist for processing of studentList in UI
                    studentList.add(new Student_DataModel(student_name, student_idNum, student_grade));
                }
                //updates the UI after fetching data from db
                initUI();
                swipeRefreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        rq.add(request);
    }

    private void initUI() {
        adapter = new Adapter_StudentList(studentList, this);
        RecyclerView.LayoutManager RVlayout = new LinearLayoutManager(getApplicationContext());
        recyclerView_studentList.setLayoutManager(RVlayout);
        recyclerView_studentList.setAdapter(adapter);
    }

    //when professor clicked add button
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Activity_StudentList_in_Course.this, Activity_AddStudent.class);
        intent.putExtra("SUBJECT_NAME", subject_name);
        startActivity(intent);
    }

    //when professor click a student card view
    @Override
    public void onSubjectClick(int position) {
        Student_DataModel student = studentList.get(position);
        Intent intent = new Intent(getApplicationContext(), Activity_Student_Grades.class);
        intent.putExtra("student_name", student.getStudent_name());
        intent.putExtra("student_idNum", student.getStudent_idNum());
        startActivity(intent);
    }
}