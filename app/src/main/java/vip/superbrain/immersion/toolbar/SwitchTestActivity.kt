package vip.superbrain.immersion.toolbar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @Description
 *
 * @Author wangleilei
 * @Email wangleilei@mockuai.com
 * @Date 2020-02-20 10:29
 */
class SwitchTestActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun start(context:Context) {
            var intent = Intent(context, SwitchTestActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_main)
    }
}