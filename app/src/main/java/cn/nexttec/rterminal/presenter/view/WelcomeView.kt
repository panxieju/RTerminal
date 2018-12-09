package cn.nexttec.rterminal.presenter.view

import android.app.Activity
import cn.nexttec.rterminal.base.BaseView

interface WelcomeView:BaseView{

    fun getActivity():Activity
    fun onPermissionNotGranted()
    fun showCountDownTimer(l: Int)

}