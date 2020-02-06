package vip.superbrain.immersion.toolbar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Px
import androidx.appcompat.app.AppCompatActivity
import androidx.customview.widget.ViewDragHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.netease.nim.uikit.widget.shadow.ShadowDrawableHelper
import kotlinx.android.synthetic.main.activity_main.*
import vip.superbrain.immersion.lib.toolbar.StatusBarUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StatusBarUtils.transparencyBar(this)
        StatusBarUtils.setDarkMode(this, true)
        init()
    }

    fun init() {
        btn2RedEnvelopes.setOnClickListener {
            LiveRedEnvelopesActivity.startLiveRedEnvelopesActivity(this)
        }
        btn2Progress.setOnClickListener {
            ProgressActivity.startProgressActivity(this)
        }
        shytContent.setListener(object : SlideHorizontalLayout.OnSlideListener {

            override fun onOpen(slideDelete: SlideHorizontalLayout?) {
                shadowTop.requestLayout()
                shadowTop.invalidate()
            }

            override fun onClose(slideDelete: SlideHorizontalLayout?) {
                shadowTop.requestLayout()
                shadowTop.invalidate()
            }

            override fun onClick(slideDelete: SlideHorizontalLayout?) {
                Toast.makeText(baseContext, "单机我了呀", Toast.LENGTH_SHORT).show()
            }
        })
    }
}