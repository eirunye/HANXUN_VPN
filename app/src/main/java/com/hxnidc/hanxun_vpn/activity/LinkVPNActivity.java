package com.hxnidc.hanxun_vpn.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by on 2017/1/10 13:11
 * Author：yrg
 * Describe:
 */


public class LinkVPNActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void getIU(){

        new Thread(){
            @Override
            public void run() {
                super.run();
                boolean isb =false;
                while (isb){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

            }

        }.start();
    }
}
