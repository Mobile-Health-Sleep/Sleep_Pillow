package edu.wit.mobile_health.pillow_companion;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import edu.wit.mobile_health.pillow_companion.user.User;

public class UserDataActivity extends AppCompatActivity {

    private Button doneButton;
    private EditText height;
    private EditText weight;
    private EditText age;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File userFile = new File("data/data/edu.wit.mobile_health.pillow_companion/files", "User.json");

        if(userFile.exists()){

            //Load user info from json

            try {
                BufferedReader jsonReader = new BufferedReader(new FileReader(userFile));

                String line = new String();
                StringBuilder json = new StringBuilder();
                while ((line = jsonReader.readLine()) != null) {
                    json.append(line);
                }

                JSONObject user = new JSONObject(json.toString());

                loadMainActivity(user.getInt("height"), user.getInt("weight"), user.getInt("age"));


            }catch(Exception e){
                e.printStackTrace();
            }

        }else{
            //Take user input from UI

            setContentView(R.layout.activity_startup);

            doneButton = findViewById(R.id.next_button);
            height = findViewById(R.id.heightText);
            weight = findViewById(R.id.weightText);
            age = findViewById(R.id.ageText);

            doneButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    if(height.getText().toString().length() > 0 &&
                            weight.getText().toString().length() > 0 &&
                            age.getText().toString().length() > 0){

                        loadMainActivity(Integer.parseInt(height.getText().toString()),
                                Integer.parseInt(weight.getText().toString()),
                                Integer.parseInt(age.getText().toString()));
                    }

                }
            });

        }

    }

    private void loadMainActivity(int height, int weight, int age){
        Intent intent = new Intent();
        intent.setClass(UserDataActivity.this, MainActivity.class );

        Bundle bundle = new Bundle();
        bundle.putInt("height", height);
        bundle.putInt("weight", weight);
        bundle.putInt("age", age);

        intent.putExtras(bundle);

        startActivity(intent);
        finish();
    }


}
