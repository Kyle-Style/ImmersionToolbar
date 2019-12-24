package vip.superbrain.immersion.toolbar


import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper

class SlideHorizontalLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {

    companion object {

        var TAG = SlideLayout::class.java.simpleName

        const val ANCHOR_LEFT = 0x0001
        const val ANCHOR_RIGHT = 0x0100
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

    // 吸附点位置
    private var anchorPosition = ANCHOR_RIGHT

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
        check(childCount <= 1) { "${this::class.java.simpleName} can host only one child" }
        if (childCount == 0) {
            return
        }
        contentView = getChildAt(0)
        helper = ViewDragHelper.create(this, 1f, InnerCallBack())
    }

    private inner class InnerCallBack : ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child === contentView
        }

        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            super.onViewCaptured(capturedChild, activePointerId)
        }

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

        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            if (changedView === contentView) {
                when (anchorPosition) {
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

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            var childLeft = releasedChild.left
            when (anchorPosition) {
                ANCHOR_LEFT -> {
                    show(childLeft > getHideWidth() / 2)
                }
                else -> {
                    show(childLeft < width - (getHideWidth() + getContentWidth()) / 2)
                }
            }
            super.onViewReleased(releasedChild, xvel, yvel)
        }
    }

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

    protected fun show(show: Boolean) {
        if (show) {
            onSlideListener?.onOpen(this@SlideHorizontalLayout)
        } else {
            onSlideListener?.onClose(this@SlideHorizontalLayout)
        }
        when (anchorPosition) {
            ANCHOR_LEFT -> {
                if (show) {
                    helper?.smoothSlideViewTo(contentView!!, 0, (height - getContentHeight()) / 2)
                    mShow = true
                } else {
                    helper?.smoothSlideViewTo(contentView!!, -getHideWidth(), (height - getContentHeight()) / 2)
                    mShow = false
                }
            }
            else -> {
                if (show) {
                    helper?.smoothSlideViewTo(contentView!!, width - getContentWidth(), (height - getContentHeight()) / 2)
                    mShow = true
                } else {
                    helper?.smoothSlideViewTo(contentView!!, width - (getContentWidth() - getHideWidth()), (height - getContentHeight()) / 2)
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
        setMeasuredDimension(contentView!!.measuredWidth, contentView!!.measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount == 1) {
            when (anchorPosition) {
                ANCHOR_LEFT -> {
                    if (mShow) {
                        contentView!!.layout(0, (height - getContentHeight()) / 2, getContentWidth(), (height + getContentHeight()) / 2)
                    } else {
                        contentView!!.layout(-getHideWidth(), (height - getContentHeight()) / 2, -getHideWidth() + getContentWidth(), (height + getContentHeight()) / 2)
                    }
                }
                else -> {
                    if (mShow) {
                        contentView!!.layout(width - getContentWidth(), (height - getContentHeight()) / 2, width, (height + getContentHeight()) / 2)
                    } else {
                        contentView!!.layout(width - (getContentWidth() - getHideWidth()), (height - getContentHeight()) / 2, width + getHideWidth(), (height + getContentHeight()) / 2)
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

    private fun measureDefineChild(child: View?, widthMeasureSpec: Int, heightMeasureSpec: Int, pWidth: Int, pHeight: Int) {
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
                contentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(width, pWidth), MeasureSpec.EXACTLY)
            } else if (width == LayoutParams.WRAP_CONTENT) {
                contentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(width, pWidth), MeasureSpec.AT_MOST)
            }
        } else if (widthMode == MeasureSpec.AT_MOST) {
            if (width == LayoutParams.MATCH_PARENT) {
                contentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(pWidth, MeasureSpec.AT_MOST)
            } else if (width > 0) {
                contentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(width, pWidth), MeasureSpec.EXACTLY)
            } else if (width == LayoutParams.WRAP_CONTENT) {
                contentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(width, pWidth), MeasureSpec.AT_MOST)
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
                            it.onClick(this@SlideHorizontalLayout)
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
        fun onOpen(slideDelete: SlideHorizontalLayout?)
        fun onClose(slideDelete: SlideHorizontalLayout?)
        fun onClick(slideDelete: SlideHorizontalLayout?)
    }

    fun setListener(listener: OnSlideListener?) {
        this.onSlideListener = listener
    }
}