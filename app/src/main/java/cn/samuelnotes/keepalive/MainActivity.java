package cn.samuelnotes.keepalive;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.samuelnotes.keepalive.service.DaemonService;
import cn.samuelnotes.keepalive.service.JobHandlerService;
import cn.samuelnotes.keepalive.syncadapter.SyncUtils;
import cn.samuelnotes.keepalive.utils.Config;


/**
 * create by samuelnotes
 * 这里添加小米机型判断是因为， 当时项目做的这个保活主要就是为了唤醒推送，提高推送到达率。 小米机型有自己的推送sdk ，so，，
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        if (!Config.PHONETYPE.equals(Build.MANUFACTURER)) {

//            startService(new Intent(this, JobHandlerService.class));
        startService(new Intent(this, DaemonService.class));
        SyncUtils.CreateSyncAccount(getApplicationContext());
//        }


    }
}
