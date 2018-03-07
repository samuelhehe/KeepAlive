package cn.samuelnotes.keepalive.service;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import cn.samuelnotes.keepalive.syncadapter.SyncService;
import cn.samuelnotes.keepalive.utils.Config;

/**
 * Created by samuelwang on 2018/3/6.
 * <p>
 * JobService 这个类是21 新api
 * 计划一个周期性任务 检查周期性任务的运行状态
 * 很适合我们的保活
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobHandlerService extends JobService {

    private static final String TAG = "JobHandlerService";
    private JobScheduler mJobScheduler;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("JobService is created");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(0X00001, new ComponentName(getPackageName(), JobHandlerService.class.getName()));

            builder.setPeriodic(5000);
            builder.setRequiresCharging(true);
            builder.setPersisted(true); //调用 setPersisted(true) 方法可保证服务在设备重启后也能按计划运行。
            builder.setRequiresDeviceIdle(true);

            if (mJobScheduler.schedule(builder.build()) <= 0) {
                //If something goes wrong
                Log.i(TAG, "work task failure  ");
            } else {
                Log.i(TAG, "work fine !");
            }
        }
        return START_STICKY;
    }


    /**
     * Override this method with the callback logic for your job. Any such logic needs to be
     * performed on a separate thread, as this function is executed on your application's main
     * thread.
     *
     * @param params Parameters specifying info about this job, including the extras bundle you
     *               optionally provided at job-creation time.
     * @return True if your service needs to process the work (on a separate thread). False if
     * there's no more work to be done for this job.
     * <p>
     * return true  这次没执行完， 需要放在另一个独立线程完成。  false 表示事情搞完了。
     */
    @Override
    public boolean onStartJob(JobParameters params) {

        /// 检查进程
        checkDaemonService();

        return false;
    }


    /**
     * This method is called if the system has determined that you must stop execution of your job
     * even before you've had a chance to call {@link #jobFinished(JobParameters, boolean)}.
     * <p>
     * <p>This will happen if the requirements specified at schedule time are no longer met. For
     * example you may have requested WiFi with
     * {@link android.app.job.JobInfo.Builder#setRequiredNetworkType(int)}, yet while your
     * job was executing the user toggled WiFi. Another example is if you had specified
     * {@link android.app.job.JobInfo.Builder#setRequiresDeviceIdle(boolean)}, and the phone left its
     * idle maintenance window. You are solely responsible for the behaviour of your application
     * upon receipt of this message; your app will likely start to misbehave if you ignore it. One
     * immediate repercussion is that the system will cease holding a wakelock for you.</p>
     *
     * @param params Parameters specifying info about this job.
     * @return True to indicate to the JobManager whether you'd like to reschedule this job based
     * on the retry criteria provided at job creation-time. False to drop the job. Regardless of
     * the value returned, your job must stop executing.
     * <p>
     * 方法就是表明，服务马上就要停掉了。不要抱有幻想，请立即停止手头上的一切事情。
     * return  true  任务应该计划在下次继续
     * false   放弃该任务， 必须结束
     */
    @Override
    public boolean onStopJob(JobParameters params) {
        /// 检查进程
        checkDaemonService();
        return true;
    }


    /**
     * 这个方法的作用是检查DaemonService 是否已经挂掉， 如果挂掉则将其启动
     * 检查策略：
     * 1. 如果非小米手机则进行DaemonService检查
     * 2. 查看daemon 是否存在于当前系统进程中
     */
    public void checkDaemonService() {
        Log.i(TAG, "checkDaemonService created start ");

        if (!Config.PHONETYPE.equals(Build.MANUFACTURER)) {
            Log.i(TAG, "checkDaemonService created start Build.MANUFACTURER " + Build.MANUFACTURER);
            if (!SyncService.isExistDaemonProcess(getApplicationContext())) {
                Log.i(TAG, "checkDaemonService created start isExistDaemonProcess ");
                startService(new Intent(this, DaemonService.class));
            }
        }
        Log.i(TAG, "checkDaemonService created end ");
    }


}
