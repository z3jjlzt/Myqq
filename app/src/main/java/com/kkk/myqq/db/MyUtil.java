package com.kkk.myqq.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by kkk on 2016/4/24.
 */
public class MyUtil {
    private Context mContext;
    private String mDb_name;
    private DbHelper mDbHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public MyUtil(Context context, String db_name, int version, Class<?> clazz) {
        mDbHelper = new DbHelper(context, db_name, version, clazz);
        mSQLiteDatabase = mDbHelper.getWritableDatabase();
    }

    public void insert(Object o) {
        mDbHelper.insertObject(o);
    }


}

class DbHelper extends SQLiteOpenHelper {
  Class mClazz;
  SQLiteDatabase mSQLiteDatabase;

  public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
      super(context, name, factory, version);
  }

  public DbHelper(Context context, String name, int version, Class<?> clazz) {
      super(context, name, null, version);
      mClazz = clazz;
      mSQLiteDatabase = getWritableDatabase();
  }

 static  String getTableName(Class clazz) {
      return clazz.getSimpleName();
  }

    public static   String getCreateSql(Class clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("create table ").append(getTableName(clazz)).append("( id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String tpye = "m" + field.getType().getSimpleName();
            //  Log.e("sb", "tpye " + field.getName());
            if (tpye.equals("mIncrementalChange")) {
                continue;
            }
            stringBuilder.append(field.getName() + " " + SqlType.valueOf(tpye).getType() + ",");
        }
        int len = stringBuilder.length();
        stringBuilder.replace(len - 1, len, ")");
//        Log.e("sb", stringBuilder.toString());
        return stringBuilder.toString();
    }

  void insertObject(Object o) {
      //1得到o的类
      Class clazz = o.getClass();
      //2得到 属性
      Field[] fields = clazz.getDeclaredFields();
      ContentValues contentValues = new ContentValues();
      for (Field field : fields) {
          //3得到属性值
          field.setAccessible(true);
          String classname = "m" + field.getType().getSimpleName();
          if (field.getName().contains("change")) {//包含$change就跳过//这是as2.0热修复自动为类添加的属性
              continue;
          }
          try {
              Object[] param = new Object[]{field.getName(), field.get(o)};
              //得到方法的参数
              Method method = ContentValues.class.getMethod("put", new Class[]{String.class, SqlType.valueOf(classname).getmClass()});
              method.setAccessible(true);
              method.invoke(contentValues, param);
          } catch (IllegalAccessException e) {
              e.printStackTrace();
          } catch (NoSuchMethodException e) {
              e.printStackTrace();
          } catch (InvocationTargetException e) {
              e.printStackTrace();
          }
      }
      mSQLiteDatabase.insert(getTableName(clazz), null, contentValues);
  }


  @Override
  public void onCreate(SQLiteDatabase db) {
      db.execSQL(getCreateSql(mClazz));
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}

enum SqlType {
    mString("text", String.class), mint("integer", Integer.class), mboolean("boolean", Boolean.class),
    mfloat("float", Float.class), mlong("long", Long.class), mdouble("double", Double.class),
    mchar("varchar", Character.class);
    String type;
    Class<?> mClass;

    SqlType(String type, Class<?> mClass) {
        this.type = type;
        this.mClass = mClass;
    }

    public Class<?> getmClass() {
        return mClass;
    }

    public void setClass(Class<?> aClass) {
        mClass = aClass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
