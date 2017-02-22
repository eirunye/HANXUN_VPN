package com.hxnidc.hanxun_vpn.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxnidc.hanxun_vpn.R;
import com.hxnidc.hanxun_vpn.fragment.AboutFragment;
import com.hxnidc.hanxun_vpn.fragment.HanXunFragment;
import com.hxnidc.hanxun_vpn.fragment.VpnFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.fl_main_content)
    FrameLayout flMainContent;
    @BindView(R.id.img_main_hanxun)
    ImageView imgMainHanxun;
    @BindView(R.id.tv_main_hanxun)
    TextView tvMainHanxun;
    @BindView(R.id.ll_main_hanXun)
    LinearLayout llMainHanXun;
    @BindView(R.id.img_main_vpn)
    ImageView imgMainVpn;
    @BindView(R.id.tv_main_vpn)
    TextView tvMainVpn;
    @BindView(R.id.ll_main_vpn)
    LinearLayout llMainVpn;
    @BindView(R.id.img_main_we)
    ImageView imgMainWe;
    @BindView(R.id.tv_main_we)
    TextView tvMainWe;
    @BindView(R.id.ll_main_we)
    LinearLayout llMainWe;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;

    private FragmentManager manager;
    private HanXunFragment hanXunFragment;
    private VpnFragment vpnFragment;
    private AboutFragment aboutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        manager = getSupportFragmentManager();

        selectTab(1);

    }

    private void selectTab(int index) {

        FragmentTransaction transaction = manager.beginTransaction();

        clearSelectTab();

        hintTab(transaction);

        switch (index) {
            case 0:
                imgMainHanxun.setImageResource(R.mipmap.hanxun_icon_sel);
                tvMainHanxun.setTextColor(getResources().getColor(R.color.mainText));
                if (hanXunFragment == null) {
                    hanXunFragment = HanXunFragment.newInstance();
                    transaction.add(R.id.fl_main_content, hanXunFragment);
                } else {
                    transaction.show(hanXunFragment);
                }
                break;
            case 1:
                imgMainVpn.setImageResource(R.mipmap.vpn_icon_sel);
                tvMainVpn.setTextColor(getResources().getColor(R.color.mainText));

                if (vpnFragment == null) {
                    vpnFragment = VpnFragment.newInstance();
                    transaction.add(R.id.fl_main_content, vpnFragment);
                } else {
                    transaction.show(vpnFragment);
                }
                break;
            case 2:
                imgMainWe.setImageResource(R.mipmap.we_icon_sel);
                tvMainWe.setTextColor(getResources().getColor(R.color.mainText));
                if (aboutFragment == null) {
                    aboutFragment = AboutFragment.newInstance();
                    transaction.add(R.id.fl_main_content, aboutFragment);
                } else {
                    transaction.show(aboutFragment);
                }
                break;

        }

        transaction.commit();


    }

    private void clearSelectTab() {

        imgMainHanxun.setImageResource(R.mipmap.hanxun_icon_nor);
        imgMainVpn.setImageResource(R.mipmap.vpn_icon_nor);
        imgMainWe.setImageResource(R.mipmap.we_icon_nor);
        tvMainHanxun.setTextColor(getResources().getColor(R.color.mainTextNor));
        tvMainVpn.setTextColor(getResources().getColor(R.color.mainTextNor));
        tvMainWe.setTextColor(getResources().getColor(R.color.mainTextNor));


    }

    private void hintTab(FragmentTransaction transaction) {

        if (hanXunFragment != null) {
            transaction.hide(hanXunFragment);
        }

        if (vpnFragment != null) {
            transaction.hide(vpnFragment);
        }

        if (aboutFragment != null) {
            transaction.hide(aboutFragment);
        }
    }

    @OnClick({R.id.ll_main_hanXun, R.id.ll_main_vpn, R.id.ll_main_we})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_main_hanXun:
                selectTab(0);
                break;
            case R.id.ll_main_vpn:
                selectTab(1);
                break;
            case R.id.ll_main_we:
                selectTab(2);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }
}
