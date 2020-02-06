package vip.superbrain.immersion.toolbar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_progress.*
import kotlinx.android.synthetic.main.layout_count_down.*
import vip.superbrain.immersion.lib.toolbar.StatusBarUtils
import vip.superbrain.immersion.toolbar.widget.DelayedPeriodTaskHelper
import java.util.*


class ProgressActivity : AppCompatActivity() {

    private val cal: Calendar = Calendar.getInstance()

    companion object {

        val TAG = ProgressActivity::class.java.simpleName

        @JvmStatic
        fun startProgressActivity(context: Context) {
            val intent = Intent(context, ProgressActivity::class.java)
            context.startActivity(intent)
        }
    }

    var delayedPeriodTaskHelper: DelayedPeriodTaskHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)
        StatusBarUtils.transparencyBar(this)
        StatusBarUtils.setDarkMode(this, true)
//        vsCountDown.inflate()

        cal.timeInMillis = System.currentTimeMillis()

        val delayedTime = 1L
        val periodTime = 1L
        delayedPeriodTaskHelper = DelayedPeriodTaskHelper(delayedTime, periodTime, object :
            DelayedPeriodTaskHelper.OnRunListener {

            override fun onStart() {
                Log.e(TAG, "onStart")
            }

            override fun onNext(periodCRunCount: Int) {
                val progress = (delayedTime + periodTime.times(periodCRunCount)).toInt()
//                Log.e(TAG, progress.toString())
                cpbTop.progress = progress
//                cpbTop.startAnimation(progress.toFloat(), progress.toFloat().plus(20))
//                cpbProgress.progress = progress
                tvAmount?.text = progress.toString()
                tvRestTime?.text

            }

            override fun onComplete() {
                Log.e(TAG, "onComplete")
            }

            override fun onCancel() {
                Log.e(TAG, "onCancel")
            }
        })
        initListener()
    }

    private fun initListener() {
        val content = arrayListOf("5分钟后", "开抢")
        val view = arrayListOf("查看")
        btnStart.setOnClickListener {
//            delayedPeriodTaskHelper?.start()
            cpbTop.progress -= 20
            cpbTop.startAnimation()
        }
        btnStop.setOnClickListener {
            delayedPeriodTaskHelper?.cancel()
        }
        btnReset.setOnClickListener {
            delayedPeriodTaskHelper?.reset()
        }
        sbTop.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                cpbTop.progress = progress
//                cpbProgress.progress = progress
                if (progress == 0) {
                    cpbTop.setContent(view)
//                    cpbProgress.setContent(view)
                } else {
                    if (progress > 60) {
                        val text = if (progress % 60 == 0) progress / 60 else progress / 60 + 1
                        content[0] = "${text}分后"
                    } else {
                        content[0] = "${progress}秒后"
                    }
                    cpbTop.setContent(content)
//                    cpbProgress.setContent(content)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        cpbTop.max = 120
        cpbTop.progress = 120
//        cpbTop.progress = 0
//        cpbProgress.progress = 0
        cpbTop.setContent(content)
//        cpbProgress.setContent(content)
    }

    private fun testDataTime() {

        cal.get(Calendar.YEAR)
    }
}