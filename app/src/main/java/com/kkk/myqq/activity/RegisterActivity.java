package com.kkk.myqq.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.kkk.myqq.R;
import com.kkk.myqq.entity.RegisterResult;
import com.kkk.myqq.utils.Constants;
import com.kkk.myqq.utils.NetUtils;

import butterknife.Bind;

/**
 * Created by kkk on 2016/5/28.
 * z3jjlzt.github.io
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.username_edit)
    EditText mUsernameEdit;
    @Bind(R.id.password_edit)
    EditText mPasswordEdit;
    @Bind(R.id.btn_register)
    Button mBtnRegister;
    private String usr_name;
    private String usr_pas;

    @Override
    protected void initVariables() {

    }

    @Override
    protected void bindUI(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void doLogic() {
        mBtnRegister.setOnClickListener(this);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                RegisterResult registerResult = (RegisterResult) msg.obj;
                switch (registerResult.getIsSuccess()) {
                    case RegisterResult.REGISERT_EXIST:
                        Toast.makeText(RegisterActivity.this, "用户已存在", Toast.LENGTH_SHORT).show();
                        break;
                    case RegisterResult.REGISERT_SUCCESS:
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                }
            } else if (msg.what == 2) {
                Toast.makeText(RegisterActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
            }
        }
    };


    private void register() {
        usr_name = mUsernameEdit.getText().toString();
        usr_pas = mPasswordEdit.getText().toString();
        final String param = "name=" + usr_name + "&password=" + usr_pas + "&submit=register";

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(usr_name  , usr_pas);
                } catch (HyphenateException e) {
                    Log.e("sb", e.getMessage());
                    handler.sendEmptyMessage(2);
                }
                String s = NetUtils.post(Constants.BASE_URL + "/Myqq/register.php", param);
                RegisterResult registerResult = new Gson().fromJson(s, RegisterResult.class);
                Log.e("sb", registerResult.toString());
                if (registerResult != null) {
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    message.obj = registerResult;
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
            case R.id.btn_register:
                register();
                break;
        }
    }
}
