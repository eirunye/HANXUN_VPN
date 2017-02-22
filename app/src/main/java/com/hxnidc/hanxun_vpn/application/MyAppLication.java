package com.hxnidc.hanxun_vpn.application;

import android.app.Application;

import com.xiasuhuei321.loadingdialog.manager.StyleManager;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

/**
 * Created by on 2017/1/10 14:59
 * Author：yrg
 * Describe:
 */


public class MyAppLication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        StyleManager sm = new StyleManager();
        //在这里调用方法设置s的属性
        //code here...
        sm.Anim(false).repeatTime(0).contentSize(-1).intercept(true);

        LoadingDialog.initStyle(sm);
    }
}
