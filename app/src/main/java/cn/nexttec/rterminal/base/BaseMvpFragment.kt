package cn.nexttec.rterminal.base

import android.content.Context

open class BaseMvpFragment<T:BasePresenter<*>>:BaseFragment(),BaseView{

    lateinit var mPresenter:T

    override fun getContext(): Context {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



}

