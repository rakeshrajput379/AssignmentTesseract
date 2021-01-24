package com.example.mylauncherlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AppStatusReceiver extends BroadcastReceiver {

    private UpdateAppStatus updateAppStatus;

    public AppStatusReceiver(UpdateAppStatus updateAppStatus){

        this.updateAppStatus = updateAppStatus;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // fetching package names from extras

        Toast.makeText(context,"Action "+intent.getAction(),Toast.LENGTH_LONG).show();

        Log.d("$$ Action","Action "+intent.getAction());

        updateAppStatus.updateAppStatus(intent.getAction());

    }

}