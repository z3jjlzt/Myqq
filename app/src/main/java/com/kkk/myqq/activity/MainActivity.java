package com.kkk.myqq.activity;


import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.kkk.myqq.R;
import com.kkk.myqq.adapter.recyclerAdapter;
import com.kkk.myqq.entity.Friend;
import com.kkk.myqq.entity.SetSignResult;
import com.kkk.myqq.fragment.Fragment_Friend;
import com.kkk.myqq.utils.Constants;
import com.kkk.myqq.utils.NetUtils;
import com.kkk.myqq.utils.UploadUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements Fragment_Friend.IFriend, View.OnClickListener {

    ImageView mHeadIv;
    @Bind(R.id.nick_tv)
    TextView mNickTv;
    TextView mSignTv;
    @Bind(R.id.tab_message)
    LinearLayout ll_message;
    @Bind(R.id.tab_contacts)
    LinearLayout ll_contaces;
    @Bind(R.id.tab_news)
    LinearLayout ll_news;
    @Bind(R.id.tab_setting)
    LinearLayout ll_setting;

    private List<Friend> mFriendList;
    private Fragment_Friend mFragment_friend;
    private recyclerAdapter mRecycleAdapter;
    private String usr_name;
    private String usr_pas;
    private String mehead;


    @Override
    protected void initVariables() {
        mFriendList = new ArrayList<>();
        mRecycleAdapter = new recyclerAdapter(this, mFriendList);
        usr_name = MyApp.getSp().getString("username", "");
        usr_pas = MyApp.getSp().getString("password", "");
    }

    @Override
    protected void bindUI(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        mHeadIv = (ImageView) findViewById(R.id.ll_top).findViewById(R.id.head_iv);
        mSignTv = (TextView) findViewById(R.id.ll_top).findViewById(R.id.sign_tv);
        mFragment_friend = new Fragment_Friend();
        getFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mFragment_friend)
                .commit();
    }

    @Override
    protected void doLogic() {
        getFriendInfo();
        loginHuanxin();
        mHeadIv.setOnClickListener(this);
        mSignTv.setOnClickListener(this);
    }


    /**
     * 登入环信
     */
    private void loginHuanxin() {


        EMClient.getInstance().login(usr_name, usr_pas, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        Toast.makeText(MainActivity.this, usr_name + "登录聊天服务器成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, usr_name + "登录聊天服务器失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }


    public void getFriendInfo() {
        NetUtils.getApi().getFriend().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<List<Friend>, Observable<Friend>>() {
                    @Override
                    public Observable<Friend> call(List<Friend> friends) {
                        return Observable.from(friends);
                    }
                })
                .subscribe(new Subscriber<Friend>() {
                    @Override
                    public void onCompleted() {

                        handler.sendEmptyMessage(3);
                        //  setSelfInfo();
                        mRecycleAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("sb", e.getMessage());
                    }

                    @Override
                    public void onNext(Friend friend) {
                        Log.e("sb", "" + friend);
                        if (friend.getName().equals(usr_name))
                            mehead = friend.getHead();
                        mFriendList.add(friend);
                    }
                });
    }

    private void setSelfInfo() {
        for (Friend friend : mFriendList) {
            if (friend.getName().equals(usr_name)) {
                Glide.with(MainActivity.this)
                        .load(Constants.BASE_URL + "/Myqq" + friend.getHead())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                        .into(mHeadIv);
                mNickTv.setText(friend.getName());
                mSignTv.setText(friend.getSignature());
                break;
            }
        }
    }


    @Override
    public void updateInfo(RecyclerView recyclerView) {

        mRecycleAdapter.setOnItemClickListener(new recyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Friend friend) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("name", friend.getName());
                intent.putExtra("head", friend.getHead());
                intent.putExtra("meurl", mehead);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mRecycleAdapter);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String head = (String) msg.obj;
                    newhead(head);
                    break;
                case 2:
                    Toast.makeText(MainActivity.this, "更新头像失败",
                            Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    setSelfInfo();
                    break;
                case 4:
                    Toast.makeText(MainActivity.this, "更新签名成功",
                            Toast.LENGTH_SHORT).show();
                    newSign((String) msg.obj);
                    break;
                case 5:
                    Toast.makeText(MainActivity.this, "更新签名失败",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        private void newhead(String head) {
            Glide.with(MainActivity.this)
                    .load(Constants.BASE_URL + "/Myqq/" + head)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(mHeadIv);
            for (int i = 0; i < mFriendList.size(); i++) {
                if (mFriendList.get(i).getName().equals(usr_name)) {
                    Friend friend = mFriendList.get(i);
                    friend.setHead(head);
                   // mFriendList.set(i, friend);
                    mRecycleAdapter.notifyItemChanged(i);
                    break;
                }
            }
            Toast.makeText(MainActivity.this, "更新头像成功", Toast.LENGTH_SHORT).show();
        }
    };

    private void newSign(String txt) {
        mSignTv.setText(txt);
        for (int i = 0; i < mFriendList.size(); i++) {
            if (mFriendList.get(i).getName().equals(usr_name)) {
                Friend friend = mFriendList.get(i);
                friend.setSignature(txt);
                mFriendList.set(i, friend);
                mRecycleAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String path = null;
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    String[] pojo = {MediaStore.Images.Media.DATA};
                    ContentResolver cr = this.getContentResolver();
                    Cursor cursor = cr.query(uri, pojo, null, null, null);
                    if (cursor != null) {
                        int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                        cursor.moveToFirst();
                        path = cursor.getString(columnIndex);
                        cursor.close();
                    }
                    Log.e("sb", path);
                    new UploadUtil().uploadFile(path, usr_name + ".jpg", Constants.BASE_URL + "/Myqq/setHead.php", handler);

                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_iv:
                selectImg();
                Log.e("sb", "------------------------");
                break;
            case R.id.sign_tv:
                setNewSign();
                break;
        }
    }

    private void setNewSign() {
        Log.e("sb", "setsign===========================");
        View view = LayoutInflater.from(this).inflate(R.layout.input_dialog, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final EditText editText = (EditText) view.findViewById(R.id.et_sign);
        builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String sign = editText.getText().toString();
                        if (!editText.getText().toString().equals("")) {
                            final String param = "name=" + usr_name + "&sign=" + sign + "&submit=setSign";
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String txt = NetUtils.post(Constants.BASE_URL + "/Myqq/setSign.php", param);
                                    SetSignResult result = new Gson().fromJson(txt, SetSignResult.class);
                                    if (result != null) {
                                        Message message = handler.obtainMessage();
                                        message.what = 4;
                                        message.obj = sign;
                                        handler.sendMessage(message);
                                    } else {
                                        handler.sendEmptyMessage(5);
                                    }
                                }
                            }).start();
                        }

                    }
                }).create();
        builder.show();
    }

    private void selectImg() {
        Intent intent = new Intent();
        // intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_CANCELED);
    }
}
