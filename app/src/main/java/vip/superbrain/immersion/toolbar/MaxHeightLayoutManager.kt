package vip.superbrain.immersion.toolbar

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * @Description
 *
 * @Author wangleilei
 * @Email wangleilei@mockuai.com
 * @Date 2020-02-17 15:52
 */
class MaxHeightLayoutManager : LinearLayoutManager {
    constructor(context: Context?) : super(context) {}
    constructor(
        context: Context?,
        orientation: Int,
        reverseLayout: Boolean
    ) : super(context, orientation, reverseLayout) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    override fun setMeasuredDimension(
        childrenBounds: Rect?,
        wSpec: Int,
        hSpec: Int
    ) {
        super.setMeasuredDimension(
            childrenBounds, wSpec, View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.getSize(
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ), View.MeasureSpec.AT_MOST
            )
        )
    }
}