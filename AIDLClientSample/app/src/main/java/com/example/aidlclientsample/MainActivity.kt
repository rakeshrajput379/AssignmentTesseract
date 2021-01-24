package com.example.aidlclientsample

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aidlserviceprovider.ActionAIDLInterface
import com.example.aidlserviceprovider.GetDeviceInfoServices
import com.example.aidlserviceprovider.PushResultAIDLInterface

class MainActivity : AppCompatActivity() {

    val TAG: String = this.javaClass.name
    lateinit var service:ActionAIDLInterface
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.tv_message)

        initService()
    }

    /**
     * Class for interacting with the main interface of the service.
     */
    private var mConnection: ServiceConnection? = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, boundService: IBinder) {
            service = ActionAIDLInterface.Stub.asInterface(boundService)
            Toast.makeText(this@MainActivity, "AIDL service connected", Toast.LENGTH_LONG).show()
            val pushResultAIDL: PushResultAIDLInterface.Stub = object : PushResultAIDLInterface.Stub() {
                override fun publishResult(orientation: String?) {
                    textView.text = orientation
                }
            }

            try {
                service.register(pushResultAIDL)
            } catch (e: RemoteException) {

            }
        }
        override fun onServiceDisconnected(name: ComponentName) {
            Toast.makeText(this@MainActivity, "AIDL service disconnected", Toast.LENGTH_LONG).show()
        }
    }

    /** Function to establish connections with the service, binding by interface names.  */
    private fun initService() {
        val i = Intent(this, GetDeviceInfoServices::class.java)
//        i.setClassName(applicationContext, GetDeviceInfoServices::class.java)
        val bindResult =
            bindService(i, mConnection!!, Context.BIND_AUTO_CREATE)
        Log.i("$$ Service Check", "initService() bindResult: $bindResult")
/*        if (bindResult) {
            buttonSerial.setClickable(true)
            buttonVersionCode.setClickable(true)
        }*/
    }

    /** Function to release connections.  */
    private fun releaseService() {
        unbindService(mConnection!!)
        mConnection = null
        Log.d(TAG, "releaseService() trigger")
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseService()
    }
}