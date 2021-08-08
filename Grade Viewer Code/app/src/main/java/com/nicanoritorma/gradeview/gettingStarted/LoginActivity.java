package com.nicanoritorma.gradeview.gettingStarted;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.nicanoritorma.gradeview.MainActivity;
import com.nicanoritorma.gradeview.R;

import http_url_connection.PutData;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText username, password;
    private ProgressBar progress;
    private final String STUDENT_LOGIN_SUCCESS = "Login success";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private long onBackPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("USERDATA", MODE_PRIVATE);
        Button login = findViewById(R.id.btn_login);
        TextView signup = findViewById(R.id.no_account);
        username = findViewById(R.id.et_username_login);
        password = findViewById(R.id.et_password_login);
        progress = findViewById(R.id.progress_login);
        TextView change_password = findViewById(R.id.forgot_pass_login);
        change_password.setOnClickListener(this);
        login.setOnClickListener(this);

        //sign up user for a new accounts
        signup.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(i);
        });
    }

    //check for internet connection
    private boolean isConnected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    //when user clicked login button
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_login:
                String u_name = username.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if (isConnected()) {
                    Login(u_name, pass);
                }
                else {
                    alertDialog(getString(R.string.internet_connection), getString(R.string.empty_placeholder));
                }
                break;
            case R.id.forgot_pass_login:
                Intent intent = new Intent(getApplicationContext(), PasswordReset.class);
                intent.putExtra("CLICK_FLAG", "Login click");
                startActivity(intent);
                finish();
                break;
        }

    }

    //process the login connection to database
    private void Login(String u_name, String pass) {
        String EMPTY_ERROR = getString(R.string.required_field);
        if (u_name.isEmpty()) {
            username.setError(EMPTY_ERROR);
        }
        if (pass.isEmpty()) {
            password.setError(EMPTY_ERROR);
        } else {
            progress.setVisibility(View.VISIBLE);
            Handler handler = new Handler();
            handler.post(() -> {
                String[] field = new String[2];
                field[0] = "username";
                field[1] = "password";

                String[] data = new String[2];
                data[0] = u_name;
                data[1] = pass;

                //TODO: change URL address
                PutData putData = new PutData("http://192.168.8.100/grade_view/Login.php", "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();
                        Log.d("LOGIN RESULT ", result);
                        if (result.equals(STUDENT_LOGIN_SUCCESS)) {
                            progress.setVisibility(View.GONE);
                            username.setText("");
                            password.setText("");

                            editor = preferences.edit();
                            editor.putString("USERNAME", u_name);
                            editor.apply();

                            //login activity is finished and proceed to mainActivity
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else {
                            progress.setVisibility(View.GONE);
                            username.setText("");
                            password.setText("");
                            alertDialog("Incorrect credentials", "Username or Password is wrong");
                        }
                    }
                }
            });
        }
    }

    //Popup alert dialog if password or username is wrong
    private void alertDialog(String title, String message) {
        if (!isConnected())
        {
            message = "Please connect to internet to proceed further.";
        }
        AlertDialog dlg = new AlertDialog.Builder(LoginActivity.this).setTitle(title)
                .setMessage(message)
                .setPositiveButton("Try Again", (dialog, which) -> dialog.dismiss()).create();
        dlg.show();
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        onBackPressedTime = System.currentTimeMillis();
    }
}