package com.example.mylauncherlib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GetListOfAppsAsyncTask extends AsyncTask<Void, ArrayList<AppInfo>,ArrayList<AppInfo>>{

    @SuppressLint("StaticFieldLeak")
    private final Context mContext;
    private UpdateAppListData updateAppListData;

    public GetListOfAppsAsyncTask(Context mContext,UpdateAppListData updateAppListData){

        this.mContext = mContext;
        this.updateAppListData = updateAppListData;
    }

    @Override
    protected ArrayList<AppInfo> doInBackground(Void[] objects) {
        try {
            return getListOfApps();
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("$$ Exception","Message :: "+e.getMessage());
        }
        return null;
    }

    private ArrayList<AppInfo> getListOfApps() throws PackageManager.NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        ArrayList<AppInfo> appsList = new ArrayList<AppInfo>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);
        for(ResolveInfo ri:allApps) {
            AppInfo app = new AppInfo();
            app.label = ri.loadLabel(pm);
            app.packageName = ri.activityInfo.packageName;
            app.icon = ri.activityInfo.loadIcon(pm);
            app.launcherActvity = ri.activityInfo.name;
            app.versionName = pm.getPackageInfo(ri.activityInfo.packageName,0).versionName;
            app.versionCode = pm.getPackageInfo(ri.activityInfo.packageName,0).versionCode;
            appsList.add(app);
        }

        return appsList;

    }

    @Override
    protected void onPostExecute(ArrayList<AppInfo> object) {
        super.onPostExecute(object);
        if(object!=null){
            updateAppListData.updateAppListData(object);
        }
    }
}
