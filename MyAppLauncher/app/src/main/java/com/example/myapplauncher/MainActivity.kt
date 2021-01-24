package com.example.myapplauncher

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylauncherlib.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private var TAG:String = this.javaClass.name

    private var appStatusReceiver:AppStatusReceiver = AppStatusReceiver(UpdateAppStatus {

        Toast.makeText(this,"Main Activity Called Inside",Toast.LENGTH_LONG).show()

      if (it.isNotEmpty()){

          Toast.makeText(this,"Main Activity Called Inside is not empty ",Toast.LENGTH_LONG).show()

          getListOfAppData()

      }
    })



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerReceiver()
        getListOfAppData()
    }


    private fun registerReceiver(){
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addDataScheme("package")
        registerReceiver(appStatusReceiver, intentFilter)
    }


    private fun getListOfAppData(){

        GetListOfAppsAsyncTask(this, UpdateAppListData { appInfoArrayList ->
            if (appInfoArrayList != null) {
                //   arrayListAppListData = appInfoArrayList

                Log.d(TAG,"List Of Apps $appInfoArrayList")

                initAdapter(appInfoArrayList)


            }
        }).execute()
    }

    private fun initAdapter(appInfoArrayList: ArrayList<AppInfo>) {

        val appListAdapter = AppListAdapter(this,appInfoArrayList);

        val rvAppList: RecyclerView = findViewById(R.id.app_list)

        rvAppList.layoutManager = LinearLayoutManager(this)

        rvAppList.adapter  = appListAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(appStatusReceiver)
    }
}