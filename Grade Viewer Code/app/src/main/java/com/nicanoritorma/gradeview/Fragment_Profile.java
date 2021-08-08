package com.nicanoritorma.gradeview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.nicanoritorma.gradeview.gettingStarted.PasswordReset;

public class Fragment_Profile extends Fragment {

    private TextView tv_profile_name, tv_profile_idNum, tv_profile_subjects_enrolled, tv_profile2;
    private String profile_name, profile_idNum;
    private SharedPreferences preferences;
    private int userType = 0;
    private int course_total = 0;

    public Fragment_Profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getActivity().getSharedPreferences("USERDATA", Context.MODE_PRIVATE);
        profile_name = preferences.getString("NAME", "");
        profile_idNum = preferences.getString("USERNAME", "");
        userType = preferences.getInt("USERTYPE", 0);
        course_total = preferences.getInt("COURSE_TOTAL", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tv_profile_name = view.findViewById(R.id.tv_profile_name);
        tv_profile_idNum = view.findViewById(R.id.tv_profile_idNum);
        tv_profile_subjects_enrolled = view.findViewById(R.id.tv_profile_subjects_enrolled);
        tv_profile2 = view.findViewById(R.id.tv_profile2);
        CardView change_pass = view.findViewById(R.id.cardview3);

        //click handler when user click change password in profile fragment
        change_pass.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PasswordReset.class);
            intent.putExtra("CLICK_FLAG", "Fragment_Profile");
            startActivity(intent);
        });
        initUI();
        return view;
    }

    private void initUI()
    {
        if (userType == 1)
        {
            String PROF_SUB = "Subjects handled";
            tv_profile2.setText(PROF_SUB);
        }
        tv_profile_name.setText(profile_name);
        tv_profile_idNum.setText(profile_idNum);
        tv_profile_subjects_enrolled.setText(String.valueOf(course_total));
    }
}