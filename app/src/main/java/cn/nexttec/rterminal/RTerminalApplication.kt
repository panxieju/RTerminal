package cn.nexttec.rterminal

import cn.wch.ch34xuartdriver.CH34xUARTDriver
import java.sql.Driver
import javax.inject.Inject

class RTerminalApplication{

    companion object {
        lateinit var localId:String
        lateinit var driver: CH34xUARTDriver
        lateinit var sessionId:String
    }
}