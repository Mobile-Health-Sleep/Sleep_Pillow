package edu.wit.mobile_health.pillow_companion.ui.popups;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import edu.wit.mobile_health.pillow_companion.MonitorActivity;
import edu.wit.mobile_health.pillow_companion.R;

public class BluetoothSeachFragment extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.popup_bluetooth);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                BluetoothSeachFragment.this.getDialog().cancel();
                MonitorActivity parent = (MonitorActivity) getActivity();
                parent.end();
            }
        });

        return builder.create();
    }
}
