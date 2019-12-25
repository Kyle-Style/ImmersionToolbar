package com.netease.nim.uikit.widget.shadow

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import androidx.core.view.ViewCompat

object ShadowDrawableHelper {

    fun setShadowDrawable(
        view: View,
        drawable: Drawable?
    ) {
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        ViewCompat.setBackground(view, drawable)
    }

    fun setShadowDrawable(
        view: View,
        shapeRadius: Int,
        shadowColor: Int,
        shadowRadius: Int,
        offsetX: Int,
        offsetY: Int
    ) {
        val drawable =
            ShadowDrawable.Builder()
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder()
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        ViewCompat.setBackground(view, drawable)
    }

    fun setShadowDrawable(
        view: View,
        bgColor: Int,
        shapeRadius: Int,
        shadowColor: Int,
        shadowRadius: Int,
        offsetX: Int,
        offsetY: Int
    ) {
        val drawable =
            ShadowDrawable.Builder()
                .setBgColor(bgColor)
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder()
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        ViewCompat.setBackground(view, drawable)
    }

    fun setShadowDrawable(
        view: View,
        shape: Int,
        bgColor: Int,
        shapeRadius: Int,
        shadowColor: Int,
        shadowRadius: Int,
        offsetX: Int,
        offsetY: Int
    ) {
        val drawable =
            ShadowDrawable.Builder()
                .setShape(shape)
                .setBgColor(bgColor)
                .setShapeRadius(shapeRadius)
                .setShadowColor(shadowColor)
                .setShadowRadius(shadowRadius)
                .setOffsetX(offsetX)
                .setOffsetY(offsetY)
                .builder()
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        ViewCompat.setBackground(view, drawable)
    }

    fun setShadowDrawable(view: View, shapeRadius: Int, shadowRadius: Int, borderWidth: Float) {
        val drawable =
            ShadowDrawable.Builder()
                .setBgColor(intArrayOf(Color.WHITE))
                .setShapeRadius(view.context.dp2px(shapeRadius.toFloat()))
                .setShadowColor(Color.parseColor("#FFD1B6"))
                .setShadowRadius(view.context.dp2px(shadowRadius.toFloat()))
                .setBorderColor(Color.parseColor("#FFD1B6"))
                .setBorderWidth((view.context.dp2px(borderWidth) / 2).toFloat())
                .setOffsetX(view.context.dp2px(0F))
                .setOffsetY(view.context.dp2px(0F))
                .builder()
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        ViewCompat.setBackground(view, drawable)
    }
}

/**
 * 常用单位转换的辅助类
 */

fun Context.dp2px(dpVal: Float): Int{
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, resources.displayMetrics).toInt()
}

fun Context.sp2px(dpVal: Float): Int{
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dpVal, resources.displayMetrics).toInt()
}

fun Context.px2dp(pxVal: Float): Float{
    return pxVal /resources.displayMetrics.density
}

fun Context.px2sp(pxVal: Float): Float{
    return pxVal /resources.displayMetrics.scaledDensity
}