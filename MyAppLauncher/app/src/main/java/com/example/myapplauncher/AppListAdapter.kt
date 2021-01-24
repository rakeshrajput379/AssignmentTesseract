package com.example.myapplauncher

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mylauncherlib.AppInfo
import org.w3c.dom.Text

class AppListAdapter(c: Context,appList:ArrayList<AppInfo>) : RecyclerView.Adapter<AppListAdapter.ViewHolder>() {

    var appsList:MutableList<AppInfo> = appList

    //set list values on change
    fun setAppListData(appList:ArrayList<AppInfo>){
        this.appsList = appsList
        notifyDataSetChanged()

    }

    fun notifyDataSetChangeAdapter(){

        notifyDataSetChanged()

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var appName: TextView = itemView.findViewById(R.id.app_name)
        var img: ImageView = itemView.findViewById(R.id.app_icon) as ImageView
        var tvPackageName: TextView = itemView.findViewById(R.id.tv_package_name)
        var tvVersionName:TextView = itemView.findViewById(R.id.tv_version);
        var tvVersionCode:TextView = itemView.findViewById(R.id.tv_version_code)
        var tvLauncherName:TextView = itemView.findViewById(R.id.tv_launcher)

        init {

            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val pos = adapterPosition
            val context: Context = v.context
            val launchIntent: Intent = context.getPackageManager()
                .getLaunchIntentForPackage(appsList[pos].packageName.toString())!!
            context.startActivity(launchIntent)
            Toast.makeText(v.context, appsList[pos].label.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        //Here we use the information in the list we created to define the view

        val appInfo: AppInfo = appsList[i]
        viewHolder.appName.text = appInfo.appName
        viewHolder.img.setImageDrawable(appInfo.icon)
        viewHolder.tvPackageName.text = appInfo.packageName
        viewHolder.tvVersionName.text = "Version Name :: $"+appInfo.versionName
        viewHolder.tvVersionCode.text = "Version Code :: "+appInfo.versionCode
        viewHolder.tvLauncherName.text = "Launcher Name ::"+appInfo.launcherActvity
    }

    override fun getItemCount(): Int {

        //This method needs to be overridden so that Androids knows how many items
        //will be making it into the list
        return appsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        //This is what adds the code we've written in here to our target view
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.row_app_details, parent, false)
        return ViewHolder(view)
    }
}