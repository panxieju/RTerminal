package cn.nexttec.rterminal.ui.activity

import android.os.Bundle
import android.widget.Toast
import cn.nexttec.rterminal.R
import cn.nexttec.rterminal.base.BaseMvpActivity
import cn.nexttec.rterminal.presenter.MainPresenter
import cn.nexttec.rterminal.presenter.view.MainView
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseMvpActivity<MainPresenter>(), MainView {

    override fun onSuccessInitialized(s: String) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            mPresenter.start()
        }
    }




}
