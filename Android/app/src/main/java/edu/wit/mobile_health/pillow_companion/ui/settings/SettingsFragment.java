package edu.wit.mobile_health.pillow_companion.ui.settings;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;

import edu.wit.mobile_health.pillow_companion.MainActivity;
import edu.wit.mobile_health.pillow_companion.R;
import edu.wit.mobile_health.pillow_companion.user.User;

public class SettingsFragment extends Fragment {
    private MainActivity activity;

    private SettingsViewModel settingsViewModel;

    private Button reset;

    private EditText height;
    private EditText weight;
    private EditText age;

    private User user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        reset = root.findViewById(R.id.reset_user);

        height = root.findViewById(R.id.height_text_box);
        weight = root.findViewById(R.id.weight_text_box);
        age = root.findViewById(R.id.age_text_box);

        activity = (MainActivity) getActivity();
        user = activity.getUser();

        height.setText(Integer.toString(user.getHeight()));
        weight.setText(Integer.toString(user.getWeight()));
        age.setText(Integer.toString(user.getAge()));


        /**
         * Set Listener Section (I honestly should probably make methods to do this)
         */


        reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                File file = new File("data/data/edu.wit.mobile_health.pillow_companion/files/User.json");
                file.delete();

                activity.resetUser();

            }
        });

        height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    user.setHeight(Integer.parseInt(charSequence.toString()));
                    user.exportToJson();
                }else{
                    user.setHeight(0);
                    user.exportToJson();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    user.setWeight(Integer.parseInt(charSequence.toString()));
                    user.exportToJson();
                }else{
                    user.setWeight(0);
                    user.exportToJson();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    user.setAge(Integer.parseInt(charSequence.toString()));
                    user.exportToJson();
                }else{
                    user.setAge(0);
                    user.exportToJson();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return root;
    }



}