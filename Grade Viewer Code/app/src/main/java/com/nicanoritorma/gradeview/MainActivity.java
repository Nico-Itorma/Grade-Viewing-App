package com.nicanoritorma.gradeview;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.nicanoritorma.gradeview.gettingStarted.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton fab;
    private String username;
    private int user_type;
    private RequestQueue rq;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private DrawerLayout drawer;
    private String header_name, header_idNum;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBar ab;
    private long onBackPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        rq = Volley.newRequestQueue(MainActivity.this);
        fab = findViewById(R.id.fab_addCourse);
        navigationView = findViewById(R.id.nav_view);
        fab.setOnClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        preferences = getSharedPreferences("USERDATA", MODE_PRIVATE);
        username = preferences.getString("USERNAME", "");
        if (username.length() > 0) {
            MyJsonParser();
        }
    }

    private void initUI() {
        TextView tv_header_name = findViewById(R.id.nav_tv_name);
        TextView tv_header_idNum = findViewById(R.id.nav_tv_idNum);
        tv_header_name.setText(header_name);
        tv_header_idNum.setText(header_idNum);
        drawer = findViewById(R.id.drawer_layout);
        ab.setTitle("Class List");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (user_type == 1) {
            fab.show();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new Fragment_CourseList()).commit();
        navigationView.setCheckedItem(R.id.menu_classList);
    }

    private void MyJsonParser() {
        //TODO: change URL address
        String url = "http://192.168.8.100/grade_view/getUserData.php?username=" + username;

        Handler handler = new Handler();
        handler.post(() -> {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                try {
                    JSONArray array = response.getJSONArray("userData");
                    for (int i = 0; i < array.length(); ++i) {
                        JSONObject userData = array.getJSONObject(i);
                        String Name = userData.getString("name");
                        String Username = userData.getString("username");
                        user_type = userData.getInt("userType");
                        header_name = Name;
                        header_idNum = username;

                        editor = preferences.edit();
                        editor.putString("NAME", Name);
                        editor.putString("USERNAME", Username);
                        editor.putInt("USERTYPE", user_type);
                        editor.apply();
                    }
                    initUI();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, Throwable::printStackTrace);

            rq.add(request);
        });
    }

    //when professor clicked the add button
    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, Activity_AddCourse.class));
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        int ITEM_ID = item.getItemId();

        if (ITEM_ID == R.id.menu_classList) {
            initUI();
        }
        else if (ITEM_ID == R.id.menu_profile) {
            fragmentTransaction.replace(R.id.framelayout, new Fragment_Profile()).commit();
            fab.hide();
            ab.setTitle("Profile");
            navigationView.setCheckedItem(R.id.menu_profile);
        }
        else if (ITEM_ID == R.id.menu_archiveClass)
        {
            //TODO: open archived class - fragment
        }
        else if (item.getItemId() == R.id.menu_logout) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        else if (item.getItemId() == R.id.menu_privacy) {
            //TODO: show privacy policy
            Toast.makeText(getBaseContext(), "Privacy Policy", Toast.LENGTH_SHORT).show();
            ab.setTitle(R.string.privacy_policy);
            navigationView.setCheckedItem(R.id.menu_privacy);
        }
        else if (item.getItemId() == R.id.menu_terms) {
            Toast.makeText(getBaseContext(), "Terms and Condition", Toast.LENGTH_SHORT).show();
            navigationView.setCheckedItem(R.id.menu_terms);
            ab.setTitle(R.string.terms_and_conditions);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}