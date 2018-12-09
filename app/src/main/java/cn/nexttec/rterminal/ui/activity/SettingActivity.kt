package cn.nexttec.rterminal.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import cn.nexttec.rterminal.R
import cn.nexttec.rterminal.base.BaseActivity
import cn.nexttec.rterminal.common.externDataPath
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.content_setting.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

    }

}
