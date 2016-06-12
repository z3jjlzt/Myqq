package com.kkk.myqq.activity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.kkk.myqq.R;
import com.kkk.myqq.adapter.LztRecycleViewAdapter;
import com.kkk.myqq.db.MyDbHelper;
import com.kkk.myqq.entity.Msg;
import com.kkk.myqq.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kkk on 2016/5/25.
 * z3jjlzt.github.io
 */
public class ChatActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.btn_send)
    Button mBtnSend;
    @Bind(R.id.rv_chat)
    RecyclerView mRvChat;
    @Bind(R.id.et_msg)
    EditText mEtMsg;
    @Bind(R.id.refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private List<Msg> msglist;
    private EMMessageListener msgListener;
    private LztRecycleViewAdapter mLztRecycleViewAdapter;
    private String name;
    private String head;
    private String meurl;
    private String meName;
    private SharedPreferences sp;
    private SQLiteDatabase db;

    private static final String TABLE_NAME = "chatHistory";

    @Override
    protected void initVariables() {
        sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        msglist = new ArrayList<>();
        name = getIntent().getStringExtra("name");
        head = getIntent().getStringExtra("head");
        meurl = getIntent().getStringExtra("meurl");
        meName = sp.getString("username", "");

        db = new MyDbHelper(this, "chathistory.db", 3).getWritableDatabase();
        Log.e("sb", "var :" + name + " " + head + " " + meurl + " " + meName);
    }

    @Override
    protected void bindUI(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_chat);
    }

    @Override
    protected void doLogic() {
        initAdapter();
        setMsgListener();
        mBtnSend.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                mLztRecycleViewAdapter.notifyDataSetChanged();
            }
        }
    };


    private void initAdapter() {
        mRvChat.setLayoutManager(new LinearLayoutManager(this));
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mLztRecycleViewAdapter = new LztRecycleViewAdapter<vh, Msg>(ChatActivity.this, R.layout.chat_left, msglist) {

            @Override
            public vh createVh(View v) {
                return new vh(v);
            }

            @Override
            public void BindVh(vh viewholder, int position) {
                viewholder.chat_tv_content.setText(msglist.get(position).getMsg());
                viewholder.chat_tv_name.setText(msglist.get(position).getFromName());
                viewholder.tv_time.setText(sdf.format(msglist.get(position).getTime()));
                String url = null;
                if (msglist.get(position).isLeft()) {
                    url = head;
                } else {
                    url = meurl;
                }
                Glide.with(ChatActivity.this)
                        .load(Constants.BASE_URL + "/Myqq" + url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter()
                        .into(viewholder.chat_iv_head);
                Log.e("sb", Constants.BASE_URL + "/Myqq" + url);
            }
        };
        mRvChat.setAdapter(mLztRecycleViewAdapter);
    }

    private void sendMes(String msg, String to) {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(msg, to);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    public void setMsgListener() {

        msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                String txt = messages.get(0).getBody().toString();
                //
                Long date = new Date().getTime();
                Msg msg = new Msg(true, name, meName, txt.substring(txt.indexOf("\"") + 1, txt.lastIndexOf("\"")), date);
                msglist.add(msg);
                Log.e("sb", msg.toString());
                insert(msg);

                //回调接口不在主线程中，要用handler通知主线程更新
                mHandler.sendEmptyMessage(1);

            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
                //收到已读回执
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
                //收到已送达回执
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send: {
                String msg = mEtMsg.getText().toString();
                if (!msg.equals(" ")) {

                    Long date = new Date().getTime();
                    Msg msg1 = new Msg(false, meName, name, msg, date);
                    msglist.add(msg1);
                    insert(msg1);
                    mHandler.sendEmptyMessage(1);
                    sendMes(msg, name);
                    mEtMsg.setText("");
                }
                break;
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db.isOpen()) {
            db.close();
        }
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    /**
     * 写入数据库
     *
     * @param msg
     */
    private void insert(Msg msg) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("isLeft", msg.isLeft());
        contentValues.put("fromname", msg.getFromName());
        contentValues.put("toname", msg.getToName());
        contentValues.put("msg", msg.getMsg());
        contentValues.put("time", msg.getTime());
        db.insert(TABLE_NAME, null, contentValues);
    }

    /**
     * 查询历史记录
     */
    private void getHistory() {
        Cursor c = db.query(TABLE_NAME, null,
                "(fromname=? AND toname=?) OR (fromname=? AND toname=?)",
                new String[]{meName, name, name, meName}, null, null, null);
        // c.moveToFirst();

        msglist.clear();
        while (c.moveToNext()) {
            Msg msg = new Msg();
            msg.setFrom(c.getString(c.getColumnIndex("fromname")));
            msg.setTo(c.getString(c.getColumnIndex("toname")));
            msg.setMsg(c.getString(c.getColumnIndex("msg")));
            msg.setTime(c.getLong(c.getColumnIndex("time")));
            msg.setLeft(c.getInt(c.getColumnIndex("isLeft")) > 0);
            msglist.add(msg);
            Log.e("sb", msg.toString());
        }
        mHandler.sendEmptyMessage(1);
        mSwipeRefreshLayout.setRefreshing(false);
        c.close();
    }

    @Override
    public void onRefresh() {
        getHistory();
    }


    class vh extends RecyclerView.ViewHolder {
        @Bind(R.id.chat_iv_head)
        ImageView chat_iv_head;

        @Bind(R.id.chat_tv_content)
        TextView chat_tv_content;

        @Bind(R.id.chat_tv_name)
        TextView chat_tv_name;

        @Bind(R.id.tv_time)
        TextView tv_time;

        public vh(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
