package com.hch.test;

import android.util.Log;

//import com.hch.hchlib.utils.L;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * Created by 奔向阳光 on 2016/2/29.
 * https://github.com/square/okhttp/wiki/Recipes
 */
public class OkhttpTest {

    /**
     * 同步请求
     * @throws IOException
     */
    public void synGet() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();
        final Call call = client.newCall(request);
//        Response response = client.newCall(request).execute();
//        Headers headers = response.headers();
//        for (int i = 0; i<headers.size(); i++){
//            Log.i("ok",headers.name(i)+"："+headers.value(i));
//        }
//        Log.i("ok","body"+response.body().string());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = call.execute();
                    Headers headers = response.headers();
                    for (int i = 0; i<headers.size(); i++){
                        Log.i("ok",headers.name(i)+"："+headers.value(i));
                    }
                    Log.i("ok","body"+response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * 异步请求
     * @throws IOException
     */
    public void asynGet() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Headers headers = response.headers();
                for (int i = 0;i<headers.size();i++){
                    Log.i("ok",headers.name(i)+"："+headers.value(i));
                }
                Log.i("ok","body"+response.body().string());
            }
        });
    }

    /**
     * 响应头
     */
    public void addHeader(){
        final OkHttpClient client = new OkHttpClient();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url("https://api.github.com/repos/square/okhttp/issues")
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .header("Accept", "application/vnd.github.v3+json")
                        .build();
                Log.i("ok",request.headers().toString());
                try {
                    Response response = client.newCall(request).execute();
                    Log.i("ok",response.headers().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * post提交String
     */
    public void postStr(){
        final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
        final OkHttpClient client = new OkHttpClient();
        String postBody = ""
                + "Releases\n"
                + "--------\n"
                + "\n"
                + " * _1.0_ May 6, 2013\n"
                + " * _1.1_ June 15, 2013\n"
                + " * _1.2_ August 11, 2013\n";
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN,postBody))
                .build();
        Log.i("ok",request.body().toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("ok",response.body().string());
            }
        });
    }

    public void postStream(){
        final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
        final OkHttpClient client =  new OkHttpClient();
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_MARKDOWN;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("Numbers\n");
                sink.writeUtf8("-------\n");
                for (int i = 2; i <= 997; i++) {
                    sink.writeUtf8(String.format(" * %s = %s\n", i, factor(i)));
                }
            }

            private String factor(int n) {
                for (int i = 2; i < n; i++) {
                    int x = n / i;
                    if (x * i == n) return factor(x) + " × " + i;
                }
                return Integer.toString(n);
            }
        };
        Request request =  new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("ok",response.body().string());
            }
        });
    }

    public void postFile(){
        final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
        final OkHttpClient client =  new OkHttpClient();
        File file = new File("README.md");
        Request request = new Request.Builder()
                .url("")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN,file))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("ok",response.body().string());
            }
        });
    }

    public void postForm(){
        final OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("search","Jurassic Park")
                .build();
        Request request = new Request.Builder()
                .url("https://en.wikipedia.org/w/index.php")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("ok",response.body().string());
            }
        });
    }

    public void postMulti(){
        final  MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        final OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title","Square Logo")
                .addFormDataPart("title","logo-square.png",
                        RequestBody.create(MEDIA_TYPE_PNG,new File("website/static/logo-square.png")))
                .build();
        Request request = new Request.Builder()
                .url("https://api.imgur.com/3/image")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("ok",response.body().string());
            }
        });
    }

    /**
     * 待解决
     */
    public void parseGson(){
        final OkHttpClient client = new OkHttpClient();
        final Gson gson = new Gson();
        Request request  = new Request.Builder()
                .url("https://api.github.com/gists/c2a7c39532239ff261be")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gist gist = gson.fromJson(response.body().charStream(),Gist.class);
                for(Map.Entry<String,GistFile> entry : gist.maps.entrySet()){
                    Log.i("ok",entry.getKey());
                    Log.i("ok",entry.getValue().content);
                }
            }
        });
    }
    static class Gist{
        Map<String,GistFile> maps;
    }
    static class GistFile{
        String content;
    }

}
