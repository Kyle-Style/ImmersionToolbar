package vip.superbrain.immersion.toolbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import com.vdurmont.emoji.EmojiManager
import kotlinx.android.synthetic.main.activity_main.*
import vip.superbrain.immersion.lib.toolbar.StatusBarUtils
import vip.superbrain.immersion.toolbar.utils.EmojiHelper
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StatusBarUtils.transparentizeStatusBar(this)
        StatusBarUtils.setDarkMode(this, true)
        initListeners()
    }

    private fun initListeners() {
        tvEmojiTest.movementMethod = ScrollingMovementMethod.getInstance();
        btnEmoji.setOnClickListener {
            var sb = StringBuilder()
            var count = 0
            var content = EmojiManager.getAll()
                .map {
                count++
                Log.e("MainActivity", "$count    ${it.unicode}")
//                sb.append("\t")
//                sb.append("$count")
//                sb.append("\t")
//                sb.append("${it.unicode.length}")
//                sb.append("\t")
                sb.append(it.unicode)
//                sb.append("\n")
            }.toList().apply {
                Log.e("MainActivity", "count   ${count}")
                Log.e("MainActivity", "长度   ${this.size}")
            }
                .toString()
//            Log.e("MainActivity", "codePointCount   ${content.codePointCount(0, content.length)}")

            val testString = "哈哈123abc+  \uD83C\uDDFB\uD83C\uDDE6\uD83C\uDDFB\uD83C\uDDE8\uD83C\uDDFB\uD83C\uDDEE\uD83C\uDDFB\uD83C\uDDE6\uD83C\uDDFE\uD83C\uDDEA\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC77\uDB40\uDC6C\uDB40\uDC73\uDB40\uDC7F\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC77\uDB40\uDC6C\uDB40\uDC73\uDB40\uDC7F\uD83C\uDDFB\uD83C\uDDEE\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC77\uDB40\uDC6C\uDB40\uDC73\uDB40\uDC7F\uD83C\uDDFB\uD83C\uDDEE\uD83C\uDDFB\uD83C\uDDE6\uD83C\uDDFB\uD83C\uDDE6\uD83C\uDDFB\uD83C\uDDE6\uD83C\uDDFB\uD83C\uDDEC\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC77\uDB40\uDC6C\uDB40\uDC73\uDB40\uDC7F\uD83C\uDDFB\uD83C\uDDEE\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC77\uDB40\uDC6C\uDB40\uDC73\uDB40\uDC7F\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC77\uDB40\uDC6C\uDB40\uDC73\uDB40\uDC7F\uD83C\uDDFB\uD83C\uDDEE"
//            Log.e("MainActivity", "EmojiUtils.getEmojiCount   ${EmojiUtils.getEmojiCount(content)}")
//            Log.e("MainActivity", "EmojiUtils.getCharCount2   ${EmojiUtils.getCharCount2(content)}")
            Log.e("MainActivity", "EmojiHelper.getVisionCharCount   ${EmojiHelper.instance.getVisionCharCount(testString)}")
            tvEmojiTest.text = testString
        }
    }

    fun onEffect(view: View) {
        CommonActivity.start(this)
    }
}