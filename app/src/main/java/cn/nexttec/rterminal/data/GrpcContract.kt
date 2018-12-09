package cn.nexttec.rterminal.data

import android.content.Context
import com.google.common.primitives.Bytes

interface GrpcContract{
    interface Service{
        fun getContext():Context
        fun onServerUnreachable()
        fun onCreateSession(success:Boolean)
        fun onDataReceived(bytes: Bytes)
        fun onSessionLost()
        fun onReceiveFail()
    }

    interface Action{
        fun keepAlive()
        fun createSession()
        fun restoreSession()
        fun leaveSession()
        fun read(): ByteArray?
        fun write(bytes:ByteArray, length:Int)
    }
}