package cn.nexttec.rterminal.base

import javax.inject.Inject

open class BasePresenter<T:BaseView>{


    open lateinit var mView:T

}