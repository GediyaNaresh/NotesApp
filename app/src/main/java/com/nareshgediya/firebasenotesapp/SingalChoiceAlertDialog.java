package com.nareshgediya.firebasenotesapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SingalChoiceAlertDialog extends DialogFragment {
    int position = 0;

    public interface SingleChoiceListner{
        void  OnpositiveBtnClick(String [] list, int position);
    }
    SingleChoiceListner listner;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listner = (SingleChoiceListner)context;

        }catch (Exception e){

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        String [] list = {"Sort by Oldest","Sort by Latest"};

        alertDialog.setTitle("Sort").setSingleChoiceItems(list, position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                position = i;
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listner.OnpositiveBtnClick(list,position);
            }
        });
return alertDialog.create();
    }




}
