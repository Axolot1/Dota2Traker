package com.axolotl.dota2traker.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.axolotl.dota2traker.utils.LogUtils;
import com.google.android.gms.gcm.TaskParams;

/**
 * Created by axolotl on 16/7/13.
 */
public class IntentTaskService extends IntentService {

    private static final String ACTION_PERIODIC = "com.axolotl.dota2traker.service.action.FOO";

    public IntentTaskService() {
        super("IntentTaskService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        LogUtils.log("onhandle intent start task service run");
        DotaTaskService dotaTaskService = new DotaTaskService(this);
        dotaTaskService.onRunTask(new TaskParams("tag"));
    }

    public static void startTaskService(Context context) {
        Intent intent = new Intent(context, IntentTaskService.class);
        context.startService(intent);
    }
}
