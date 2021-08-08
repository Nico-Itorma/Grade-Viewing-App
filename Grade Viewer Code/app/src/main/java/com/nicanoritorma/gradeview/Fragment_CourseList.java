package com.nicanoritorma.gradeview;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.*;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import com.nicanoritorma.gradeview.adapters.Adapter_CourseList;
import com.nicanoritorma.gradeview.dataModels.Course_DataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

import static android.content.Context.MODE_PRIVATE;

/**
 * this fragment is about displaying the courses enrolled or added by the user
 * this fragment is executed in the MainActivity
 **/
public class Fragment_CourseList extends Fragment implements Adapter_CourseList.OnSubjectClickListener {

    private RecyclerView recyclerView_courselist;
    private ArrayList<Course_DataModel> courseList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String username;
    private String name = null;
    private int user_type = 0;
    private Adapter_CourseList adapter;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private RecyclerView.LayoutManager RVlayout;

    public Fragment_CourseList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getActivity().getSharedPreferences("USERDATA", MODE_PRIVATE);
        name = preferences.getString("NAME", "");
        username = preferences.getString("USERNAME", "");
        user_type = preferences.getInt("USERTYPE", 0);

        initUI();
    }

    private void initUI() {

        if (name != null) {
            if (user_type == 1) {
                String URL = "http://192.168.8.100/grade_view/getProfCourse.php?name=" + name;
                JsonParser(URL, "Department");
            } else if (user_type == 2) {
                String URL = "http://192.168.8.100/grade_view/getStudentCourse.php?student_idNum=" + username;
                JsonParser(URL, "name");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courselist, container, false);
        recyclerView_courselist = view.findViewById(R.id.courselist);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh_courseList);
        recyclerView_courselist.setHasFixedSize(true);
        adapter = new Adapter_CourseList(courseList, this);
        RVlayout = new LinearLayoutManager(getActivity());
        recyclerView_courselist.setLayoutManager(RVlayout);
        recyclerView_courselist.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this::initUI);
        return view;
    }

    //parse the JSON data from the database about the list of subjects
    private void JsonParser(String URL, String deptOrName) {

        RequestQueue rq = Volley.newRequestQueue(requireContext());
        courseList = new ArrayList<>();
        editor = preferences.edit();

        Handler handler = new Handler();
        handler.post(() -> {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, response -> {
                try {
                    JSONArray array = response.getJSONArray("subject_list");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject data = array.getJSONObject(i);
                        String course = data.getString("subject_name");
                        String YearAndBlock = data.getString("YearAndBlock");
                        String DeptOrName = data.getString(deptOrName);

                        //add fetched data to arraylist for processing of course list in UI
                        //set text view R.id.tv_prof_name in course_item to department when usertype is professor
                        courseList.add(new Course_DataModel(course, YearAndBlock, DeptOrName));

                    }
                    updateUI();

                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);

                    editor.putInt("COURSE_TOTAL", courseList.size());
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, Throwable::printStackTrace);
            rq.add(request);
        });
    }

    private void updateUI() {
        adapter = new Adapter_CourseList(courseList, this);
        RVlayout = new LinearLayoutManager(getActivity());
        recyclerView_courselist.setLayoutManager(RVlayout);
        recyclerView_courselist.setAdapter(adapter);
    }

    //when user click in a subject listed in class list
    @Override
    public void onSubjectClick(int position) {

        Course_DataModel course = courseList.get(position);
        preferences = getActivity().getSharedPreferences("SUBJECT_INFO", MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("SUBJECT_NAME", course.getCourse_name());
        editor.putString("SUBJECT_YEARANDBLOCK", course.getYearAndBlock());
        editor.putString("SUBJECT_PROFNAME", course.getProf_name());
        editor.apply();

        Intent intent;
        if (user_type == 1) {
            intent = new Intent(getActivity(), Activity_StudentList_in_Course.class);
        } else {
            intent = new Intent(getActivity(), Activity_ViewGradeByStudent.class);
        }
        startActivity(intent);
    }
}