package vip.superbrain.immersion.toolbar.avatar

import android.content.Context
import android.util.AttributeSet
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.recycler_item_live_fans_community_avatar.view.*
import vip.superbrain.immersion.toolbar.R

/**
 * @Description 仿微信图像的具体实现
 *
 * @Author wangleilei
 * @Email wangleilei@mockuai.com
 * @Date 2020-02-04 15:57
 */
class WeChatAvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    BaseWeChatAvatarRecyclerView<String>(context, attrs, defStyleAttr) {

    override fun bindHolder(holder: ViewHolder, itemData: String, type: Int) {
        Glide.with(this).load(itemData).centerCrop().into(holder.itemView.ivAvatar)
    }

    override fun itemLayoutId(): Int {
        return R.layout.recycler_item_live_fans_community_avatar
    }
}