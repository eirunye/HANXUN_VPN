package com.hxnidc.hanxun_vpn.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.VpnService;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxnidc.hanxun_vpn.R;
import com.hxnidc.hanxun_vpn.bean.MessageEvent;
import com.hxnidc.hanxun_vpn.utils.PrefUtils;
import com.hxnidc.hanxun_vpn.utils.ToastUtils;
import com.hxnidc.hanxun_vpn.vpn.GFWVpnService;
import com.hxnidc.hanxun_vpn.vpn.HxVpnService;
import com.hxnidc.hanxun_vpn.widget.InPutEditText;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import topsec.sslvpn.svsdklib.SVSDKLib;

import static android.app.Activity.RESULT_OK;

/**
 * Created by on 2017/1/10 14:48
 * Author：yrg
 * Describe:ＶＰＮ 页面
 */


public class VpnFragment extends Fragment {

    private String TAG = "VpnFragment";

    @BindView(R.id.et_link_info)
    InPutEditText etLinkInfo;
    @BindView(R.id.et_link_IpAddress)
    InPutEditText etLinkIpAddress;
    @BindView(R.id.et_link_userName)
    InPutEditText etLinkUserName;
    @BindView(R.id.et_link_password)
    InPutEditText etLinkPassword;
    @BindView(R.id.et_link_keys)
    InPutEditText etLinkKeys;
    @BindView(R.id.tv_vpn_link)
    TextView tvVpnLink;


    final static String Q_DNS = "180.76.76.76";
    final static String LOG_TAG = "HANXUN_VPN";//"GFW_VPN"
    static GFWVpnService vpn = new GFWVpnService();
    Context ctx = null;
    String dns = null;
    @BindView(R.id.ll_link_inout_info)
    LinearLayout llLinkInoutInfo;
    @BindView(R.id.img_vpn_link_try)
    ImageView imgVpnLink;
    @BindView(R.id.vpn_link)
    ImageView vpnLink_post;
    private String vpnInfo = "hanxun";
    private String vpnIp = "47.91.147.24";
    private String vpnUsername = "576864273";
    private String vpnPass = "741826";
    private String vpnKey = "";
    private static final int OK_NUM = 1;

    HxVpnService hxVpnService = new HxVpnService();
    public static final int VPN_MSG_STATUS_UPDATE = 0x100;
    public static final int QUERY_VPN_MSG_STATUS_UPDATE = 0x101; // VPN状态通知消息号
    private SVSDKLib vpnlib;
    private Context appContext;
    private boolean VPNStartFlag = false;
    private LoadingDialog loadingDialog;


    public static VpnFragment newInstance() {
        Bundle args = new Bundle();
        VpnFragment fragment = new VpnFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        ctx = getActivity();
        View view = inflater.inflate(R.layout.fragment_vpn, null);
        ButterKnife.bind(this, view);
        vpnlib = SVSDKLib.getInstance();
        // 设置VPN客户端的释放目录
        appContext = getActivity().getApplicationContext();
        initData();
        //initLinkData();
        //linkVPN();
        return view;
    }


    private void initLinkData() {

        vpnInfo = etLinkInfo.getText().toString();
        vpnIp = etLinkIpAddress.getText().toString();
        vpnUsername = etLinkUserName.getText().toString();
        vpnPass = etLinkPassword.getText().toString();
        vpnKey = etLinkKeys.getText().toString();

        if (TextUtils.isEmpty(vpnInfo) && TextUtils.isEmpty(vpnIp) && TextUtils.isEmpty(vpnUsername) && TextUtils.isEmpty(vpnPass)) {
            ToastUtils.showToast(getActivity(), getString(R.string.vpn_null_infos));
            return;
        }

        if (TextUtils.isEmpty(vpnIp)) {
            ToastUtils.showToast(getActivity(), getString(R.string.vpn_null_ip));
            return;
        }
        if (TextUtils.isEmpty(vpnUsername)) {
            ToastUtils.showToast(getActivity(), getString(R.string.vpn_null_username));
            return;
        }
        if (TextUtils.isEmpty(vpnPass)) {
            ToastUtils.showToast(getActivity(), getString(R.string.vpn_null_password));
            return;
        }

        linkVpnSet();

//
//        if (!VPNStartFlag) {
//            //linkVPN();
//            Toast.makeText(getActivity(), "正在链接", Toast.LENGTH_LONG).show();
//            linkVPNData();
//            //VPNStartFlag = true;
//        } else {
//            ToastUtils.showToast(getActivity(), "关闭");
//            SVSDKLib vpnlibs = SVSDKLib.getInstance();
//            vpnlibs.stopVPN();
//        }


    }

    private void linkVpnSet() {

        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.setLoadingText("加载中...");

        Log.e("LOADING", "loadong");

        Intent intent = VpnService.prepare(getActivity().getApplicationContext());
        if (intent == null) {
            onActivityResult(0, RESULT_OK, null);
            ToastUtils.showToast(getActivity(), "Hanxun Vpn Started");
        } else {
            startActivityForResult(intent, 0);
        }
    }


