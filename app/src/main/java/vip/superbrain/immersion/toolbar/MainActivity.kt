package vip.superbrain.immersion.toolbar

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import kotlinx.android.synthetic.main.activity_main.*
import vip.superbrain.immersion.lib.toolbar.StatusBarUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StatusBarUtils.transparencyBar(this)
        StatusBarUtils.setDarkMode(this, true)
        init()
    }

    fun init() {
        var content =
            "<p><font size=\"3\" color=\"red\">设置了字号和颜色</font></p>" +
                    "<b><font size=\"5\" color=\"blue\">设置字体加粗 蓝色 5号</font></font></b></br>" +
                    "<h1>这个是H1标签</h1></br>" +
                    "<p>这里显示图片：</p><img src=\"https://img0.pconline.com.cn/pconline/1808/06/11566885_13b_thumb.jpg\""
        var contentSpanned: Spanned = Html.fromHtml(content)
        tvContent.text = SpannableStringBuilder().apply {
            append(SpannableString("(").also { spanString ->
                spanString.setSpan(
                    AbsoluteSizeSpan(12, true),
                    0,
                    spanString.length,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            })
            "12"?.apply {
                append(SpannableString(if (this.isEmpty()) "0" else this).also { spanString ->
                    spanString.setSpan(
                        AbsoluteSizeSpan(15, true),
                        0,
                        spanString.length,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                })
            }
            append(SpannableString("笔)").also { spanString ->
                spanString.setSpan(
                    AbsoluteSizeSpan(12, true),
                    0,
                    spanString.length,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            })
            this.setSpan(
                ForegroundColorSpan(Color.parseColor("#999999")),
                0,
                this.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            this.setSpan(
                VerticalAlignTextSpan(),
                0,
                4,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
//            this.setSpan(
//                VerticalAlignTextSpan(),
//                0,
//                this.length,
//                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE
//            )
        }
    }
}