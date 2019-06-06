package com.jusutech.easyshopugserver.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jusutech.easyshopugserver.Model.Request;
import com.jusutech.easyshopugserver.Model.User;

/**
 * Created by Junior Joseph on 1/8/2019.
 */

public class Common {
    public static User currentUser;
    public static Request currentRequest;
    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";
    public static final int PICK_IMAGE_REQUEST = 71;

    public static String clean_current_user_email;
    public static String current_user_email;
    public static String current_user_name;
    public static String current_user_phone;
    public static String distributor_name;
    public static String distributor_phone;

    public static String convertCodeToStatus(String status) {
        if(status.equals("0")){
            return "order being processed";
        }
        else if(status.equals("1")){
            return "Order being transported";
        }
        else {
            return "Order delivered";
        }
    }


    public static String convertCodeToReceived(String received) {
        if(received.equals("0")){
            return "Order not received";
        }
        else {
            return "Order received successfully";
        }
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

                }
            }
        }
        return false;
    }
}
