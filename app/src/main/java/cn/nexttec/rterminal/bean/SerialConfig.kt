import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import cn.nexttec.rterminal.common.externDataPath
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Centros on 2018/12/7.
 */

class SerialConfig(
) {
    var vendor: String = "DEFAULT"
    var baudrate: Int = 38400
    var databits: Byte = 8
    var stopbit: Byte = 1
    var parity: Byte = 0
    var flowcontrol: Byte = 0

    fun reset() {
        vendor = "DEFAULT"
        baudrate = 38400
        databits = 8
        stopbit = 1
        parity = 0
        flowcontrol = 0
    }

    companion object {

        @Throws(IOException::class)
        fun readFromFile(filepath: String, filename: String): List<SerialConfig> {
            val configList = arrayListOf<SerialConfig>()
            val file = File(filepath, filename)
            val content = file.reader()
            val sb = StringBuilder()
            var count = 0
            content.forEachLine {
                if (it.matches("\\[\\w+\\]".toRegex())) {
                    if (count > 0) {
                        sb.append(";")
                    }
                    count++
                }
                sb.append("${it.replace('/', '#').split('#')[0].replace(" ", "")},")
            }
            val segment = sb.toString().split(";")
            val len = segment.size
            for (i in 0..(len - 1)) {
                configList.add(SerialConfig.parseFromString(segment[i]))
            }
            return configList
        }

        fun parseFromString(string: String): SerialConfig {
            val config = SerialConfig()
            val lines = string.split(",")
            for (line in lines) {
                if (line.matches("\\[\\w+\\]".toRegex())) {
                    config.vendor = line.substringBefore(']').substringAfter('[').replace(" ", "")
                }
                if (line.contains("baudrate")) {
                    config.baudrate = Integer.parseInt(line.substringAfter("="))
                }
                if (line.contains("databits")) {
                    config.databits = Integer.parseInt(line.substringAfter("=")).toByte()
                }
                if (line.contains("stopbit")) {
                    config.stopbit = Integer.parseInt(line.substringAfter("=")).toByte()
                }
                if (line.contains("parity")) {
                    config.parity = Integer.parseInt(line.substringAfter("=")).toByte()
                }
                if (line.contains("flowcontrol")) {
                    config.flowcontrol = Integer.parseInt(line.substringAfter("=")).toByte()
                }
            }
            return config
        }


        /**
        * todo 修改vendors.ini文件
         * @param config 当前的配置
         * @param filepath 外部存储器中vendors.ini的保存目录
         */
        fun modifyIniFile(config: SerialConfig, filepath: String, filename: String = "vendors.ini") {
            val configs = SerialConfig.readFromFile(filepath, filename)
            if (config.vendor in configs.map { it.vendor }) {
                for (cfg in configs) {
                    if (cfg.vendor == config.vendor) {
                        cfg.baudrate = config.baudrate
                        cfg.databits = config.databits
                        cfg.flowcontrol = config.flowcontrol
                        cfg.parity = config.parity
                        cfg.stopbit = config.stopbit
                    }
                }
                writeConfig(filepath, filename, configs);
            } else {
                val newList = arrayListOf<SerialConfig>()
                newList.addAll(configs)
                newList.add(config)
                writeConfig(filepath, filename, newList)
            }
        }

        @Throws(IOException::class)
        private fun writeConfig(filepath: String, filename: String, configs: List<SerialConfig>) {
            val file = File(filepath, filename)
            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
            val sb = StringBuilder()
            for (config in configs) {
                sb.append("[${config.vendor}]\n")
                sb.append("baudrate=${config.baudrate}\n")
                sb.append("databits=${config.databits}\n")
                sb.append("stopbit=${config.stopbit}\n")
                sb.append("parity=${config.parity}\n")
                sb.append("flowcontrol=${config.flowcontrol}\n")
            }
            file.writeText(sb.toString())
        }

        @SuppressLint("CheckResult")
        @Throws(IOException::class)
        fun initialize(filename: String, context: Context) {
            Observable.just(filename)
                .observeOn(Schedulers.io())
                .subscribe {
                    copyAssetFile(context, filename)
                }
        }

    }

    fun toArray(): List<Map<String, String>> {
        var list = ArrayList<Map<String, String>>()
        list.add(mapOf("厂家" to vendor))
        list.add(mapOf("波特率" to "${baudrate}"))
        list.add(mapOf("数据位" to "${databits}Bit"))
        list.add(mapOf("停止位" to "${stopbit}Bit"))
        list.add(
            mapOf(
                "奇偶校验" to "${when (parity.toInt()) {
                    0 -> "None"
                    1 -> "ODD"
                    2 -> "EVEN"
                    3 -> "MARK"
                    4 -> "SPACE"
                    else -> ""
                }
                }"
            )
        )
        list.add(
            mapOf(
                "硬件流控" to "${when (flowcontrol.toInt()) {
                    0 -> {
                        "None"
                    }
                    else -> {
                        "CTS/RTS"
                    }
                }
                }"
            )
        )
        return list
    }

    override fun toString(): String =
        "串口参数-->\n>###########################\n" +
                ">\t\t波特率: $baudrate\n" +
                ">\t\t数据位: ${databits}位\n" +
                ">\t\t停止位: ${if (stopbit == 1.toByte()) "1位" else if (stopbit == 2.toByte()) "2位" else if (stopbit == 3.toByte()) "1.5位" else ""}\n" +
                ">\t\t校验位 ${if (parity == 0.toByte()) "无" else if (parity == 1.toByte()) "奇偶校验" else "偶校验"} \n" +
                ">\t\t流控: ${if (flowcontrol == 0.toByte()) "无" else if (flowcontrol == 1.toByte()) "CTS/RTS" else ""} \n>###########################"

}


/**
 * todo 从Asset中复制文件
 * @param context Context
 * @param fileName String 文件名，不要包含目录
 */
fun copyAssetFile(context: Context, fileName: String) {
    try {
        val externPath = Environment.getExternalStorageDirectory().toString() + "/" + context.packageName + "/"
        val extern = File(externPath)
        if (!extern.exists()) {
            extern.mkdirs()
        }
        val file = File(extern, fileName)

        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()

        val `is` = context.assets.open(fileName)
        val fos = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var byteCount = `is`.read(buffer)
        while (byteCount != -1) {
            fos.write(buffer, 0, byteCount)
            byteCount = `is`.read(buffer)
        }
        fos.flush()
        `is`.close()
        fos.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }


}