package vip.superbrain.immersion.toolbar.widget

import android.os.Handler

/**
 * @Description
 *
 * @Author wangleilei
 * @Email wangleilei@mockuai.com
 * @Date 2020-01-11 09:37
 */
class DelayedPeriodTaskHelper(
    var delaySecond: Long,
    var periodSecond: Long,
    var listener: OnRunListener? = null
) : Runnable {

    private var handler: Handler? = Handler()
    private var isRunning = false
    private var periodCRunCount = 0

    override fun run() {
        if (!isRunning) {
            return
        }
        // 执任务
        periodCRunCount++
        listener?.onNext(periodCRunCount)
        // 下个周期任务
        handler?.postDelayed(this, periodSecond * 1000)
    }

    // 默认延迟启动
    fun start() {
        startDelayed(delaySecond * 1000)
    }

    // 延迟启动
    fun startDelayed(delaySecond: Long) {
        if (isRunning) {
            return
        }
        isRunning = true
        listener?.onStart()
        handler?.postDelayed(this, delaySecond)
    }

    fun reset() {
        periodCRunCount = 0
        cancel()
    }

    fun cancel() {
        listener?.onCancel()
        isRunning = false
        handler?.removeCallbacks(this)
    }

    fun release() {
        reset()
        handler = null
    }

    fun running(): Boolean {
        return isRunning
    }


    interface OnRunListener {

        fun onStart()

        fun onNext(periodCRunCount: Int)

        fun onComplete()

        fun onCancel()
    }
}