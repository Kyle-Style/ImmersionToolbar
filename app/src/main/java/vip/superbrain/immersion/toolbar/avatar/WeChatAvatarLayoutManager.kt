package vip.superbrain.immersion.toolbar.avatar

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @Description 微信群组头像布局管理器
 *
 * @Author wangleilei
 * @Email wangleilei@mockuai.com
 * @Date 2020-02-03 15:51
 */
class WeChatAvatarLayoutManager : RecyclerView.LayoutManager() {

    companion object {

        val TAG = WeChatAvatarLayoutManager::class.java.simpleName
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        relayoutChildren(recycler, state)
    }

    // children layout
    private fun relayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        recycler?.run {
            if ((itemCount == 0 || state?.isPreLayout == true)) return
            removeAndRecycleAllViews(this)
            var left = 0
            var top = 0
            //
            val columnCount = getColumnCount(itemCount)
            val rowCount = getRowCount(itemCount, columnCount)
            //
            var column = 0
            var row = 0
            var itemWidthHeight = 0
            for (position in 0 until itemCount) {
                column = getColumn(position, columnCount)
                row = getRow(position, rowCount, columnCount)

                val view = this.getViewForPosition(position)
                addView(view)
                measureChildWithMargins(view, width, height)

                itemWidthHeight = (width - paddingLeft - paddingRight) / columnCount

                view.layoutParams.width = itemWidthHeight
                view.layoutParams.height = itemWidthHeight


                left = itemWidthHeight * column + paddingLeft + getColumnExtra(row, itemCount, columnCount, itemWidthHeight).toInt()
                top = itemWidthHeight * row + (columnCount - rowCount) * itemWidthHeight.toFloat().div(2).toInt() + paddingTop

                layoutDecoratedWithMargins(view, left, top, left + itemWidthHeight, top + itemWidthHeight)
            }
        }
    }

    // 校正列位移
    private fun getColumnExtra(row: Int, size: Int, columnCount: Int, itemWidthHeight: Int): Float {
        return if (row == 0)
            when (val mode = size % columnCount) {
                0 -> {
                    0F
                }
                else -> {
                    itemWidthHeight * (columnCount - mode) / 2F
                }
            } else 0F
    }

    // 获取列
    private fun getColumn(position: Int, columnCount: Int): Int {
        return WeChatAvatarHelper.instance.getColumn(position, columnCount)
    }

    // 获取行
    private fun getRow(position: Int, rowCount:Int, columnCount: Int): Int {
        return WeChatAvatarHelper.instance.getRow(position, rowCount, columnCount)
    }

    // 获取列数
    private fun getColumnCount(size: Int): Int {
        return WeChatAvatarHelper.instance.getColumnCount(size)
    }

    // 获取行数
    private fun getRowCount(size: Int, columnCount: Int): Int {
        return WeChatAvatarHelper.instance.getRowCount(size, columnCount)
    }
}