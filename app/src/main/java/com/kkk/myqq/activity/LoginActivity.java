package com.kkk.myqq.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kkk.myqq.R;
import com.kkk.myqq.entity.LoginResult;
import com.kkk.myqq.utils.Constants;
import com.kkk.myqq.utils.NetUtils;

import butterknife.Bind;

/**
 * Created by kkk on 2016/5/25.
 * z3jjlzt.github.io
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final int WHAT_LOGIN = 1;

    @Bind(R.id.username_edit)
    EditText mUsernameEdit;
    @Bind(R.id.password_edit)
    EditText mPasswordEdit;
    @Bind(R.id.commit_button)
    Button mCommitButton;

    String usr_name, usr_pas;
    @Bind(R.id.btn_register)
    Button mBtnRegister;


    @Override
    protected void initVariables() {

    }

    @Override
    protected void bindUI(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void doLogic() {
      //
        mBtnRegister.setOnClickListener(this);
        mCommitButton.setOnClickListener(this);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_LOGIN) {
                LoginResult loginResult = (LoginResult) msg.obj;
                switch (loginResult.getState()) {
                    case LoginResult.LOGIN_NO_EXIST:
                        Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                        break;
                    case LoginResult.LOGIN_ERR_PASS:
                        Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                        break;
                    case LoginResult.LOGIN_SUCCESS:
                        Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                }

            } else if (msg.what == 2) {
                Toast.makeText(LoginActivity.this, "无网络", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void login() {
        usr_name = mUsernameEdit.getText().toString();
        usr_pas = mPasswordEdit.getText().toString();
        SharedPreferences.Editor edit = MyApp.getSp().edit();
        edit.putString("username", usr_name);
        edit.putString("password", usr_pas);
        edit.commit();

        final String param = "name=" + usr_name + "&password=" + usr_pas + "&submit=register";

        new Thread(new Runnable() {
            @Override
            public void run() {
                String login_addr = Constants.BASE_URL + "/Myqq/login.php";
                String txt = NetUtils.post(login_addr, param);
                LoginResult loginResult = new Gson().fromJson(txt, LoginResult.class);
                if (loginResult != null) {
                    Message message = handler.obtainMessage();
                    message.what = WHAT_LOGIN;
                    message.obj = loginResult;
                    handler.sendMessage(message);
                } else {
                    handler.sendEmptyMessage(2);
                }
            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit_button:
                login();
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    private void register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
