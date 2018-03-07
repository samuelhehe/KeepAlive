/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.samuelnotes.keepalive.syncadapter;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import cn.samuelnotes.keepalive.service.DaemonService;
import cn.samuelnotes.keepalive.utils.Config;


/**
 * Service to handle sync requests.
 * <p/>
 * <p>This service is invoked in response to Intents with action android.content.SyncAdapter, and
 * returns a Binder connection to SyncAdapter.
 * <p/>
 * <p>For performance, only one sync adapter will be initialized within this application's context.
 * <p/>
 * <p>Note: The SyncService itself is not notified when a new sync occurs. It's role is to
 * manage the lifecycle of our {@link SyncAdapter} and provide a handle to said SyncAdapter to the
 * OS on request.
 */
public class SyncService extends Service {
    private static final String TAG = "SyncService";

    private static final Object sSyncAdapterLock = new Object();
    private static SyncAdapter sSyncAdapter = null;

    /**
     * Thread-safe constructor, creates static {@link SyncAdapter} instance.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service created");
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true, SyncService.this);
            }
        }
    }


    /**
     * 这个方法的作用是检查DaemonService 是否已经挂掉， 如果挂掉则将其启动
     * 检查策略：
     * 1. 如果非小米手机则进行DaemonService检查
     * 2. 查看daemon 是否存在于当前系统进程中
     *
     */
    public void checkDaemonService() {
        Log.i(TAG, "checkDaemonService created start ");

//        if(!Config.PHONETYPE.equals(Build.MANUFACTURER)){
            Log.i(TAG, "checkDaemonService created start Build.MANUFACTURER "+ Build.MANUFACTURER);
            if (!isExistDaemonProcess(getApplicationContext())) {
                Log.i(TAG, "checkDaemonService created start isExistDaemonProcess ");
                startService(new Intent(this, DaemonService.class));
            }
//        }
        Log.i(TAG, "checkDaemonService created end ");
    }

    /**
     * 通过检查进程名称 判断Daemon进程是否存在
     * @param context
     * @return
     */
    public static Boolean  isExistDaemonProcess(Context context) {
        try {
//            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                    .getRunningAppProcesses()) {

                if(TextUtils.equals(appProcess.processName,":daemon")){
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            // ignore
            e.printStackTrace();
        }
        return false;
    }

    @Override
    /**
     * Logging-only destructor.
     */
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroyed");
    }

    /**
     * Return Binder handle for IPC communication with {@link SyncAdapter}.
     * <p/>
     * <p>New sync requests will be sent directly to the SyncAdapter using this channel.
     *
     * @param intent Calling intent
     * @return Binder handle for {@link SyncAdapter}
     */
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
