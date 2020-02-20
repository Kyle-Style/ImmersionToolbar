package vip.superbrain.immersion.toolbar.avatar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @Description 微信群组头像布局基类控件， 子类设置布局、数据以及对ViewHolder的操作
 *
 * @Author wangleilei
 * @Email wangleilei@mockuai.com
 * @Date 2020-02-03 18:09
 */
abstract class BaseWeChatAvatarRecyclerView<T>(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : RecyclerView(context, attrs, defStyleAttr) {

    companion object {

        const val CORNER_TYPE_COMMON = 0
        const val CORNER_TYPE_TOP_LEFT = 1
        const val CORNER_TYPE_TOP_RIGHT = 2
        const val CORNER_TYPE_BOTTOM_LEFT = 3
        const val CORNER_TYPE_BOTTOM_RIGHT = 4
    }

    var data: ArrayList<T> = arrayListOf()
        set(value) {
            field?.clear()
            adapter?.notifyDataSetChanged()
            field = value
            if (adapter is BaseWeChatAvatarRecyclerView<*>.Adapter) {
                (adapter as BaseWeChatAvatarRecyclerView<*>.Adapter).apply {
                    columnCount = WeChatAvatarHelper.instance.getColumnCount(field?.size)
                    rowCount = WeChatAvatarHelper.instance.getRowCount(field?.size, columnCount)
                }
            }
            adapter?.notifyDataSetChanged()
        }

    init {
        layoutManager = WeChatAvatarLayoutManager()
        adapter = Adapter()
    }

    abstract fun bindHolder(holder: ViewHolder, itemData: T, type: Int)

    abstract fun itemLayoutId(): Int

    inner class Adapter : RecyclerView.Adapter<ViewHolder>() {

        var columnCount = 0

        var rowCount = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(itemLayoutId(), parent, false))
        }

        override fun getItemCount(): Int {
            return data?.size
        }

        private fun getColumn(position: Int): Int {
            return WeChatAvatarHelper.instance.getColumn(position, columnCount)
        }

        private fun getRow(position: Int): Int {
            return WeChatAvatarHelper.instance.getRow(position, rowCount, columnCount)
        }

        fun getType(position: Int): Int {
            val row = getRow(position)
            val column = getColumn(position)
            if (rowCount == columnCount && columnCount > 1) {
                if(row + 1 == rowCount){
                    if (column == 0) {
                        return CORNER_TYPE_BOTTOM_LEFT
                    } else if(column + 1 == columnCount) {
                        return CORNER_TYPE_BOTTOM_RIGHT
                    }
                } else if(row == 0 && itemCount == rowCount * columnCount){
                    if (column == 0) {
                        return CORNER_TYPE_TOP_LEFT
                    } else if(column + 1 == columnCount) {
                        return CORNER_TYPE_TOP_RIGHT
                    }
                }
            }
            return CORNER_TYPE_COMMON
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            bindHolder(holder, data?.get(position), getType(position))
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}