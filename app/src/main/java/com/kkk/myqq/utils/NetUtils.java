package com.kkk.myqq.utils;

import com.kkk.myqq.interfaces.IFriendApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kkk on 2016/5/23.
 * z3jjlzt.github.io
 */
public class NetUtils {
    private static IFriendApi sIFriendApi=null;
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();
    private static RxJavaCallAdapterFactory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    public static IFriendApi getApi(){
        if (sIFriendApi == null) {
            synchronized (IFriendApi.class){
                if (sIFriendApi == null) {
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                            .addConverterFactory(gsonConverterFactory)
                            .client(okHttpClient)
                            .addCallAdapterFactory(rxJavaCallAdapterFactory)
                            .build();
                    sIFriendApi = retrofit.create(IFriendApi.class);
                }
            }
        }
        return sIFriendApi;
    }

    public static String post(String url,String param){
        String result=null;
        try {
            URL postUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) postUrl.openConnection();
            con.setConnectTimeout(5000);
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("Content-Lenth",String.valueOf(param.getBytes().length));
            con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            bw.write(param);
            bw.close();
            if(con.getResponseCode() == 200){
                InputStream is = con.getInputStream();//得到网络返回的输入流
                BufferedReader buf=new BufferedReader(new InputStreamReader(is));//转化为字符缓冲流
                int len=-1;
                char[] b = new char[2028];
                StringBuffer sb= new StringBuffer();
               while((len=buf.read(b))!=-1){
                   sb.append(b,0,len);
                 //  Log.e("sb", );
               }
               //  result = new Gson().fromJson(sb.toString(), RegisterResult.class);
                result = sb.toString();

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
