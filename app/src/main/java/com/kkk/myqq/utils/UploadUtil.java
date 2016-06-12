package com.kkk.myqq.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.kkk.myqq.entity.setHeadResult;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by kkk on 2016/5/29.
 * z3jjlzt.github.io
 */
public class UploadUtil {
    private static final String BOUNDARY = UUID.randomUUID().toString();
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data";
    private static final String CHARSET = "utf-8";

    public void uploadFile(String path, final String filename, final String url, final Handler handler) {
        if(path==null){
            return ;
        }
        final File file = new File(path);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url1 = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) url1.openConnection();
                    setProperty(con);
                    writeData(con, filename, file, handler);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private void writeData(HttpURLConnection con, String filename, File file, Handler handler) {
        StringBuffer sb = new StringBuffer();
        sb.append(PREFIX + BOUNDARY + LINE_END);
        sb.append("Content-Disposition: form-data; name=\"headImg\"; filename=\"" + filename + "\"").append(LINE_END).append(LINE_END);
        try {

            DataOutputStream dos = new DataOutputStream(con.getOutputStream());
            dos.writeBytes(sb.toString());
            imageCompress(file, dos);


            dos.writeBytes(LINE_END + PREFIX + BOUNDARY + PREFIX + LINE_END);
            dos.flush();

            if (con.getResponseCode() == 200) {
                InputStream resultIs = con.getInputStream();//得到网络返回的输入流
                BufferedReader buf = new BufferedReader(new InputStreamReader(resultIs));//转化为字符缓冲流
                int len = -1;
                char[] b = new char[2048];
                StringBuffer sb1 = new StringBuffer();
                while ((len = buf.read(b)) != -1) {
                    sb1.append(b, 0, len);
                }
                setHeadResult result = new Gson().fromJson(sb1.toString(), setHeadResult.class);
                if (result == null) {
                    handler.sendEmptyMessage(0);
                } else {
                    if (result.getState() == 1) {
                        Message message = Message.obtain();
                        message.what=1;
                        message.obj=result.getHead();
                        handler.sendMessage(message);
                    } else if (result.getState() == 0) {
                        handler.sendEmptyMessage(0);
                    }
                }
                Log.e("sb", sb1.toString());
            }
        } catch (IOException e) {
        }
    }

    /**
     * 压缩图片同时把刘写入dos
     *
     * @param file
     * @param dos
     */
    private void imageCompress(File file, DataOutputStream dos) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, dos);

        } catch (Exception e) {
        }

    }

    /**
     * 设置连接属性
     *
     * @param con
     */
    private void setProperty(HttpURLConnection con) {
        try {
            con.setRequestMethod("POST");
            con.setConnectTimeout(5000);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Charset", CHARSET);
            con.setRequestProperty("Connection", "keep-alive");
            con.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

    }


}
