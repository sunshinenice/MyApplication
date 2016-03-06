package com.hch.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

//import com.google.gson.Gson;
//import com.hch.hchlib.utils.L;
//import com.hch.hchlib.utils.PermissionsChecker;

import com.hch.test.gson.Book;
import com.hch.test.gson.HObject;
import com.hch.test.gson.hjson;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 奔向阳光 on 2016/1/26.
 */
public class MainActivity extends Activity {

//    private Book book;
    private TextView tv_main;

    private String PERMISSIONS = "android.permission.INTERNET";
//    private PermissionsChecker mPermissionsChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        tv_main = (TextView) this.findViewById(R.id.tv_main);

//        HObject book = new Book();
//        HObject book =new HObject();
//        book.setObjectId("123");
//        book.setName("的回复");
//        book.setValue("的肌肤及");
//        Log.i("ok",book.toJson());
        OkhttpTest okhttpTest = new OkhttpTest();
        okhttpTest.parseGson();
//        try {
//            okhttpTest.synGet();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        synGet();
//        httpGet();
//        mPermissionsChecker = new PermissionsChecker(this);
//        if(mPermissionsChecker.lacksPermissions(PERMISSIONS)){
//            L.i("----true");
//        }else{
//            L.i("----false");
//        }
//        jsonOb = new JSONObject();
//        Gson gson = new Gson();
//        Book book = new Book();
//        book.setObjectId("id");
//        book.setUserId(3);
//        book.setUserName("hch");
//        L.i(book.toJson());
//        Book book1 = gson.fromJson(jsonStr,Book.class);
    }

    public void synGet(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://www.hchstudio.cn/")
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            Headers headers = response.headers();
            for (int i = 0; i<headers.size(); i++){
                Log.i("ok",headers.name(i)+"："+headers.value(i));
            }
            Log.i("ok","body"+response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
