package com.kkk.myqq.activity;

import android.app.Application;
import android.content.SharedPreferences;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

/**
 * Created by kkk on 2016/5/25.
 * z3jjlzt.github.io
 */
public class MyApp extends Application {
    private static SharedPreferences sp =null;
    @Override
    public void onCreate() {
        super.onCreate();
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        options.setAutoLogin(false);
//初始化
        EMClient.getInstance().init(this, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);

        sp = getSharedPreferences("userinfo", MODE_PRIVATE);

//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
    }

    public static SharedPreferences getSp() {
        return sp;
    }
}
