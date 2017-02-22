package com.hxnidc.hanxun_vpn.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by on 2017/1/11 14:39
 * Author：yrg
 * Describe:
 */


public class PostService extends Service {
    Timer timer;
    String BROADCASTACTION = "com.broadcast.post";
    String BROAD_NUM = "broad_num";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // 定时更新
                // 发送广播
                Intent intent = new Intent();
                intent.setAction(BROADCASTACTION);
                intent.putExtra("broad", BROAD_NUM);
                sendBroadcast(intent);
//               Message msg = handler.obtainMessage();
//               msg.what = UPDATAWEATHER;
//               handler.sendMessage( msg );
            }
        }, 0, 20 * 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
