package cn.nexttec.rterminal.data

import android.content.Context

interface SerialContact {
    interface Service {
        fun getContext(): Context
        fun onUsbHostSupportFailed()
        fun onSerialOpen(success: Boolean)
        fun onSerialConfigSet(success: Boolean)
        fun onReadString(data: String)
        fun onReadBytes(data: ByteArray, length: Int)
        fun onWrite(success: Boolean)
    }

    interface Action{
        fun checkUsbHostSupport()
        fun openSerial()
        fun setConfig()
        fun writeString(cmd:String)
        fun writeByte(byte:Byte)
        fun read()
    }
}