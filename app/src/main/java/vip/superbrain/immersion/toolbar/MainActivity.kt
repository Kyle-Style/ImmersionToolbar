package vip.superbrain.immersion.toolbar

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.Px
import androidx.appcompat.app.AppCompatActivity
import androidx.customview.widget.ViewDragHelper
import kotlinx.android.synthetic.main.activity_main.*
import vip.superbrain.immersion.lib.toolbar.StatusBarUtils

class MainActivity : AppCompatActivity() {

//    var constraintSet1 = ConstraintSet()
//    var constraintSet2 = ConstraintSet()

    var viewDragHelper: ViewDragHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StatusBarUtils.transparencyBar(this)
        StatusBarUtils.setDarkMode(this, true)
        init()
    }

    var isChange = false

    fun init() {
//        constraintSet1.clone(csytOuter)
//        constraintSet2.clone(this, R.layout.layout_exchange)
//
//        btnBottom.setOnClickListener {
//            isChange = !isChange
//            if (isChange) {
//                TransitionManager.beginDelayedTransition(csytOuter);
//                constraintSet2.applyTo(csytOuter)
//            } else {
//                var transition = AutoTransition()
//                transition.setDuration(500)
//                TransitionManager.beginDelayedTransition(csytOuter, transition)
//                constraintSet1.applyTo(csytOuter)
//            }
//        }

        btnChangeStatus.setOnClickListener {
            isChange = !isChange
            shytContent.showRightForce(isChange, false)
            Log.e(MainActivity::class.java.simpleName, "状态改变了")
        }

        shytContent.setListener(object : SlideHorizontalLayout.OnSlideListener {


            override fun onOpen(slideDelete: SlideHorizontalLayout?) {

            }

            override fun onClose(slideDelete: SlideHorizontalLayout?) {

            }

            override fun onClick(slideDelete: SlideHorizontalLayout?) {
                Toast.makeText(baseContext, "单机我了呀", Toast.LENGTH_SHORT).show()
            }
        })

        viewDragHelper = ViewDragHelper.create(csytOuter, object : ViewDragHelper.Callback() {

            override fun onViewDragStateChanged(state: Int) {}

            override fun onViewPositionChanged(
                changedView: View,
                left: Int,
                top: Int, @Px dx: Int,
                @Px dy: Int
            ) {
            }

            override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            }

            override fun onEdgeTouched(edgeFlags: Int, pointerId: Int) {}

            override fun onEdgeLock(edgeFlags: Int): Boolean {
                return false
            }

            override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {

            }

            override fun getOrderedChildIndex(index: Int): Int {
                return index
            }

            override fun getViewHorizontalDragRange(child: View): Int {
                return 0
            }

            override fun getViewVerticalDragRange(child: View): Int {
                return 0
            }

            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                return false
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                return 0
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                return 0
            }
        })


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