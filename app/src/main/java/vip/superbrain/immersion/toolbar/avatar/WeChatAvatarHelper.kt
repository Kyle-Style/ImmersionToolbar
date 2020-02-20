package vip.superbrain.immersion.toolbar.avatar


import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.sqrt

/**
 * @Description
 *
 * @Author wangleilei
 * @Email wangleilei@mockuai.com
 * @Date 2020-02-05 18:54
 */
class WeChatAvatarHelper private constructor() {

    companion object {
        val instance: WeChatAvatarHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            WeChatAvatarHelper()
        }

        const val MIN_COLUMN_COUNT = 2
    }

    // 获取列
    fun getColumn(position: Int, columnCount: Int): Int {
        return position % columnCount
    }

    // 获取行
    fun getRow(position: Int, rowCount: Int, columnCount: Int): Int {
        return rowCount - position.toFloat().div(columnCount).plus(1).toInt()
    }

    // 获取列数
    fun getColumnCount(size: Int): Int {
        return getMinColumn(MIN_COLUMN_COUNT, size)
    }

    // 获取行数
    fun getRowCount(size: Int, columnCount: Int): Int {
        return ceil(size.toFloat().div(columnCount)).toInt()
    }

    // 获取当前至少minColumnCount列
    fun getMinColumn(minColumnCount: Int, size: Int): Int {
        return max(ceil(sqrt(size.toDouble())), minColumnCount.toDouble()).toInt()
    }
}