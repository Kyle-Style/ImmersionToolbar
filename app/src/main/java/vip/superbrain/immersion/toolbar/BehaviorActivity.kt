package vip.superbrain.immersion.toolbar

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_layout_behavior.*
import kotlinx.android.synthetic.main.item_test.view.*


/**
 * @Description
 *
 * @Author wangleilei
 * @Email wangleilei@mockuai.com
 * @Date 2020-02-17 15:54
 */
class BehaviorActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, BehaviorActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout_behavior)
        if (Build.VERSION.SDK_INT > 9) {
            val policy =
                StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        initData()
        initView()
    }

    var dynamicData = arrayListOf<String>(
        "https://cn.bing.com/th?id=OIP.a13xv-Dkd4pE26B1PNFpyAHaGl&pid=Api&rs=1",
        "https://cn.bing.com/th?id=OIP.JmNR5WCb8D_iZVlCsj4OIAHaEo&pid=Api&w=1920&h=1200&rs=1",
        "http://www.bbra.cn/Uploadfiles/imgs/2015/07/08/ziran2/Xbzs_005.jpg",
        "https://cn.bing.com/th?id=OIP.-UL1ALZqQLbVMNynhlNV2QHaE8&pid=Api&w=1200&h=800&rs=1",
        "https://cn.bing.com/th?id=OIP.YSPGZb8dybUJmXwh2AgBSQHaEo&pid=Api&w=1920&h=1200&rs=1",
        "http://file06.16sucai.com/2018/0422/0b6bcaa9649065b0aea648387710d75f.jpg",
        "https://cn.bing.com/th?id=OIP.fqRHs5D89sHukhMt7zaQwwHaEK&pid=Api&w=1920&h=1080&rs=1",
        "https://sjbz-fd.zol-img.com.cn/t_s320x510c/g5/M00/00/02/ChMkJlfJVQGIRs8VAA97CYxAeKkAAU91AEX9iYAD3sh497.jpg",
        "http://pic.sc.chinaz.com/files/pic/pic9/201512/apic17358.jpg",
        "http://up.enterdesk.com/edpic/20/25/12/202512d18e735e4ff5b0f115b6b0822d.jpg",
        "http://up.enterdesk.com/edpic_360_360/ed/71/81/ed7181eadac4adf22b2634e7e1a2c3eb.jpg",
        "http://pics.sc.chinaz.com/files/pic/pic9/201801/zzpic9374.jpg",
        "http://pica.nipic.com/2008-03-07/200837101931479_2.jpg",
        "https://cn.bing.com/th?id=OIP.DmDIg-lelFyfugKLiy_0hwHaKE&pid=Api&rs=1",
        "https://cn.bing.com/th?id=OIP.a13xv-Dkd4pE26B1PNFpyAHaGl&pid=Api&rs=1",
        "https://cn.bing.com/th?id=OIP.JmNR5WCb8D_iZVlCsj4OIAHaEo&pid=Api&w=1920&h=1200&rs=1",
        "http://www.bbra.cn/Uploadfiles/imgs/2015/07/08/ziran2/Xbzs_005.jpg",
        "https://cn.bing.com/th?id=OIP.-UL1ALZqQLbVMNynhlNV2QHaE8&pid=Api&w=1200&h=800&rs=1",
        "https://cn.bing.com/th?id=OIP.YSPGZb8dybUJmXwh2AgBSQHaEo&pid=Api&w=1920&h=1200&rs=1",
        "http://file06.16sucai.com/2018/0422/0b6bcaa9649065b0aea648387710d75f.jpg",
        "https://cn.bing.com/th?id=OIP.fqRHs5D89sHukhMt7zaQwwHaEK&pid=Api&w=1920&h=1080&rs=1",
        "https://sjbz-fd.zol-img.com.cn/t_s320x510c/g5/M00/00/02/ChMkJlfJVQGIRs8VAA97CYxAeKkAAU91AEX9iYAD3sh497.jpg",
        "http://pic.sc.chinaz.com/files/pic/pic9/201512/apic17358.jpg",
        "http://up.enterdesk.com/edpic/20/25/12/202512d18e735e4ff5b0f115b6b0822d.jpg",
        "http://up.enterdesk.com/edpic_360_360/ed/71/81/ed7181eadac4adf22b2634e7e1a2c3eb.jpg",
        "http://pics.sc.chinaz.com/files/pic/pic9/201801/zzpic9374.jpg",
        "http://pica.nipic.com/2008-03-07/200837101931479_2.jpg",
        "https://cn.bing.com/th?id=OIP.DmDIg-lelFyfugKLiy_0hwHaKE&pid=Api&rs=1",
        "https://cn.bing.com/th?id=OIP.a13xv-Dkd4pE26B1PNFpyAHaGl&pid=Api&rs=1",
        "https://cn.bing.com/th?id=OIP.JmNR5WCb8D_iZVlCsj4OIAHaEo&pid=Api&w=1920&h=1200&rs=1",
        "http://www.bbra.cn/Uploadfiles/imgs/2015/07/08/ziran2/Xbzs_005.jpg",
        "https://cn.bing.com/th?id=OIP.-UL1ALZqQLbVMNynhlNV2QHaE8&pid=Api&w=1200&h=800&rs=1",
        "https://cn.bing.com/th?id=OIP.YSPGZb8dybUJmXwh2AgBSQHaEo&pid=Api&w=1920&h=1200&rs=1",
        "http://file06.16sucai.com/2018/0422/0b6bcaa9649065b0aea648387710d75f.jpg",
        "https://cn.bing.com/th?id=OIP.fqRHs5D89sHukhMt7zaQwwHaEK&pid=Api&w=1920&h=1080&rs=1",
        "https://sjbz-fd.zol-img.com.cn/t_s320x510c/g5/M00/00/02/ChMkJlfJVQGIRs8VAA97CYxAeKkAAU91AEX9iYAD3sh497.jpg",
        "http://pic.sc.chinaz.com/files/pic/pic9/201512/apic17358.jpg",
        "http://up.enterdesk.com/edpic/20/25/12/202512d18e735e4ff5b0f115b6b0822d.jpg",
        "http://up.enterdesk.com/edpic_360_360/ed/71/81/ed7181eadac4adf22b2634e7e1a2c3eb.jpg",
        "http://pics.sc.chinaz.com/files/pic/pic9/201801/zzpic9374.jpg",
        "http://pica.nipic.com/2008-03-07/200837101931479_2.jpg",
        "https://cn.bing.com/th?id=OIP.DmDIg-lelFyfugKLiy_0hwHaKE&pid=Api&rs=1",
        "https://cn.bing.com/th?id=OIP.ImKQV_GGvhM1eiws6vQ9dwHaE5&pid=Api&w=1587&h=1050&rs=1"
    )

    var data = arrayListOf<String>()

    fun initView() {
        recyclerView.layoutManager = MaxHeightLayoutManager(this)
        recyclerView.adapter = Adapter()
    }

    fun initData() {
        for (position in 0 until dynamicData.size) {
            data.add(position.toString())
        }
    }

    inner class Adapter : RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_test,
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.tvCommunityName.text = (position + 1).toString()
            holder.itemView.wcaVAvatar.data = getData(position)
        }

        fun getData(position: Int): ArrayList<String> {
            var dynamicData22 = arrayListOf<String>()
            for (position in 0..position) {
                dynamicData22.add(dynamicData[position])
            }
            return dynamicData22
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}