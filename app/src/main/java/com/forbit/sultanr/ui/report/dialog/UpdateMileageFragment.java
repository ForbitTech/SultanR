package com.forbit.sultanr.ui.report.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.forbit.sultanr.R;
import com.forbit.sultanr.utils.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateMileageFragment extends DialogFragment {

    private UpdateMilageListener listener;

    public void setListener(UpdateMilageListener listener){
        this.listener= listener;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(false);

        String titleText ="Update Mileage";

        // Initialize a new foreground color span instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.WHITE);

        // Initialize a new spannable string builder instance
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);

        // Apply the text color span
        ssBuilder.setSpan(
                foregroundColorSpan,
                0,
                titleText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme).create();
        alertDialog.setTitle(ssBuilder);
        alertDialog.setCancelable(false);

        View v = getActivity().getLayoutInflater().inflate(R.layout.update_mileage, null);

        final EditText etMilage = v.findViewById(R.id.mileage);

        double mileage = getArguments().getDouble(Constant.MILEAGE);

        etMilage.setText(String.valueOf(mileage));


        //final DatePicker datePicker = v.findViewById(R.id.dialog_date_datePicker);

        alertDialog.setView(v);

        Drawable drawable = ContextCompat.getDrawable(getActivity(),R.drawable.form);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.setTint(getResources().getColor(android.R.color.holo_orange_dark));
        }
        alertDialog.setIcon(drawable);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            }
        });



        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(etMilage.getText().toString().trim().equals("")){
                    Toast.makeText(getActivity(), "Amount is Empty", Toast.LENGTH_SHORT).show();
                    etMilage.requestFocus();
                    dialogInterface.cancel();
                    return;
                }

                final double milage = Double.parseDouble(etMilage.getText().toString().trim());

                if(milage>0){
                    if(listener!=null){
                        listener.update(milage);
                    }
                }

            }
        });

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            }
        });

        return alertDialog;
    }

}
