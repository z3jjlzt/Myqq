package com.kkk.myqq.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by kkk on 2016/6/3.
 * z3jjlzt.github.io
 */
public class MyDbHelper extends SQLiteOpenHelper {
    private String db_name;

    public MyDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public MyDbHelper(Context context,String db_name,int version){
        this(context,db_name,null,version);
        this.db_name=db_name;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_create = "create table chatHistory (id integer primary key autoincrement ," +
                "isLeft boolean , fromname text , toname text , msg text , time long )";
        db.execSQL(sql_create);
        Log.e("sb", "done");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
