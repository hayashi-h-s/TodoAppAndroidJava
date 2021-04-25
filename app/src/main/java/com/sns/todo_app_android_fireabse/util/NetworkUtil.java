package com.sns.todo_app_android_fireabse.util;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AlertDialog;

import com.sns.todo_app_android_fireabse.R;

public class NetworkUtil {

    /**
     * 通信できるか否かのを返す
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * 通信エラーダイアログを表示
     */
    public static void showNetWorkErrorDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.add_todo_activity_network_error_dialog_title)
                .setMessage(R.string.add_todo_activity_network_error_dialog_body)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int id) {

                    }
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog1, int id) {

                    }
                }).show();
    }

}
