//package com.hch.hchlib.http;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Handler;
//import android.os.Looper;
//import android.widget.ImageView;
//
//import com.google.gson.Gson;
//import com.google.gson.internal.$Gson$Types;
//import com.squareup.okhttp.Call;
//import com.squareup.okhttp.Callback;
//import com.squareup.okhttp.FormEncodingBuilder;
//import com.squareup.okhttp.Headers;
//import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.MultipartBuilder;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.net.CookieManager;
//import java.net.CookiePolicy;
//import java.net.FileNameMap;
//import java.net.URLConnection;
//import java.util.Map;
//import java.util.Set;
//
///**
// * Created by 奔向阳光 on 2016/1/28.
// * OkHttp代码封装
// * 参考 http://blog.csdn.net/lmj623565791/article/details/47911083
// */
//public class OkHttpManager {
//
//    private static OkHttpManager mInstance;
//    private OkHttpClient mOkHttpClient;
//    private Handler mHandler;
//    private Gson mGson;
//
//    private OkHttpManager() {
//        mOkHttpClient = new OkHttpClient();
//        //cookie enabled
//        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
//        mHandler = new Handler(Looper.getMainLooper());
//        mGson = new Gson();
//    }
//
//    public static OkHttpManager getInstance() {
//        if (mInstance == null) {
//            synchronized (OkHttpManager.class) {
//                if (mInstance == null) {
//                    mInstance = new OkHttpManager();
//                }
//            }
//        }
//        return mInstance;
//    }
//
//    /**
//     * 同步GET请求
//     *
//     * @param url
//     * @return response
//     * @throws IOException
//     */
//    private Response _get(String url) throws IOException {
//        final Request request = new Request.Builder().url(url).build();
//        Call call = mOkHttpClient.newCall(request);
//        Response response = call.execute();
//        return response;
//    }
//
//    /**
//     * 同步GET请求
//     *
//     * @param url
//     * @return String
//     * @throws IOException
//     */
//    private String _getStr(String url) throws IOException {
//        Response response = _get(url);
//        return response.body().string();
//    }
//
//    /**
//     * 异步GET请求
//     *
//     * @param url
//     * @param callback
//     */
//    private void _getAsyn(String url, final ResultCallback callback) {
//        final Request request = new Request.Builder().url(url).build();
//        deliveryResult(request, callback);
//    }
//
//    /**
//     * 同步POST请求
//     *
//     * @param url
//     * @param params
//     * @return response
//     */
//    private Response _post(String url, Param... params) throws IOException {
//        Request request = buildPostRequest(url, params);
//        Call call = mOkHttpClient.newCall(request);
//        Response response = call.execute();
//        return response;
//    }
//
//    /**
//     * 同步POST请求
//     *
//     * @param url
//     * @param params
//     * @return string
//     * @throws IOException
//     */
//    private String _postStr(String url, Param... params) throws IOException {
//        Response response = _post(url, params);
//        return response.body().string();
//    }
//
//    /**
//     * 异步POST请求
//     *
//     * @param url
//     * @param callback
//     * @param params
//     */
//    private void _postAsyn(String url, final ResultCallback callback, Param... params) {
//        Request request = buildPostRequest(url, params);
//        deliveryResult(request, callback);
//    }
//
//    /**
//     * 异步POST请求
//     *
//     * @param url
//     * @param callback
//     * @param maps
//     */
//    private void _postAsyn(String url, final ResultCallback callback, Map<String, String> maps) {
//        Param[] params = map2Params(maps);
//        _postAsyn(url, callback, params);
//    }
//
//    /**
//     * 同步基于POST的文件上传
//     *
//     * @param params
//     * @return
//     * @throws IOException
//     */
//    private Response _post(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
//        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
//        return mOkHttpClient.newCall(request).execute();
//    }
//
//    /**
//     * 异步基于POST文件上传
//     *
//     * @param url
//     * @param files
//     * @param fileKeys
//     * @param callback
//     * @param params
//     */
//    private void _postAsyn(String url, File[] files, String[] fileKeys, ResultCallback callback, Param... params) {
//        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
//        deliveryResult(request, callback);
//    }
//
//    /**
//     * 异步下载文件
//     *
//     * @param url
//     * @param destFileDir
//     * @param callback
//     */
//    private void _downloadAsyn(final String url, final String destFileDir, final ResultCallback callback) {
//        Request request = new Request.Builder().url(url).build();
//        Call call = mOkHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                sendFailedCallback(request, e, callback);
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                InputStream is = response.body().byteStream();
//                byte[] buf = new byte[2048];
//                int len = 0;
//                File file = new File(destFileDir, getFileName(url));
//                FileOutputStream fos = new FileOutputStream(file);
//                while ((len = is.read(buf)) != -1) {
//                    fos.write(buf, 0, len);
//                }
//                fos.flush();
//                sendSuccessResultCallback(file.getAbsolutePath(), callback);
//                if (is != null) is.close();
//                if (fos != null) fos.close();
//            }
//        });
//    }
//
//    private String getFileName(String path) {
//        int separatorIndex = path.lastIndexOf("/");
//        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
//    }
//
//    /**
//     * 加载图片
//     *
//     * @param view
//     * @param url
//     * @param errorResId
//     */
//    private void _displayImage(final ImageView view, final String url, final int errorResId) {
//        Request request = new Request.Builder().url(url).build();
//        Call call = mOkHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                setErrorResId(view, errorResId);
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                InputStream is = response.body().byteStream();
////                ImageUtils.ImageSize actualImageSize = ImageUtils.getImageSize(is);
////                ImageUtils.ImageSize imageViewSize = ImageUtils.getImageViewSize(view);
////                int inSampleSize = ImageUtils.calculateInSampleSize(actualImageSize, imageViewSize);
////                try{
////                    is.reset();
////                } catch (IOException e){
////                    response = _get(url);
////                    is = response.body().byteStream();
////                }
////                BitmapFactory.Options ops = new BitmapFactory.Options();
////                ops.inJustDecodeBounds = false;
////                ops.inSampleSize = inSampleSize;
////                final Bitmap bm = BitmapFactory.decodeStream(is, null, ops);
//                final Bitmap bm = BitmapFactory.decodeStream(is);
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        view.setImageBitmap(bm);
//                    }
//                });
//                is.close();
//            }
//        });
//    }
//
//    private void setErrorResId(final ImageView view, final int errorResId) {
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                view.setImageResource(errorResId);
//            }
//        });
//    }
//
//    //*************对外公布的方法************
//
//    public static Response get(String url) throws IOException {
//        return getInstance()._get(url);
//    }
//
//    public static String getStr(String url) throws IOException {
//        return getInstance()._getStr(url);
//    }
//
//    public static void getAsyn(String url, ResultCallback callback) {
//        getInstance()._getAsyn(url, callback);
//    }
//
//    public static Response post(String url, Param... params) throws IOException {
//        return getInstance()._post(url, params);
//    }
//
//    public static String postStr(String url, Param... params) throws IOException {
//        return getInstance()._postStr(url, params);
//    }
//
//    public static void postAsyn(String url, ResultCallback callback, Param... params) {
//        getInstance()._postAsyn(url, callback, params);
//    }
//
//    public static void postAsyn(String url, ResultCallback callback, Map<String, String> params) {
//        getInstance()._postAsyn(url, callback, params);
//    }
//
//    public static Response post(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
//        return getInstance()._post(url, files, fileKeys, params);
//    }
//
////    public static Response post(String url, File file, String fileKey) throws IOException {
////        return getInstance()._post(url, new File[]{file}, new String[]{fileKey}, null);
////    }
//
//    public static Response post(String url, File file, String fileKey, Param... params) throws IOException {
//        return getInstance()._post(url, new File[]{file}, new String[]{fileKey}, params);
//    }
//
//    public static void postAsyn(String url, ResultCallback callback, File[] files, String[] fileKeys, Param... params) throws IOException {
//        getInstance()._postAsyn(url, files, fileKeys, callback, params);
//    }
//
////    public static void postAsyn(String url, ResultCallback callback, File file, String fileKey) throws IOException {
////        getInstance()._postAsyn(url, new File[]{file}, new String[]{fileKey}, callback, null);
////    }
//
//    public static void postAsyn(String url, ResultCallback callback, File file, String fileKey, Param... params) throws IOException {
//        getInstance()._postAsyn(url, new File[]{file}, new String[]{fileKey}, callback, params);
//    }
//
//    public static void displayImage(final ImageView view, String url, int errorResId) throws IOException {
//        getInstance()._displayImage(view, url, errorResId);
//    }
//
//    public static void displayImage(final ImageView view, String url) {
//        getInstance()._displayImage(view, url, -1);
//    }
//
//    public static void downloadAsyn(String url, String destDir, ResultCallback callback) {
//        getInstance()._downloadAsyn(url, destDir, callback);
//    }
//
//    //****************************
//
//    private Request buildMultipartFormRequest(String url, File[] files, String[] fileKeys, Param... params) {
//        params = validateParam(params);
//        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
//        for (Param param : params) {
//            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""), RequestBody.create(null, param.value));
//        }
//        if (files != null) {
//            RequestBody fileBody = null;
//            for (int i = 0; i < files.length; i++) {
//                File file = files[i];
//                String fileName = file.getName();
//                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
//                //TODO 根据文件名设置contentType
//                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""), fileBody);
//            }
//        }
//        RequestBody requestBody = builder.build();
//        return new Request.Builder().url(url).post(requestBody).build();
//    }
//
//    private String guessMimeType(String path) {
//        FileNameMap fileNameMap = URLConnection.getFileNameMap();
//        String contentTypeFor = fileNameMap.getContentTypeFor(path);
//        if (contentTypeFor == null) {
//            contentTypeFor = "application/octet-stream";
//        }
//        return contentTypeFor;
//    }
//
//    private Param[] validateParam(Param[] params) {
//        if (params == null)
//            return new Param[0];
//        else
//            return params;
//    }
//
//    private Param[] map2Params(Map<String, String> params) {
//        if (params == null) return new Param[0];
//        int size = params.size();
//        Param[] paras = new Param[size];
//        Set<Map.Entry<String, String>> entries = params.entrySet();
//        int i = 0;
//        for (Map.Entry<String, String> entry : entries) {
//            paras[i++] = new Param(entry.getKey(), entry.getValue());
//        }
//        return paras;
//    }
//
//    private void deliveryResult(Request request, final ResultCallback callback) {
//        mOkHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                sendFailedCallback(request, e, callback);
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                String str = response.body().string();
//                if (callback.mType == String.class) {
//                    sendSuccessResultCallback(str, callback);
//                } else {
//                    Object o = mGson.fromJson(str, callback.mType);
//                    sendSuccessResultCallback(o, callback);
//                }
//            }
//        });
//    }
//
//    private void sendFailedCallback(final Request request, final Exception e, final ResultCallback callback) {
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (callback == null) callback.onError(request, e);
//            }
//        });
//    }
//
//    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (callback != null) {
//                    callback.onResponse(object);
//                }
//            }
//        });
//    }
//
//    private Request buildPostRequest(String url, Param[] params) {
//        params = validateParam(params);
//        FormEncodingBuilder builder = new FormEncodingBuilder();
//        for (Param param : params) {
//            builder.add(param.key, param.value);
//        }
//        RequestBody requestBody = builder.build();
//        return new Request.Builder().url(url).post(requestBody).build();
//    }
//
//    public static abstract class ResultCallback<T> {
//        Type mType;
//
//        public ResultCallback() {
//            mType = getSuperclassTypeParameter(getClass());
//        }
//
//        static Type getSuperclassTypeParameter(Class<?> subclass) {
//            Type superclass = subclass.getGenericSuperclass();
//            if (superclass instanceof Class) {
//                throw new RuntimeException("Missing type parameter.");
//            }
//            ParameterizedType parameterized = (ParameterizedType) superclass;
//            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
//        }
//
//        public abstract void onError(Request request, Exception e);
//
//        public abstract void onResponse(T response);
//    }
//
//    public static class Param {
//
//        String key;
//        String value;
//
//        public Param() {
//        }
//
//        public Param(String key, String value) {
//            this.key = key;
//            this.value = value;
//        }
//    }
//}