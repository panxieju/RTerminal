package cn.nexttec.rterminal.presenter

import android.content.Context
import cn.nexttec.rterminal.base.BasePresenter
import cn.nexttec.rterminal.data.GrpcContract
import cn.nexttec.rterminal.data.SerialContact
import cn.nexttec.rterminal.data.impl.GrpcImpl
import cn.nexttec.rterminal.data.impl.SerialImpl
import cn.nexttec.rterminal.presenter.view.MainView
import com.google.common.primitives.Bytes
import javax.inject.Inject

class MainPresenter :BasePresenter<MainView>(),SerialContact.Service, GrpcContract.Service{
    lateinit var serial :SerialImpl<SerialContact.Service>
    lateinit var grpc: GrpcImpl<GrpcContract.Service>

    override fun onReceiveFail() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun getContext(): Context {
        return mView.getContext()
    }

    override fun onUsbHostSupportFailed() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSerialOpen(success: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSerialConfigSet(success: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onReadString(data: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onReadBytes(data: ByteArray, length: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onWrite(success: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onServerUnreachable() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateSession(success: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDataReceived(bytes: Bytes) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSessionLost() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun start() {
        mView.onSuccessInitialized("Done")
    }

}