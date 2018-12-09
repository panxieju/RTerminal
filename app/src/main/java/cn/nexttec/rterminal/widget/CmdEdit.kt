package cn.nexttec.rterminal.widget

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent.*
import android.view.inputmethod.EditorInfo

interface OnEditListener{
    fun newCommand(command: String)
}

class CmdEdit @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : android.support.v7.widget.AppCompatEditText(context, attrs, defStyleAttr) {

    /**
     * CmdWindow要实现的功能
     * 1）显示命令及输出
     * 2）输出显示之后，自动显示提示符，并将光标移动到末尾
     * 3）当前㎡不足于显示所有输出时，自动滚动到末尾
     * 4）禁止光标移动到除了最后一行之外的其他行
     * 5) 按下Enter之后自动生成带promote的新一行
     */
    private val promotion = ">"
    private lateinit var callback:OnEditListener

    init {

        setBackgroundColor(context.getColor(android.R.color.white))
        setTextColor(context.getColor(android.R.color.black))
        inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        setSingleLine(false)
        setHorizontallyScrolling(false)
        //软键盘的ENTER显示为SEND
        imeOptions = EditorInfo.IME_ACTION_DONE

        //获取焦点之后，将光标移动到最末尾
        onFocusChangeListener = OnFocusChangeListener { _, b ->
            if (b) {
                setSelection(text!!.length)
            }
        }

        //todo 激活键盘
        setOnTouchListener { _, _ ->
            isFocusableInTouchMode = true
            false
        }

        setOnClickListener {
            setSelection(text!!.length)
        }


        setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if(lineCount*lineHeight>height){
                log("Scroll to:$scrollY,$oldScrollY")
                scrollTo(0,scrollY)
            }

        }


        setOnKeyListener { _, i, keyEvent ->
            log("keycode is ${i.toString()} and keyevent is ${keyEvent.toString()}")
            if(i== KEYCODE_DEL){
                val reg = ".?(#|>)$".toRegex()
                val texts = text!!.split("\n")
                if(reg.matches(texts[texts.size-1].toString())){
                    log("Match!")
                    true
                }
                else{
                    log("Unmatch")
                    false
                }
            }
            else
            false
        }

        //todo 处理Enter事件
        setOnEditorActionListener { _, i, keyEvent ->
            log("setOnEditorActionListener $i,$keyEvent")
            keyEvent?.let {
                log(keyEvent.keyCode.toString())
            }
            if (i == 6) {
                sendEnterKey()
                true
            } else
                false
        }

    }

    //@todo 发送Enter事件
    fun sendEnterKey() {
        val command: String = getLastCommand().toString()
        if (command.isNotEmpty()) {
            log("Commmand:$command")
                callback.newCommand(command)
        } else {
            append("\n")
            append(promotion)
            requestFocus()
        }
    }

    fun addOnEditListener(listener: OnEditListener){
        this.callback = listener
    }



    //todo 打印串口接收
    fun addOutput(vararg output:String){
        output.forEach {
            if ("^.?(#|>)$".toRegex().matches(it))
                addCmd("",promote=it)
            else
                append("\n$it")
        }
    }

    fun getLastOutput():String?{
        return text.toString().substringBeforeLast(promotion)
                .substringAfterLast(promotion)
    }

    //todo 输出信息，输出结束之后会自动换行
    fun addInfo(msg:String, showPromote: Boolean=true){
        append(msg)
        if(showPromote) {
            append("\n")
            addCmd("")
        }
    }

    //todo 输出命令
    fun addCmd( message: String,promote: String = promotion,isTip:Boolean=false) {
        append(promote + message)
        if(isTip) {
            append("\n")
            append(promote)
        }
    }

    //todo 获取最后输入的命令
    fun getLastCommand():String{
        text?.let {
            var cmdLine:String
            if(lineCount==1){
                cmdLine = it.toString()
            }
            else{
                val cmds = it.toString()
                cmdLine = it.toString().substringAfterLast("\n")
            }
            if(promotion!=null && promotion.isNotEmpty()){
                val input =  cmdLine.split(promotion)
                return input[input.size-1]
            }
            else{
                return cmdLine
            }
        }
        return ""
    }





}

fun log(msg: String) = Log.i("CmdWindow", msg)
