package com.nareshgediya.firebasenotesapp.NetWorkCheck;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import com.nareshgediya.firebasenotesapp.MainActivity;
import com.nareshgediya.firebasenotesapp.R;

public class CheckInterrnetConnetion extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String status = NetworkUtils.getNetworkStat(context);

        Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom);

        Button retry = dialog.findViewById(R.id.button);
        retry.setOnClickListener(view -> {
            ((Activity) context).finish();
            Intent intent1 = new Intent(context, MainActivity.class);
            context.startActivity(intent1);
        });

        if (status.isEmpty() || status.equals("No Internet Connection")) {
            dialog.show();
        }


    }
}
