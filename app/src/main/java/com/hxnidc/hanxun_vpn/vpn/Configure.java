package com.hxnidc.hanxun_vpn.vpn;

import android.content.Context;
import android.net.VpnService;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by phoeagon on 15-1-14.
 */
public class Configure {

    static InputStream is = null;

    static void init(Context ctx) {
        try {
            is = ctx.getResources().getAssets().open("ip.txt");
        }catch(IOException e){
            Log.d("GFW_VPN", "Resource failed");
        }
    }
    static void operate(VpnService.Builder b) {
        if (is==null)
            return;
        String line = null;
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            while ((line = bf.readLine()) != null) {
                String[] s = line.split("/");
                b.addRoute(s[0], Integer.parseInt(s[1]));
            }
        }catch(Exception e){
            Log.d("GFW_VPN", "Failed to operate: " + line, e);
        }
    }
}
