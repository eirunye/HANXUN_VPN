package com.hxnidc.hanxun_vpn.vpn;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.hxnidc.hanxun_vpn.fragment.VpnFragment;

import java.io.IOException;

/**
 * Created by phoeagon on 15-1-13.
 */
public class GFWVpnService extends VpnService {
    private static ParcelFileDescriptor tunPFD = null;
    final static String LOG_TAG = "GFW_VPN";

    @Override
    public void onStart(Intent intent, int startId) {
        startVpn();
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
            Intent statusActivityIntent = new Intent(this, VpnFragment.class);
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, statusActivityIntent, 0);
            VpnService.Builder b = new Builder()
                    .setConfigureIntent(pIntent)
                    .setSession("hanxun-router")
                    .addAddress("47.91.147.24", 24)//addAddress("10.25.1.1", 24)
                    //.addDnsServer("8.8.8.8")//addDnsServer("114.114.114.114")
                    //.addDnsServer("180.76.76.76")//addDnsServer("180.76.76.76")
                    .setMtu(1280);

            Configure.init(getApplicationContext());
            Configure.operate(b);

            tunPFD = b.establish();

            if (tunPFD == null) {
                stopSelf();
                Log.i(LOG_TAG, "Launch failed!");
                return;
            }
            Log.e(LOG_TAG, "Successfully launched.");
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
