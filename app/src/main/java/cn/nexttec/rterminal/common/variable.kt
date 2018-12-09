package cn.nexttec.rterminal.common

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Environment
import android.support.annotation.RequiresApi
import android.telephony.TelephonyManager
import com.google.gson.Gson
import java.util.*

//todo 获取时间戳
val timestamp = (Date().time / 1000).toInt().toLong()

//todo 使用gson格式化对象
val gson = { obj: Any? ->
    Gson().toJson(obj)
}

//todo 获取telephony_manager
val tm = { context: Context ->
    context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
}

//todo 读取imei
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("MissingPermission")
val imei = { context: Context ->
    tm(context).imei
}

//todo 外部存储目录
val externDataPath = { context: Context ->
    "${Environment.getExternalStorageDirectory()}/${context.packageName}"
}