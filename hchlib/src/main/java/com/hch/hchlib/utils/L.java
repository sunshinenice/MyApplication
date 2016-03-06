package com.hch.hchlib.utils;

import android.util.Log;

import com.hch.hchlib.AppConst;

/**
 * Created by 奔向阳光 on 2016/1/27.
 * Log统一管理类
 */
public class L {

    public static boolean isLog = AppConst.isLog;     // 是否需要打印bug，可以在application的onCreate函数里面初始化
    private static final String tag = "ok";

    //下面四个是默认tag的函数
    public static void i(String msg){
        if(isLog){Log.i(tag,msg);}
    }

    public static void d(String msg){
        if(isLog){Log.d(tag,msg);}
    }

    public static void e(String msg){
        if(isLog){Log.e(tag,msg);}
    }

    public static void v(String msg){
        if(isLog){Log.v(tag,msg);}
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag,String msg){
        if(isLog){Log.i(tag,msg);}
    }

    public static void d(String tag,String msg){
        if(isLog){Log.d(tag,msg);}
    }

    public static void e(String tag,String msg){
        if(isLog){Log.e(tag,msg);}
    }

    public static void v(String tag,String msg){
        if(isLog){Log.v(tag,msg);}
    }
}
