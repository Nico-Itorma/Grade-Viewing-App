package com.nicanoritorma.gradeview.gettingStarted;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.nicanoritorma.gradeview.R;

import http_url_connection.PutData;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button student, professor;
    private TextInputEditText fullname, username, password, et_password_confirm;
    private final String SIGNUP_SUCCESS = "Sign up success";
    private final String USERNAME_EXIST = "Username already exist";
    private final String PASSWORD_TOO_SHORT = "Password must be 8 characters";
    private ProgressBar progress;
    private Boolean isStudent = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button signup = findViewById(R.id.btn_sign_up);
        fullname = findViewById(R.id.et_fullname_sign_up);
        username = findViewById(R.id.et_username_sign_up);
        student = findViewById(R.id.student_rank);
        professor = findViewById(R.id.professor_rank);
        password = findViewById(R.id.et_password_sign_up);
        et_password_confirm = findViewById(R.id.et_password_sign_up_confirm);
        progress = findViewById(R.id.progress);
        signup.setOnClickListener(this);
        student.setOnClickListener(this);
        professor.setOnClickListener(this);

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && s.length() < 8) {
                    password.setError(PASSWORD_TOO_SHORT);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        String userType;
        switch (v.getId()) {
            case R.id.btn_sign_up:
                String f_name = fullname.getText().toString().trim();
                String u_name = username.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String password_confirm = et_password_confirm.getText().toString().trim();

                if (isStudent)
                {
                    userType = "Student";
                }
                else {
                    userType = "Professor";
                }
                Signup_checkFields(f_name, u_name, pass, userType, password_confirm);
                break;
            case R.id.student_rank:
                isStudent = true;
                professor.setBackgroundColor(Color.parseColor("#838383"));
                student.setBackgroundColor(Color.parseColor("#3AD29F"));
                break;
            case R.id.professor_rank:
                isStudent = false;
                student.setBackgroundColor(Color.parseColor("#838383"));
                professor.setBackgroundColor(Color.parseColor("#3AD29F"));
                break;

        }
    }

    private void Signup_checkFields(String f_name, String u_name, String pass, String userType, String password_confirm) {
        String EMPTY_ERROR = "Required field";
        if (f_name.isEmpty()) {
            fullname.setError(EMPTY_ERROR);
        }
        if (u_name.isEmpty()) {
            username.setError(EMPTY_ERROR);
        }
        if (pass.isEmpty()) {
            password.setError(EMPTY_ERROR);
        }
        if (password_confirm.isEmpty())
        {
            et_password_confirm.setError(EMPTY_ERROR);
        }
        if (!pass.equals(password_confirm))
        {
            String PASSWORD_DO_NOT_MATCH = "Password do not match";
            password.setError(PASSWORD_DO_NOT_MATCH);
            et_password_confirm.setError(PASSWORD_DO_NOT_MATCH);
        }
        if (!isConnected())
        {
           alertDialog("Internet Connection", "", "");
        }
        else {
            Signup(f_name, u_name, pass, userType);
        }
    }

    private boolean isConnected()
    {
        //check if we are connected to a network
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void Signup(String f_name, String u_name, String pass, String userType) {
        progress.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.post(() -> {
            String[] field = new String[4];
            field[0] = "name";
            field[1] = "username";
            field[2] = "password";
            field[3] = "userType";

            String[] data = new String[4];
            data[0] = f_name;
            data[1] = u_name;
            data[2] = pass;
            data[3] = userType;

            //TODO: change URL address
            PutData putData = new PutData("http://192.168.8.100/grade_view/Signup.php", "POST", field, data);
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    String result = putData.getResult();
                    Log.d("SIGNUP RESULT ", result);
                    if (result.equals(SIGNUP_SUCCESS)) {
                        progress.setVisibility(View.GONE);
                        fullname.setText("");
                        username.setText("");
                        password.setText("");
                        alertDialog("Signup successful", "Account successfully created. \n\nPlease log in to continue.", "Log in");
                    } else if (result.equals(USERNAME_EXIST)) {
                        username.setError(USERNAME_EXIST);
                    }
                    progress.setVisibility(View.GONE);
                }
            }
        });
    }

    //Popup alert dialog if password or username is wrong
    private void alertDialog(String title, String message, String button_message) {
        if (!isConnected())
        {
            message = "Please connect to internet to sign up.";
            button_message = "Try Again";
        }

        AlertDialog dlg = new AlertDialog.Builder(SignupActivity.this).setTitle(title)
                .setMessage(message)
                .setPositiveButton(button_message, (dialog, which) -> {
                    dialog.dismiss();
                    if (title.equals("Signup successful"))
                    {
                        onBackPressed();
                        finish();
                    }
                }).create();
        dlg.show();
    }
}
