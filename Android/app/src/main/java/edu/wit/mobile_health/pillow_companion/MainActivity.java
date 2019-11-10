package edu.wit.mobile_health.pillow_companion;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import edu.wit.mobile_health.pillow_companion.ui.dashboard.DashboardFragment;
import edu.wit.mobile_health.pillow_companion.ui.popups.TimePickerFragment;
import edu.wit.mobile_health.pillow_companion.user.User;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    private User currentUser;

    private Button newSession;
    private TimePickerDialog timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_insights, R.id.navigation_dashboard, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //Import bundle
        Bundle bundle = this.getIntent().getExtras();

        //Grab current user from bundle and export to json
        currentUser = new User(bundle.getInt("height"), bundle.getInt("weight"), bundle.getInt("age"));
        currentUser.exportToJson();


        newSession = findViewById(R.id.new_sleep_button);

        newSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onButtonShowPopupWindowClick(view);


            }
        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public void resetUser(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, UserDataActivity.class);

        startActivity(intent);

        finish();
    }

    public User getUser(){
        return this.currentUser;
    }

    public void onButtonShowPopupWindowClick(View view) {

        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, MonitorActivity.class );

        Bundle bundle = new Bundle();
        int hour = -1;
        String period = null;

        if(hourOfDay == 0){
            hour = 12;
            period = "AM";
        }else if(hourOfDay > 0 && hourOfDay < 12){
            hour = hourOfDay;
            period = "AM";
        }else if (hourOfDay == 12){
            hour = hourOfDay;
            period = "PM";
        }else if(hourOfDay > 12){
            hour = hourOfDay - 12;
            period = "PM";
        }

        bundle.putInt("hour", hour);
        bundle.putInt("min", minute);
        bundle.putString("period", period);

        intent.putExtras(bundle);

        startActivity(intent);
    }

}
