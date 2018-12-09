package cn.nexttec.rterminal.base

import android.content.Context
import android.os.Bundle
import javax.inject.Inject

open class BaseMvpActivity<T:BasePresenter<*>>:BaseActivity(), BaseView{

    override fun getContext(): Context {
        return this
    }

    @Inject
    lateinit var mPresenter: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



}