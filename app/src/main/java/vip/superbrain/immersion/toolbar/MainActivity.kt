package vip.superbrain.immersion.toolbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import vip.superbrain.immersion.lib.toolbar.StatusBarUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StatusBarUtils.transparencyBar(this)
        StatusBarUtils.setDarkMode(this, true)
    }
}