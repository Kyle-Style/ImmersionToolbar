package vip.superbrain.immersion.toolbar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_radiogroup.*
import vip.superbrain.immersion.lib.toolbar.StatusBarUtils
import vip.superbrain.immersion.toolbar.widget.DelayedPeriodTaskHelper

class LiveRedEnvelopesActivity : AppCompatActivity() {

    companion object {

        @JvmStatic
        fun startLiveRedEnvelopesActivity(context: Context) {
            val intent = Intent(context, LiveRedEnvelopesActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radiogroup)
        StatusBarUtils.transparencyBar(this)
        StatusBarUtils.setDarkMode(this, true)

        tvAmount5.setOnCheckedChangeListener { buttonView, isChecked ->
            rgContent.check(buttonView.id)
        }
        tvAmount20.setOnCheckedChangeListener { buttonView, isChecked ->
            rgContent.check(buttonView.id)
        }
        tvAmount50.setOnCheckedChangeListener { buttonView, isChecked ->
            rgContent.check(buttonView.id)
        }
        tvAmount100.setOnCheckedChangeListener { buttonView, isChecked ->
            rgContent.check(buttonView.id)
        }
    }

    fun countdownTest() {

    }
}