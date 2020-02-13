//package vip.superbrain.immersion.toolbar
//
//import android.content.Context
//import android.util.AttributeSet
//import android.view.inputmethod.EditorInfo
//import android.view.inputmethod.InputConnection
//import android.view.inputmethod.InputConnectionWrapper
//import androidx.appcompat.widget.AppCompatEditText
//
///**
// * @Description
// *
// * @Author wangleilei
// * @Email wangleilei@mockuai.com
// * @Date 2020-02-10 21:30
// */
//class TextLengthEditText : AppCompatEditText, TextLengthListener {
//
//    constructor(context: Context) : super(context)
//    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
//    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
//
//    private var listener: TextLengthListener? = null
//
//    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection {
//        return TextLengthInputConnecttion(super.onCreateInputConnection(outAttrs), listener = TextLengthEditText@ this)
//    }
//
//    override fun onTextLengthOutOfLimit() {
//        listener?.onTextLengthOutOfLimit()
//    }
//
//    fun setTextLengthListener(l: TextLengthListener) {
//        listener = l
//    }
//
//    inner class TextLengthInputConnecttion(
//        val target: InputConnection,
//        private val maxLength: Int = Utils.MAX_LENGTH,
//        val listener: TextLengthListener? = null
//    ) : InputConnectionWrapper(target, false) {
//
//        override fun commitText(source: CharSequence, newCursorPosition: Int): Boolean {
//            val count = Utils.calcTextLength(source)
//            val destCount = Utils.calcTextLength(text as CharSequence, selectionStart, selectionEnd)
//            if (count + destCount > maxLength) {
//                // 超过了sum个字符，需要截取
//                var sum = count + destCount - maxLength
//                val delete = Utils.getDeleteIndex(source, 0, source.length, sum)
//                listener?.onTextLengthOutOfLimit()
//                // 输入字符超过了限制，截取
//                return super.commitText(if (delete > 0) source.subSequence(0, delete) else "", newCursorPosition)
//            }
//            return super.commitText(source, newCursorPosition)
//        }
//    }
//}