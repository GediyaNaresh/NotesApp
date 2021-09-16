package com.nareshgediya.firebasenotesapp.NetWorkCheck;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
    public NetworkUtils() {
    }

    public static String getNetworkStat(Context context) {
        String status = null;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                status = "WiFi Connected";
                return status;

            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = "Mobile Data Connected";
                return status;
            }
        } else {
            status = "No Internet Connection";
            return status;
        }
        return status;
    }
}
