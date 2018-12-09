package cn.nexttec.rterminal.common

import android.content.Context
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.Toast

//todo 简化log输出
infix fun Any.li(msg:String) = Log.i(this::class.java.simpleName, msg)
infix fun Any.le(msg:String) = Log.e(this::class.java.simpleName, msg)
infix fun Any.ld(msg:String) = Log.d(this::class.java.simpleName, msg)
infix fun Any.lw(msg:String) = Log.wtf(this::class.java.simpleName, msg)

//todo 简化Toast
infix fun Context.toast(msg:String) = Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
infix fun Context.toastl(msg:String) = Toast.makeText(this, msg, Toast.LENGTH_LONG).show()

//todo 简化Snackbar
infix fun View.snackbar(msg:String) = Snackbar.make(this,msg,Snackbar.LENGTH_SHORT).show()
