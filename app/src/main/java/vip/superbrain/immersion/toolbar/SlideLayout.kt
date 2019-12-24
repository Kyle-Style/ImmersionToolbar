package vip.superbrain.immersion.toolbar


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.NonNull
import androidx.annotation.Px
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import vip.superbrain.immersion.toolbar.SlideLayout.OnPropertyListener

class SlideLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    companion object {

        var TAG = SlideLayout::class.java.simpleName

        const val ANCHOR_LEFT = 0x0001
        const val ANCHOR_TOP = 0x0010
        const val ANCHOR_RIGHT = 0x0100
        const val ANCHOR_BOTTOM = 0x1000


        const val HORIZONTAL = LinearLayout.HORIZONTAL
        const val VERTICAL = LinearLayout.VERTICAL
    }

    val onPropertyListener: OnPropertyListener?
    var forgroundPaint: Paint

    init {
        onPropertyListener = object : OnPropertyListener {
            override fun onViewPositionChanged(
                changedView: View,
                left: Int,
                top: Int,
                dx: Int,
                dy: Int
            ) {
                mLeft = left
                mTop = top
                mDx = dx
                mDy = dy
                invalidate()
                Log.e(TAG, "changedView left $left top $top dx $dx dy $dy")
            }
        }
        forgroundPaint = Paint()
        forgroundPaint.textSize = 40F
        forgroundPaint.isAntiAlias = true
    }

    private var contentView: View? = null
    private var helper: ViewDragHelper? = null

    private var hasScroll = false
    private var savedUpTime = 0L
    private var mShow = false

    private var downX: Float = 0F
    private var downY: Float = 0F

    private var contentHidePercent = 0.5F

    private var touchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop

    private var mLeft = 0
    private var mTop = 0
    private var mDx = 0
    private var mDy = 0

    // 吸附点位置
    private var anchorPosition = ANCHOR_TOP

    override fun generateLayoutParams(attrs: AttributeSet): MarginLayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        check(childCount <= 1) { "SlideLayout can host only one child" }
        if (childCount == 0) {
            return
        }
        contentView = getChildAt(0)
        helper = ViewDragHelper.create(this, 1f, InnerCallBack(onPropertyListener))
    }

    private inner class InnerCallBack constructor(var listener: OnPropertyListener? = null) :
        ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child === contentView
        }

        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            super.onViewCaptured(capturedChild, activePointerId)
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            super.clampViewPositionVertical(child, top, dy)
            if (child === contentView) {
                when (anchorPosition) {
                    ANCHOR_TOP -> {
                        if (top < -getHideHeight()) {
                            return -getHideHeight()
                        } else if (top > 0) {// 此处可以添加是否限制left
                            return 0
                        }
                    }
                    else -> {
                        if (top < height - getHideHeight()) {
                            return height - getHideHeight()
                        } else if (top > height - getHideHeight()) {// 此处可以添加是否限制left
                            return height - getHideHeight()
                        }
                    }
                }
            }
            return top
        }

        // 限制水平范围
        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            if (child === contentView) {
                when (anchorPosition) {
                    ANCHOR_LEFT -> {
                        if (left < -getHideWidth()) {
                            return -getHideWidth()
                        } else if (left > 0) {// 此处可以添加是否限制left
                            return 0
                        }
                    }
                    else -> {
                        if (left < width - getContentWidth()) {
                            return width - getContentWidth()
                        } else if (left > width - getHideWidth()) {// 此处可以添加是否限制left
                            return width - getHideWidth()
                        }
                    }
                }
            }
            return left
        }

        // 位置变动
        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            listener?.also {
                it.onViewPositionChanged(changedView, left, top, dx, dy)
            }
            if (changedView === contentView) {
                when (anchorPosition) {
                    ANCHOR_TOP, ANCHOR_BOTTOM -> {
                        contentView!!.layout(
                            (width - getContentWidth()) / 2,
                            top,
                            (width + getContentWidth()) / 2,
                            top + getContentHeight()
                        )
                    }
                    ANCHOR_LEFT, ANCHOR_RIGHT -> {
                        contentView!!.layout(
                            left,
                            (height - getContentHeight()) / 2,
                            left + getContentWidth(),
                            (height + getContentHeight()) / 2
                        )
                    }
                    else -> {
                    }
                }
            }
            invalidate()
        }

        // 根据滑动距离判断是否滑动到目的地
        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            var childLeft = releasedChild.left
            var childTop = releasedChild.top
            when (anchorPosition) {
                ANCHOR_LEFT -> {
                    show(childLeft > getHideWidth() / 2)
                }
                ANCHOR_TOP -> {
                    show(childTop > getHideHeight() / 2)
                }
                ANCHOR_BOTTOM -> {
                    show(childTop < width - (getHideHeight() + getContentHeight()) / 2)
                }
                else -> {
                    show(childLeft < width - (getHideWidth() + getContentWidth()) / 2)
                }
            }
            super.onViewReleased(releasedChild, xvel, yvel)
        }
    }

    // TODO 根据不同的吸附点滑动
    fun showForce(show: Boolean, force: Boolean) {
        if (force) {
            show(show)
        } else {
            if (mShow == show) {
                return
            }
            show(show)
        }
    }

    // TODO 根据不同的吸附点滑动
    protected fun show(show: Boolean) {
        if (show) {
            onSlideListener?.onOpen(this@SlideLayout)
        } else {
            onSlideListener?.onClose(this@SlideLayout)
        }
        when (anchorPosition) {
            ANCHOR_LEFT -> {
                if (show) {
                    helper?.smoothSlideViewTo(
                        contentView!!,
                        0,
                        (height - getContentHeight()) / 2
                    )
                    mShow = true
                } else {
                    helper?.smoothSlideViewTo(
                        contentView!!,
                        -getHideWidth(),
                        (height - getContentHeight()) / 2
                    )
                    mShow = false
                }
            }
            ANCHOR_TOP -> {
                if (show) {
                    helper?.smoothSlideViewTo(
                        contentView!!,
                        (width - getContentWidth()) / 2,
                        0
                    )
                    mShow = true
                } else {
                    helper?.smoothSlideViewTo(
                        contentView!!,
                        (width - getContentWidth()) / 2,
                        height - (getContentHeight() - getHideHeight())
                    )
                    mShow = false
                }
            }
            ANCHOR_BOTTOM -> {
            }
            else -> {
                if (show) {
                    helper?.smoothSlideViewTo(
                        contentView!!,
                        width - getContentWidth(),
                        (height - getContentHeight()) / 2
                    )
                    mShow = true
                } else {
                    helper?.smoothSlideViewTo(
                        contentView!!,
                        width - (getContentWidth() - getHideWidth()),
                        (height - getContentHeight()) / 2
                    )
                    mShow = false
                }
            }
        }
        ViewCompat.postInvalidateOnAnimation(this)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (helper?.continueSettling(true)!!) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (childCount == 1) {
            val pWidth = MeasureSpec.getSize(widthMeasureSpec)
            val pHeight = MeasureSpec.getSize(heightMeasureSpec)
            measureDefineChild(contentView, widthMeasureSpec, heightMeasureSpec, pWidth, pHeight)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDrawForeground(canvas: Canvas?) {
        super.onDrawForeground(canvas)
        canvas?.drawText(
            "width $width contentWidth ${getContentWidth()} contentHeight ${getContentHeight()}}",
            100F,
            100F,
            forgroundPaint
        )
        canvas?.drawText(
            "hideWidth ${getHideWidth()} hideHeight ${getHideHeight()}",
            100F,
            150F,
            forgroundPaint
        )
        canvas?.drawText("dx $mDx dy $mDy", 100F, 200F, forgroundPaint)
        canvas?.drawText(
            "child left ${contentView?.left} top ${contentView?.top}",
            100F,
            250F,
            forgroundPaint
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount == 1) {
            when (anchorPosition) {
                ANCHOR_LEFT -> {
                    if (mShow) {
                        contentView!!.layout(
                            0,
                            (height - getContentHeight()) / 2,
                            getContentWidth(),
                            (height + getContentHeight()) / 2
                        )
                    } else {
                        contentView!!.layout(
                            -getHideWidth(),
                            (height - getContentHeight()) / 2,
                            -getHideWidth() + getContentWidth(),
                            (height + getContentHeight()) / 2
                        )
                    }
                }
                ANCHOR_TOP -> {
                    if (mShow) {
                        contentView!!.layout(
                            (width - getContentWidth()) / 2,
                            0,
                            (width + getContentWidth()) / 2,
                            getContentHeight()
                        )
                    } else {
                        contentView!!.layout(
                            (width - getContentWidth()) / 2,
                            -getHideHeight(),
                            (width + getContentWidth()) / 2,
                            getContentHeight() - getHideHeight()
                        )
                    }
                }
                ANCHOR_BOTTOM -> {
                    if (mShow) {
                        contentView!!.layout(
                            (width - getContentWidth()) / 2,
                            height - getContentHeight(),
                            (width + getContentWidth()) / 2,
                            height
                        )
                    } else {
                        contentView!!.layout(
                            (width - getContentWidth()) / 2,
                            height - getHideHeight(),
                            (width + getContentWidth()) / 2,
                            height + getHideHeight()
                        )
                    }
                }
                else -> {
                    if (mShow) {
                        contentView!!.layout(
                            width - getContentWidth(),
                            (height - getContentHeight()) / 2,
                            width,
                            (height + getContentHeight()) / 2
                        )
                    } else {
                        contentView!!.layout(
                            width - (getContentWidth() - getHideWidth()),
                            (height - getContentHeight()) / 2,
                            width + getHideWidth(),
                            (height + getContentHeight()) / 2
                        )
                    }
                }
            }
        }
    }

    private fun getContentWidth(): Int {
        return contentView!!.measuredWidth
    }

    private fun getContentHeight(): Int {
        return contentView!!.measuredHeight
    }

    private fun getHideWidth(): Int {
        return (contentHidePercent * getContentWidth()).toInt()
    }

    private fun getHideHeight(): Int {
        return (contentHidePercent * getContentHeight()).toInt()
    }

    private fun measureDefineChild(
        child: View?,
        widthMeasureSpec: Int,
        heightMeasureSpec: Int,
        pWidth: Int,
        pHeight: Int
    ) {
        val contentLayoutParams = child!!.layoutParams
        val height = contentLayoutParams.height
        val width = contentLayoutParams.width
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var contentWidthMeasureSpec = 0
        if (widthMode == MeasureSpec.EXACTLY) {
            if (width == LayoutParams.MATCH_PARENT) {
                contentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(pWidth, MeasureSpec.EXACTLY)
            } else if (width > 0) {
                contentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    Math.min(width, pWidth),
                    MeasureSpec.EXACTLY
                )
            } else if (width == LayoutParams.WRAP_CONTENT) {
                contentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    Math.min(width, pWidth),
                    MeasureSpec.AT_MOST
                )
            }
        } else if (widthMode == MeasureSpec.AT_MOST) {
            if (width == LayoutParams.MATCH_PARENT) {
                contentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(pWidth, MeasureSpec.AT_MOST)
            } else if (width > 0) {
                contentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    Math.min(width, pWidth),
                    MeasureSpec.EXACTLY
                )
            } else if (width == LayoutParams.WRAP_CONTENT) {
                contentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    Math.min(width, pWidth),
                    MeasureSpec.AT_MOST
                )
            }
        }
        var contentHeightMeasureSpec = 0
        if (heightMode == MeasureSpec.EXACTLY) {
            if (height == LayoutParams.MATCH_PARENT) {
                contentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(pHeight, MeasureSpec.EXACTLY)
            } else if (height == LayoutParams.WRAP_CONTENT) {
                contentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(pHeight, MeasureSpec.AT_MOST)
            } else if (height > 0) {
                contentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            }
        } else if (heightMode == MeasureSpec.AT_MOST) {
            if (height == LayoutParams.MATCH_PARENT) {
                contentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(pHeight, MeasureSpec.AT_MOST)
            } else if (height == LayoutParams.WRAP_CONTENT) {
                contentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(pHeight, MeasureSpec.AT_MOST)
            } else if (height > 0) {
                contentHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            }
        }
        child.measure(contentWidthMeasureSpec, contentHeightMeasureSpec)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
                hasScroll = false
            }
            MotionEvent.ACTION_MOVE -> {
                val offsetX = ev.x - downX
                val offsetY = ev.y - downY
                if (Math.abs(offsetX) > touchSlop || Math.abs(offsetY) > touchSlop) {
                    hasScroll = true
                    parent.requestDisallowInterceptTouchEvent(true)
                } else {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            MotionEvent.ACTION_UP -> {
                if (!hasScroll) {
                    if (System.currentTimeMillis() - savedUpTime > 800) {
                        onSlideListener?.also {
                            it.onClick(this@SlideLayout)
                        }
                    } else {
                    }
                    savedUpTime = System.currentTimeMillis()
                } else {
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return helper?.shouldInterceptTouchEvent(ev)!!
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        helper?.processTouchEvent(ev)
        return true
    }

    private var onSlideListener: OnSlideListener? = null

    interface OnSlideListener {
        fun onOpen(slideDelete: SlideLayout?)
        fun onClose(slideDelete: SlideLayout?)
        fun onClick(slideDelete: SlideLayout?)
    }

    interface OnPropertyListener {
        fun onViewPositionChanged(
            @NonNull changedView: View, left: Int,
            top: Int, @Px dx: Int, @Px dy: Int
        )
    }

    fun setListener(listener: OnSlideListener?) {
        this.onSlideListener = listener
    }
}