    private void initData() {

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = (String) v.getTag();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                getActivity().startActivity(i);
            }
        };

        vpnLink_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hxVpnService.isRunning()) {
                    showRunDiaog();//运行VPN中的弹出框
                } else {
                    if (!TextUtils.isEmpty(PrefUtils.getString("vpnIP", "", getActivity())) && VPNStartFlag != true) {
                        showDiaog();//显示默认弹出框
                    } else {
                        initLinkData();//填写要连接的弹出框
                    }
                }
            }
        });


        // addButton.setOnClickListener(listener);

        imgVpnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vpn.isRunning()) {
                    if (dns != null) {
                        configureDNS(dns);
                    }
                    vpn.onRevoke();
                    vpn.onDestroy();
                    ToastUtils.showToast(getActivity(), "Hanxun Vpn Stopped");
                    //btn.setColor(getResources().getColor(R.color.accent));
                    imgVpnLink.setImageResource(R.mipmap.img_vpn_link);
                } else {
                    backupDNS();
                    configureDNS(Q_DNS);
                    Intent i = VpnService.prepare(getActivity().getApplicationContext());
                    if (i == null) {
                        onActivityResult(0, RESULT_OK, null);
                    } else {
                        startActivityForResult(i, 0);
                    }
                    ToastUtils.showToast(getActivity(), "Hanxun Vpn Started");
                    //btn.setColor(getResources().getColor(R.color.accent_high));
                    imgVpnLink.setImageResource(R.mipmap.vpn_link_select);
                }
            }

        });

    }

    /**
     * 运行中VPN弹出框
     */
    private void showRunDiaog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("VPN正在运行中");
        //builder.setTitle("提示");
        builder.setPositiveButton("断开连接", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                hxVpnService.onRevoke();
                hxVpnService.onDestroy();
                ToastUtils.showToast(getActivity(), "Hanxun Vpn Stopped");
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //VPNStartFlag = true;
            }
        });
        builder.create().show();

    }

    /**
     * 默认连接方式弹出框
     */
    private void showDiaog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("请选择连接方式");
        //builder.setTitle("提示");
        builder.setPositiveButton("默认连接", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setEditData();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                VPNStartFlag = true;
            }
        });
        builder.create().show();
    }

    private void setEditData() {

        etLinkInfo.setText(PrefUtils.getString("vpnInfo", "", getActivity()));
        etLinkIpAddress.setText(PrefUtils.getString("vpnIP", "", getActivity()));
        etLinkUserName.setText(PrefUtils.getString("vpnUsername", "", getActivity()));
        etLinkPassword.setText(PrefUtils.getString("vpnPass", "", getActivity()));
        etLinkKeys.setText(PrefUtils.getString("vpnKey", "", getActivity()));
        //tvVpnLink.setText("连接中...");
        //连接数据
        linkVpnSet();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            String prefix = getActivity().getPackageName();
//            final Intent i = new Intent(getActivity().getApplicationContext(), GFWVpnService.class);
//            Thread t = new Thread() {
//                @Override
//                public void run() {
//                    getActivity().startService(i);
//                    Log.e("VPN", "vpn..................");
//                }
//            };
//            t.start();
//        } else
        if (resultCode == RESULT_OK && requestCode == 0) {
            String prefix = getActivity().getPackageName();

            final Intent intent = new Intent(getActivity().getApplicationContext(), HxVpnService.class);
            Bundle bundle = new Bundle();
            bundle.putString("vpnInfo", vpnInfo);
            bundle.putString("vpnIp", vpnIp);
            bundle.putString("vpnUsername", vpnUsername);
            bundle.putString("vpnKey", vpnKey);
            bundle.putString("vpnPass", vpnPass);
            intent.putExtras(bundle);
            //ToastUtils.showToast(getActivity(),"prepare");
            Log.e("TAG", "prepare......aaa");
            Thread t = new Thread() {
                @Override
                public void run() {
                    getActivity().startService(intent);
                }
            };
            t.start();
        }
        //super.onActivityResult(requestCode, resultCode, data);
    }

    private void backupDNS() {
//        DhcpInfo i = ((WifiManager) getSystemService(Context.WIFI_SERVICE)).getDhcpInfo();
//        dns = Formatter.formatIpAddress(i.dns1);
    }

    private void configureDNS(final String _dns) {
        if (RootTools.isAccessGiven()) {
            Command command = new Command(0, "setprop net.dns1 " + _dns);
            try {
                RootTools.getShell(true).add(command);
            } catch (Exception e) {
                Log.d(LOG_TAG, "DNS Configuration Failed", e);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {

        if (messageEvent != null) {
//            Log.e("VEN", messageEvent.getMessage() + "-----lllll");
//            if (loadingDialog != null) {
//                loadingDialog.close();
//            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void linkVPNData() {
        //SVSDKLib vpnlibs = SVSDKLib.getInstance();
        vpnlib.setVPNInfo(vpnIp, 900, vpnUsername, vpnPass);
        vpnlib.prepareVPNSettings();
        // 获取VPN库实例
        vpnlib.stopVPN();
        // 启动VPN连接
        vpnlib.startVPN();

        new GetVPNStatusThread().start();

        String sVPNStatus = vpnlib.getVPNStatus();

        ToastUtils.showToast(getActivity(), sVPNStatus);

        ArrayList<HashMap<String, String>> reslist = vpnlib.getResList();

        Log.e("test", "port1 :" + vpnlib.getResLocalPort("47.91.147.24", 900));
        Log.e("test", "port2 :" + vpnlib.getResLocalPort("47.91.147.24", 900));

    }

    private void linkVPN() {

        vpnlib.setSVClientPath(appContext.getFilesDir().getPath());
        // 设置应用程序的资产管理器
        vpnlib.setAppam(getActivity().getAssets());

        // 设置VPNSDK库的消息处理器
        vpnlib.setMsgHandler(handler);

        // 设置VPNSDK库的VPN状态变更消息号
        vpnlib.setVPNMsgID(VPN_MSG_STATUS_UPDATE);

        // 设置VPN连接信息
        vpnlib.setVPNInfo(vpnIp, 433, vpnUsername, vpnPass);
        // VPN客户端连接前的准备
        vpnlib.prepareVPNSettings();

    }


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int msgID = msg.what;
            Bundle bundle = (Bundle) msg.obj;
            switch (msg.what) {
                case VPN_MSG_STATUS_UPDATE: // VPN库发送消息处理
                {
                    if (null != bundle) {
                        String vpnStatus = bundle.getString("vpnstatus");
                        String vpnErr = bundle.getString("vpnerror");
                        if (vpnStatus.equalsIgnoreCase("6")) {
                            // VPN隧道建立成功
                            Log.e(TAG, "VPN库消息通知：VPN隧道建立成功");
                            // http://127.0.0.1:30080/cctv2/Jhsoft.mobileapp/login/loginbyurl.html?userName=trx&pwd=111111
                            ToastUtils.showToast(getActivity(), "VPN库消息通知：VPN隧道建立成功");

                        }
                        if (vpnStatus.equalsIgnoreCase("200")) {
                            ToastUtils.showToast(getActivity(), "VPN库消息通知：VPN隧道超时");

                        }

                        if (!vpnErr.equalsIgnoreCase("0")) {

                            if (vpnErr.equalsIgnoreCase("10")) {
                                Log.e(TAG,
                                        "VPN库消息通知：VPN需要重新登陆，可提示用户进行选择是否踢出上一个用户，现在是强行踢出上一个用户");
                                SVSDKLib vpnlib = SVSDKLib.getInstance();
                                vpnlib.reLoginVPN();

                            } else {
                                // VPN隧道建立出错

                                ToastUtils.showToast(getActivity(), "VPN库消息通知：当前VPN错误为：" + vpnErr);
                            }
                        }
                    }
                }
                break;
                case QUERY_VPN_MSG_STATUS_UPDATE: // 查询线程消息处理
                {
                    if (null != bundle) {
                        String vpnStatus = bundle.getString("vpnstatus");
                        String vpnErr = bundle.getString("vpnerror");
                        if (vpnStatus.equalsIgnoreCase("6")) {
                            // VPN隧道建立成功
                            ToastUtils.showToast(getActivity(), "查询线程通知：VPN隧道建立成功");
                        }

                        if (!vpnErr.equalsIgnoreCase("0")) {
                            // VPN隧道建立出错
                            ToastUtils.showToast(getActivity(), "查询线程通知:当前VPN错误为：" + vpnErr);
                        }
                    }
                }
                break;
            }
            super.handleMessage(msg);
        }
    };

    private class GetVPNStatusThread extends Thread {
        // @Override
        public void run() {
            String sVPNStatus;
            String sVPNErr;
            //SVSDKLib vpnlib = SVSDKLib.getInstance();

            int nTimeOut = 10; // 超时时间，10秒
            int nTimeStep = 0;

            try {
                while (nTimeStep < nTimeOut) {
                    sVPNStatus = vpnlib.getVPNStatus();
                    sVPNErr = vpnlib.getVPNError();
                    if (sVPNStatus.equalsIgnoreCase("6")) {
                        // 通知主进行VPN连接成功
                        Message msg = new Message();
                        msg.what = QUERY_VPN_MSG_STATUS_UPDATE;
                        sVPNStatus = "6";
                        Bundle bundle = new Bundle();
                        bundle.putString("vpnerror", sVPNErr);
                        bundle.putString("vpnstatus", sVPNStatus);
                        msg.obj = bundle;
                        //msg.sendToTarget();
                        handler.sendMessage(msg);
                        break;
                    }

                    if (!sVPNErr.equalsIgnoreCase("0")) {
                        // 通知主进行VPN连接出错了
                        Message msg = new Message();
                        msg.what = QUERY_VPN_MSG_STATUS_UPDATE;
                        Bundle bundle = new Bundle();
                        bundle.putString("vpnerror", sVPNErr);
                        bundle.putString("vpnstatus", sVPNStatus);
                        //msg.sendToTarget();
                        msg.obj = bundle;
                        handler.sendMessage(msg);
                        break;
                    }

                }
                Thread.sleep(1000);
                nTimeStep++;

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return;
        }
    }


}
