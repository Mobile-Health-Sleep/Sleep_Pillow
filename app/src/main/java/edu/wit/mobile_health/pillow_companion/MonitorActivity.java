package edu.wit.mobile_health.pillow_companion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MonitorActivity extends AppCompatActivity {

    TextView alarm;
    Button done;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        Bundle bundle = this.getIntent().getExtras();

        alarm = findViewById(R.id.alarm_time);
        done = findViewById(R.id.done_button);

        alarm.setText(bundle.getInt("hour") + ":" + String.format("%02d", bundle.getInt("min")) + " " + bundle.getString("period"));


        done.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

    }
}
