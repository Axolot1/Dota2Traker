package com.axolotl.dota2traker.utils;

import android.util.Log;

/**
 * Created by axolotl on 16/7/2.
 */
public class LogUtils {
    public static final String TAG = "dota";
    public static final boolean ENABLE = false;

    public static void log(String str){
        if(ENABLE) {
            Log.i(TAG, str);
        }
    }
}
