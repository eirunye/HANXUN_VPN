package com.hxnidc.hanxun_vpn.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by on 2017/2/16 13:39
 * Author：yrg
 * Describe:
 */


public class ToastUtils {

    private static Toast toast;

    public static void showToast(Context context,
                                 String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}
