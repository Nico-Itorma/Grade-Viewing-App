package com.nicanoritorma.gradeview.gettingStarted;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.nicanoritorma.gradeview.R;

import http_url_connection.PutData;

/*
    Activity to reset the password of a user when Reset Password is clicked in login page
 */
public class PasswordReset extends AppCompatActivity {

    private TextInputEditText et_reset_uname, et_pass1, et_pass2;
    private ProgressBar progress;
    private String click_came_from;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        click_came_from = getIntent().getStringExtra("CLICK_FLAG");
        SharedPreferences preferences = getSharedPreferences("USERDATA", MODE_PRIVATE);
        username = preferences.getString("USERNAME", "");
        String name = preferences.getString("NAME", "");
        et_reset_uname = findViewById(R.id.et_reset_username);
        et_pass1 = findViewById(R.id.et_reset_password1);
        et_pass2 = findViewById(R.id.et_reset_password2);
        Button reset_pass = findViewById(R.id.btn_reset_pass);
        progress = findViewById(R.id.progress_reset_pass);
        TextInputLayout et_reset_layout1 = findViewById(R.id.et_reset_layout1);
        View v1 = findViewById(R.id.reset_view1);
        TextView reset_title = findViewById(R.id.tv_reset_title);

        if (click_came_from.equals("Login click"))
        {
            et_reset_layout1.setVisibility(View.VISIBLE);
            v1.setVisibility(View.VISIBLE);
            String RESET_PASSWORD = "Reset Password";
            reset_title.setText(RESET_PASSWORD);
            reset_pass.setText(RESET_PASSWORD);
        }
        else
        {
            Toolbar toolbar = findViewById(R.id.reset_toolbar);
            toolbar.setVisibility(View.VISIBLE);
            setSupportActionBar(toolbar);
            ActionBar ab = getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);
            String CHANGE_PASSWORD = "Change Password";
            ab.setTitle(CHANGE_PASSWORD);
            reset_title.setTextSize(18);
            Typeface tf = ResourcesCompat.getFont(getApplicationContext(), R.font.poppinsmedium);
            reset_title.setTypeface(tf);
            String tv_reset_title = CHANGE_PASSWORD + " for: " + name;
            reset_title.setText(tv_reset_title);
            reset_pass.setText(CHANGE_PASSWORD);
        }

        //password length handler
        et_pass1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && s.length() < 8)
                {
                    et_pass1.setError("Password must be 8 characters");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //button reset password click handler
        reset_pass.setOnClickListener(v -> {
            String u_name;
            if (click_came_from.equals("Login click"))
            {
                u_name = et_reset_uname.getText().toString().trim();
            }
            else
            {
                u_name = username;
            }

            String new_pass1 = et_pass1.getText().toString().trim();
            String new_pass2 = et_pass2.getText().toString().trim();

            if (username.length() > 0)
            {
                if (new_pass1.equals(new_pass2))
                {
                    changePassword(u_name, new_pass2);
                }
            } else {
                et_reset_uname.setError("Required field");
            }
        });
    }

    private void changePassword(String u_name, String new_pass2)
    {
        //TODO: change URL address
        String URL = "http://192.168.8.100/grade_view/changePassword.php?username=" + username + "&password="+new_pass2;
        progress.setVisibility(View.VISIBLE);

        String[] field = new String[2];
        field[0] = "username";
        field[1] = "password";

        String[] data = new String[2];
        data[0] = u_name;
        data[1] = new_pass2;

        PutData putData = new PutData(URL, "GET", field, data);
        if (putData.startPut())
        {
            if (putData.onComplete())
            {
                String result = putData.getResult();
                progress.setVisibility(View.GONE);
                Log.d("Reset result ", result);
                switch (result) {
                    case "Password changed":
                        if (click_came_from.equals("Login click")) {
                            alertDialog("Reset password", "Reset password successfully");
                        } else {
                            alertDialog("Change password", "Change password successful. \n\nLogin again to continue");
                        }

                        break;
                    case "Error: username not found":
                        alertDialog("Error", result);
                        break;
                    case "Error: Database connection":
                        alertDialog("Connection problem", "Please try again later");
                        break;
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Password Reset ", "is onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Password Reset ", "is onDestroy");
    }

    //Popup alert dialog about the result
    private void alertDialog(String title, String message) {

        AlertDialog dlg = new AlertDialog.Builder(PasswordReset.this).setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                    et_pass1.setText("");
                    et_pass2.setText("");
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }).create();
        dlg.show();
    }

    @Override
    public void onBackPressed() {

        if (click_came_from.equals("Login click"))
        {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
        else {
            super.onBackPressed();
        }
    }
}