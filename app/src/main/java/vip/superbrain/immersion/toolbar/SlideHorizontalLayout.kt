package vip.superbrain.immersion.toolbar


import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper

class SlideHorizontalLayout : ViewGroup {

    private var leftView: View? = null
    private var rightView: View? = null
    private var helper: ViewDragHelper? = null

    private var mLeftWidth = 0
    private var mLeftHeight = 0
    private var mRightWidth = 0
    private var mRightHeight = 0
    private var downX: Float = 0F
    private var downY: Float = 0F
    private var hasScroll = false
    private var savedUpTime = 0L
    private var mShowRight = false

    private var touchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

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
        check(childCount == 2) { "child count must equal 2" }
        leftView = getChildAt(0)
        rightView = getChildAt(1)
        helper = ViewDragHelper.create(this, 1f, InnerCallBack())
    }

    private inner class InnerCallBack : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return child === leftView || child === rightView
        }

        override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
            super.onViewCaptured(capturedChild, activePointerId)
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            if (child === leftView) {
                if (left >= width - mLeftWidth) {
                    return width - mLeftWidth
                } else if (left <= width - mLeftWidth - mRightWidth) {
                    return width - mLeftWidth - mRightWidth
                }
            }
            if (child === rightView) {
                if (left >= width) {
                    return width
                } else if (left <= width - mRightWidth) {
                    return width - mRightWidth
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
            if (changedView === leftView) {
                rightView?.also {
                    it.layout(
                        left + mLeftWidth,
                        top,
                        left + mLeftWidth + mRightWidth,
                        top + mRightHeight
                    )
                }
            }
            if (changedView === rightView) {
                leftView?.also {
                    it.layout(
                        left - mLeftWidth,
                        top,
                        left - mLeftWidth + mRightWidth,
                        top + mLeftHeight
                    )
                }
            }
            invalidate()
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val left = rightView!!.left
            if (width - left > mRightWidth / 2) {
                showRight(true)
                listener?.onOpen(this@SlideHorizontalLayout)
            } else {
                showRight(false)
                listener?.onClose(this@SlideHorizontalLayout)
            }
            super.onViewReleased(releasedChild, xvel, yvel)
        }
    }

    fun showRightForce(showRight: Boolean, force: Boolean) {
        if (force) {
            showRight(showRight)
        } else {
            if (mShowRight == showRight) {
                return
            }
            showRight(showRight)
        }
    }

    protected fun showRight(showRight: Boolean) {
        if (showRight) {
            helper?.smoothSlideViewTo(leftView!!, width - mLeftWidth - mRightWidth, 0)
            helper?.smoothSlideViewTo(rightView!!, width - mRightWidth, 0)
            mShowRight = true
        } else {
            helper?.smoothSlideViewTo(rightView!!, width, 0)
            helper?.smoothSlideViewTo(leftView!!, width - mLeftWidth, 0)
            mShowRight = false
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
        val pWidth = MeasureSpec.getSize(widthMeasureSpec)
        val pHeight = MeasureSpec.getSize(heightMeasureSpec)
        measureDefineChild(leftView, widthMeasureSpec, heightMeasureSpec, pWidth, pHeight)
        measureDefineChild(rightView, widthMeasureSpec, heightMeasureSpec, pWidth, pHeight)
        setMeasuredDimension(
            leftView!!.measuredWidth + rightView!!.measuredWidth,
            Math.max(leftView!!.measuredHeight, rightView!!.measuredHeight)
        )
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

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        mLeftWidth = leftView!!.measuredWidth
        mLeftHeight = leftView!!.measuredHeight
        mRightHeight = rightView!!.measuredHeight
        mRightWidth = rightView!!.measuredWidth
        if (mShowRight) {
            leftView!!.layout(width - mLeftWidth - mRightWidth, 0, width - mRightWidth, mLeftHeight)
            rightView!!.layout(width - mRightWidth, 0, width + mLeftWidth, mRightHeight)
        } else {
            leftView!!.layout(width - mLeftWidth, 0, width, mLeftHeight)
            rightView!!.layout(width, 0, width + mLeftWidth + mRightWidth, mRightHeight)
        }
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
                        listener?.also {
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

    private var listener: OnSlideListener? = null

    interface OnSlideListener {
        fun onOpen(slideDelete: SlideHorizontalLayout?)
        fun onClose(slideDelete: SlideHorizontalLayout?)
        fun onClick(slideDelete: SlideHorizontalLayout?)
    }

    fun setListener(listener: OnSlideListener?) {
        this.listener = listener
    }
}