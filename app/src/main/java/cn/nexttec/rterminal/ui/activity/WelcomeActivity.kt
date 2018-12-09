package cn.nexttec.rterminal.ui.activity

import android.app.Activity
import android.os.Bundle
import cn.nexttec.rterminal.R
import cn.nexttec.rterminal.base.BaseMvpActivity
import cn.nexttec.rterminal.injection.component.DaggerWelcomeComponet
import cn.nexttec.rterminal.injection.module.WelcomeModule
import cn.nexttec.rterminal.presenter.WelcomePresenter
import cn.nexttec.rterminal.presenter.view.WelcomeView
import javax.inject.Inject

class WelcomeActivity:BaseMvpActivity<WelcomePresenter>(), WelcomeView{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        DaggerWelcomeComponet.builder().welcomeModule(WelcomeModule()).build().inject(this)
        with (mPresenter){

            mView = this@WelcomeActivity
            start()
        }
    }

    override fun onPermissionNotGranted() {
    }

    override fun showCountDownTimer(l: Int) {
    }

    override fun getActivity(): Activity {
        return this
    }
}