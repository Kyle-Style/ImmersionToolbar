package com.netease.nim.uikit.widget.shadow

import android.graphics.*
import android.graphics.drawable.Drawable

class ShadowDrawable private constructor(
    private val mShape: Int,
    bgColor: IntArray,
    shadowColor: Int,
    borderColor: Int,
    shapeRadius: Int,
    shadowRadius: Int,
    borderWidth: Float,
    offsetX: Int,
    offsetY: Int
) : Drawable() {

    private val mShadowPaint: Paint
    private val mBgPaint: Paint
    private val mBorderPaint: Paint

    private val mShadowRadius: Int
    private val mShapeRadius: Int
    private val mBorderWidth: Float

    private val mBgColor: IntArray?
    private val mShadowColor: Int
    private val mBorderColor: Int

    private val mOffsetX: Int
    private val mOffsetY: Int

    private var mRect: RectF? = null

    companion object {
        const val SHAPE_ROUND = 1
        const val SHAPE_CIRCLE = 2
    }

    init {
        mShapeRadius = shapeRadius
        mShadowRadius = shadowRadius
        mOffsetX = offsetX
        mOffsetY = offsetY

        mShadowPaint = Paint()
        mShadowColor = shadowColor
        mShadowPaint.color = mShadowColor
        mShadowPaint.isAntiAlias = true
        mShadowPaint.setShadowLayer(
            shadowRadius.toFloat(),
            offsetX.toFloat(),
            offsetY.toFloat(),
            shadowColor
        )
        mShadowPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)

        mBgPaint = Paint()
        mBgPaint.isAntiAlias = true
        mBgColor = bgColor
        mBgPaint.color = mBgColor[0]

        mBorderPaint = Paint()
        mBorderPaint.isAntiAlias = true
        mBorderColor = borderColor
        mBorderPaint.color = mBorderColor
        mBorderPaint.style = Paint.Style.STROKE
        mBorderWidth = borderWidth
        mBorderPaint.strokeWidth = mBorderWidth
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        mRect = RectF(
            (left + mShadowRadius - mOffsetX).toFloat(),
            (top + mShadowRadius - mOffsetY).toFloat(),
            (right - mShadowRadius - mOffsetX).toFloat(),
            (bottom - mShadowRadius - mOffsetY).toFloat()
        )
    }

    override fun draw(canvas: Canvas) {
        if (mBgColor != null) {
            if (mBgColor.size == 1) {
                mBgPaint.color = mBgColor[0]
            } else {
                mBgPaint.shader = LinearGradient(
                    mRect!!.left, mRect!!.height() / 2, mRect!!.right,
                    mRect!!.height() / 2, mBgColor, null, Shader.TileMode.CLAMP
                )
            }
        }
        if (mShape == SHAPE_ROUND) {
            canvas.drawRoundRect(
                mRect!!,
                mShapeRadius.toFloat(),
                mShapeRadius.toFloat(),
                mShadowPaint
            )
            canvas.drawRoundRect(mRect!!, mShapeRadius.toFloat(), mShapeRadius.toFloat(), mBgPaint)
            mRect!!.left += mBorderWidth/2
            mRect!!.top += mBorderWidth/2
            mRect!!.right -= mBorderWidth/2
            mRect!!.bottom -= mBorderWidth/2
            canvas.drawRoundRect(
                mRect!!,
                mShapeRadius.toFloat(),
                mShapeRadius.toFloat(),
                mBorderPaint
            )
        } else {
            canvas.drawCircle(
                mRect!!.centerX(),
                mRect!!.centerY(),
                Math.min(mRect!!.width(), mRect!!.height()) / 2,
                mShadowPaint
            )
            canvas.drawCircle(
                mRect!!.centerX(),
                mRect!!.centerY(),
                Math.min(mRect!!.width(), mRect!!.height()) / 2,
                mBgPaint
            )
        }
    }

    override fun setAlpha(alpha: Int) {
        mShadowPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mShadowPaint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    class Builder {
        private var mShape: Int

        private var mShadowRadius: Int
        private var mShapeRadius: Int
        private var mBorderWidth: Float

        private var mBgColor: IntArray
        private var mShadowColor: Int
        private var mBorderColor: Int

        private var mOffsetX: Int
        private var mOffsetY: Int

        fun builder(): ShadowDrawable {
            return ShadowDrawable(
                mShape,
                mBgColor,
                mShadowColor,
                mBorderColor,
                mShapeRadius,
                mShadowRadius,
                mBorderWidth,
                mOffsetX,
                mOffsetY
            )
        }

        fun setShape(mShape: Int): Builder {
            this.mShape = mShape
            return this
        }

        fun setBgColor(BgColor: Int): Builder {
            mBgColor[0] = BgColor
            return this
        }

        fun setBgColor(BgColor: IntArray): Builder {
            mBgColor = BgColor
            return this
        }

        fun setShadowColor(shadowColor: Int): Builder {
            mShadowColor = shadowColor
            return this
        }

        fun setBorderColor(borderColor: Int): Builder {
            mBorderColor = borderColor
            return this
        }

        fun setShapeRadius(ShapeRadius: Int): Builder {
            mShapeRadius = ShapeRadius
            return this
        }

        fun setShadowRadius(shadowRadius: Int): Builder {
            mShadowRadius = shadowRadius
            return this
        }

        fun setBorderWidth(borderWidth: Float): Builder {
            mBorderWidth = borderWidth
            return this
        }

        fun setOffsetX(OffsetX: Int): Builder {
            mOffsetX = OffsetX
            return this
        }

        fun setOffsetY(OffsetY: Int): Builder {
            mOffsetY = OffsetY
            return this
        }

        init {
            mShape = SHAPE_ROUND
            mShapeRadius = 12
            mShadowColor = Color.parseColor("#4d000000")
            mBorderColor = Color.RED
            mBorderWidth = 10F
            mShadowRadius = 18
            mOffsetX = 0
            mOffsetY = 0
            mBgColor = IntArray(1)
            mBgColor[0] = Color.TRANSPARENT
        }
    }
}