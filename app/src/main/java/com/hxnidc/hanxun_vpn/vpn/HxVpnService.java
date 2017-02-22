package com.hxnidc.hanxun_vpn.vpn;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.hxnidc.hanxun_vpn.bean.MessageEvent;
import com.hxnidc.hanxun_vpn.fragment.VpnFragment;
import com.hxnidc.hanxun_vpn.utils.PrefUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

/**
 * Created by phoeagon on 15-1-13.
 */
public class HxVpnService extends VpnService {
    private static ParcelFileDescriptor tunPFD = null;
    final static String LOG_TAG = "GFW_VPN";
    private String vpnIp = "";
    private String vpnUsername;
    private String vpnPass;
    private String vpnInfo;
    private String vpnKey;
    //private SharedPreferences preferences;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = (Bundle) intent.getExtras();

        if (bundle != null) {
            vpnIp = bundle.getString("vpnIp");
            vpnUsername = bundle.getString("vpnUsername");
            vpnPass = bundle.getString("vpnPass");
            vpnInfo = bundle.getString("vpnInfo");
            vpnKey = bundle.getString("vpnKey");
            Log.e("VpnService", "vpnInfo：" + vpnInfo + "\n" + "vpnUsername：" + vpnUsername + "\n" + vpnPass + "\n" + vpnIp + "\n" + vpnKey + "\n");
        }
        startVpn();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        //startVpn();
    }

    @Override
    public void onRevoke() {
        stopVpn();
    }

    public void startVpn() {
        try {
            if (tunPFD != null) {
                throw new RuntimeException("another VPN is still running");
            }
            Intent statusActivityIntent = new Intent(getApplicationContext(), VpnFragment.class);
            PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, statusActivityIntent, 0);
            Builder b = new Builder();
            b.setConfigureIntent(pIntent)
                    .setSession("HanXun")
                    .addAddress(vpnIp, 24)
                    .addDnsServer("168.126.63.1")
                    .addRoute("0.0.0.0", 0)
                    .setMtu(1280);
            tunPFD = b.establish();
            FileInputStream in = new FileInputStream(tunPFD.getFileDescriptor());
            FileOutputStream out = new FileOutputStream(tunPFD.getFileDescriptor());
            DatagramChannel t= DatagramChannel.open();
            t.connect(new InetSocketAddress("182.162.103.38",8888));
            protect(t.socket());
//            for (;;){
//                Thread.sleep(100);
//            }


            Configure.init(getApplicationContext());
            Configure.operate(b);

            tunPFD = b.establish();

            if (tunPFD == null) {
                stopSelf();
                Log.i(LOG_TAG, "Launch failed!");
                return;
            }
            PrefUtils.putString("vpnIP", vpnIp, this);
            PrefUtils.putString("vpnUsername", vpnUsername, this);
            PrefUtils.putString("vpnPass", vpnPass, this);
            PrefUtils.putString("vpnInfo", vpnInfo, this);
            PrefUtils.putString("vpnKey", vpnKey, this);
            Log.e(LOG_TAG, "Successfully launched.");

            EventBus.getDefault().post(new MessageEvent("连接成功"));

        } catch (Exception e) {
            Log.e(LOG_TAG, "VPN establish failed", e);
        }
    }

    public static boolean isRunning() {
        return tunPFD != null;
    }

    public void stopVpn() {
        if (tunPFD != null) {
            try {
                tunPFD.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "failed to stop tunPFD", e);
            }
            tunPFD = null;
        }
        stopSelf();
        Log.i(LOG_TAG, "Successfully exited.");
    }

    public void toggleVpn() {
        if (isRunning())
            stopVpn();
        else startVpn();
    }
}
