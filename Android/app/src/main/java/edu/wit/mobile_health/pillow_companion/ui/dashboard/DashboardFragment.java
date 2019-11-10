package edu.wit.mobile_health.pillow_companion.ui.dashboard;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.text.SimpleDateFormat;
import java.util.Calendar;
//import java.util.Date;

import edu.wit.mobile_health.pillow_companion.MainActivity;
import edu.wit.mobile_health.pillow_companion.R;
import edu.wit.mobile_health.pillow_companion.data_collection.Date;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private MainActivity activity;

    private Date selectedDate;

    private TextView date;
    private DatePicker dateView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        activity = (MainActivity) getActivity();

        Calendar c = Calendar.getInstance();
        selectedDate = new Date(c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));

        date = root.findViewById(R.id.date_display);
        updateDate(selectedDate);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonShowPopupWindowClick(view);
            }
        });

        return root;
    }

    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                activity.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_calendar, null);

        // create the popup window
        int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        dateView = popupView.findViewById(R.id.datePicker);

        dateView.updateDate(selectedDate.getYear(), selectedDate.getMonth()-1, selectedDate.getDay());

        dateView.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                selectedDate.setMonth(i1+1);
                selectedDate.setDay(i2);
                selectedDate.setYear(i);
                updateDate(selectedDate);
                popupWindow.dismiss();
            }
        });

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


    }

    public void updateDate(Date currentDate){
        date.setText(currentDate.toString());
    }


}