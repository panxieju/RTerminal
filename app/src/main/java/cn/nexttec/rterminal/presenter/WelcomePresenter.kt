package cn.nexttec.rterminal.presenter

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.CountDownTimer
import android.support.v4.app.ActivityCompat
import cn.nexttec.rterminal.RTerminalApplication
import cn.nexttec.rterminal.base.BasePresenter
import cn.nexttec.rterminal.common.SKIP_SETTING
import cn.nexttec.rterminal.common.imei
import cn.nexttec.rterminal.presenter.view.WelcomeView
import cn.nexttec.rterminal.ui.activity.MainActivity
import cn.nexttec.rterminal.ui.activity.SettingActivity
import cn.nexttec.rxpermission.RxPermissions
import javax.inject.Inject


const val TIMER = 4000.toLong()
const val INTERVAL = 1000.toLong()

class WelcomePresenter @Inject constructor(): BasePresenter<WelcomeView>() {

    override lateinit var mView: WelcomeView
    private lateinit var context: Context
    private var skipSetting: Boolean = false

    private val countDownTimer = object : CountDownTimer(TIMER, INTERVAL) {
        override fun onFinish() {
            switchActivity()
        }

        override fun onTick(p0: Long) {
            mView.showCountDownTimer(((TIMER-p0) / 1000).toInt())
        }
    }

    @SuppressLint("CheckResult")
    fun start() {
        context = mView.getContext()
        val permissions = listOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissions.none { ActivityCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED }) {
            readPreference()
            countDownTimer.start()
        } else {
            RxPermissions(mView.getActivity()).request(permissions)
                .subscribe {
                    if (it) {
                        RTerminalApplication.localId = imei(context)
                        SerialConfig.initialize("vendors.ini", context)
                        countDownTimer.start()
                    } else {
                        mView.onPermissionNotGranted()
                    }
                }
        }
    }

    /**
     * todo 读取配置
     */
    private fun readPreference() {
        val mPreference = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        skipSetting = mPreference.getBoolean(SKIP_SETTING, false)
    }

    private fun switchActivity() {
        var intent: Intent
        if (skipSetting) {
            intent = Intent(context, MainActivity::class.java)
        } else {
            intent = Intent(context, SettingActivity::class.java)
        }

        context.startActivity(intent)
    }

}