package cn.nexttec.rterminal.data.impl

import RSerial.RSerialGrpc
import RSerial.RSerialOuterClass
import android.content.Context
import android.util.Log
import cn.nexttec.rterminal.RTerminalApplication
import cn.nexttec.rterminal.common.*
import cn.nexttec.rterminal.data.GrpcContract
import com.google.protobuf.ByteString
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusRuntimeException
import java.lang.Exception
import javax.inject.Inject

class GrpcImpl<T:GrpcContract.Service> : GrpcContract.Action {
    private lateinit var sessionId: String
    private lateinit var context: Context
    private lateinit var channel: ManagedChannel
    private lateinit var stub: RSerialGrpc.RSerialBlockingStub
    lateinit var service:T

    init {
        context = service.getContext()
        val mPreference = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        val

                sessionId = mPreference.getString(SESSION, "0")
        channel = ManagedChannelBuilder.forAddress(SERVER, PORT)
            .usePlaintext(true)
            .build()

        synchronized(this) {
            if (channel.isShutdown || channel.isTerminated) {
                channel = ManagedChannelBuilder.forAddress(SERVER, PORT)
                    .usePlaintext(true)
                    .build()
            }

            stub = RSerialGrpc.newBlockingStub(channel)
        }
        keepAlive()
    }

    override fun keepAlive() {
        KeepAliveThread().start()
    }

    override fun createSession() {
        try {
            val ret = stub.createSession(req)
        }
        catch (e:Exception){

        }

    }

    override fun restoreSession() {
        stub.restoreSession(req)
    }

    override fun leaveSession() {
        stub.leaveSession(req)
    }

    /**
     * 从服务器读取数据
     * @return ByteArray?
     */
    override fun read(): ByteArray? {
        try {
            val ret = stub.readBytes(req)
            return ret.data.toByteArray()
        }
        catch (e:Exception){
            service.onReceiveFail()
        }
        return null
    }

    override fun write(bytes: ByteArray, length:Int) {
        stub.writeBytes(data(bytes,length))
    }

    /**
     *  新建线程与服务器保持通信
     */
    inner class KeepAliveThread : Thread() {
        override fun run() {
            var isServerReachable = true
            while (isServerReachable) {
                try {
                    Log.i(
                        "KEEPALIVE",
                        "Send out keepalive message, ${RTerminalApplication.localId}, ${RTerminalApplication.sessionId}"
                    )
                    val ret = stub.keepAlive(
                        RSerialOuterClass.ClientStatus.newBuilder()
                            .setId(RTerminalApplication.localId)
                            .setSessionId(RTerminalApplication.sessionId)
                            .build()
                    )
                    if (ret.sessionStatus){
                        val data = read()
                    }
                    Thread.sleep(500)
                } catch (exception: StatusRuntimeException) {
                    Thread.sleep(1500)
                    isServerReachable = false
                    service.onSessionLost()
                }
            }
        }

    }
}

//todo 构成grpc req请求
val req = RSerialOuterClass.Req.newBuilder()
    .setId(RTerminalApplication.localId)
    .setSessionId(RTerminalApplication.sessionId)
    .setTimestamp(timestamp)
    .build()

//todo 构成向服务器上传的数据
val data = { bytes: ByteArray, length:Int ->
    RSerialOuterClass.ByteData.newBuilder()
        .setId(RTerminalApplication.localId)
        .setSessionId(RTerminalApplication.sessionId)
        .setData(ByteString.copyFrom(bytes,0,length))
        .setTimestamp(timestamp)
        .build()
}


