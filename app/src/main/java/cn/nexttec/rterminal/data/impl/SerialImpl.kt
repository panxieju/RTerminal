package cn.nexttec.rterminal.data.impl

import android.content.Context
import cn.nexttec.rterminal.RTerminalApplication
import cn.nexttec.rterminal.data.SerialContact
import cn.wch.ch34xuartdriver.CH34xUARTDriver
import javax.inject.Inject

class SerialImpl<T:SerialContact.Service> :SerialContact.Action{

    lateinit var service:T
    private var isOpen:Boolean = false

    lateinit var driver:CH34xUARTDriver

    override fun checkUsbHostSupport() {
       if (!driver.UsbFeatureSupported()) 
           service.onUsbHostSupportFailed()
        else{

       }
    }

    override fun openSerial() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setConfig() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeString(cmd: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeByte(byte: Byte) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read() {
        ReadThread(service.getContext())
    }

    //todo 新建线程接收数据
    private inner class ReadThread(private val context: Context) : Thread() {

        override fun run() {

            val buffer = ByteArray(4096)

            while (true) {
                if (!isOpen) {
                    break
                }
                val length = RTerminalApplication.driver.ReadData(buffer, 4096)
                if (length > 0) {
                    service.onReadBytes(buffer,length)
                    val builder = StringBuilder()
                    for (i in 0 until length) {
                        if (buffer[i].toInt() != 0) {
                            builder.append(buffer[i].toChar())
                        }
                    }
                    val data = builder.toString()
                    service.onReadString(data)
                }
            }
        }
    }

}