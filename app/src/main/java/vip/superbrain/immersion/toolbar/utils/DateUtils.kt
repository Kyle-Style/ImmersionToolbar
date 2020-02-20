package vip.superbrain.immersion.toolbar.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * @Description 时间转换工具
 *
 * @Author wangleilei
 * @Email wangleilei@mockuai.com
 * @Date 2020-02-18 09:56
 */
object DateUtils {

    const val TODAY = "今天"
    const val TOMORROW = "明天"

    const val DIFF_TODAY = 0
    const val DIFF_TOMORROW = 1
    const val DIFF_OTHERS = -1000000

    /**
     * 2019年1月17日 0点10分
     * 1月17日 0点10分
     * 1月18日 0点10分
     * 1月19日 0点10分
     * 1月20日 0点10分
     * 2021年1月20日 0点10分
     * */
    open fun showTodayTomorrowOrDateTimeWithOutSecond(time: String): String? {
        return showTodayTomorrowOrDateTimeWithOutSecond(time, "年", "月", "日", "点", "分")
    }

    open fun showTodayTomorrowOrDateTimeWithOutSecond(
        time: String,
        yearUnit: String?,
        monthUnit: String?,
        dayUnit: String?,
        hourUnit: String?,
        minuteUnit: String?
    ): String? {
        val pre = Calendar.getInstance()
        val predate = Date(System.currentTimeMillis())
        pre.time = predate
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date: Date? = sdf.parse(time)
        cal.time = date
        if (cal[Calendar.YEAR] === pre[Calendar.YEAR]) {
            val diffDay = (cal[Calendar.DAY_OF_YEAR]
                    - pre[Calendar.DAY_OF_YEAR])
            return showTodayTomorrowOrDateTime(
                sdf,
                DIFF_OTHERS,
                time,
                false,
                yearUnit,
                monthUnit,
                dayUnit,
                hourUnit,
                minuteUnit,
                null
            )
        } else {
            return showTodayTomorrowOrDateTime(
                sdf,
                DIFF_OTHERS,
                time,
                true,
                yearUnit,
                monthUnit,
                dayUnit,
                hourUnit,
                minuteUnit,
                null
            )
        }
        return time
    }

    private fun showTodayTomorrowOrDateTime(
        sdf: SimpleDateFormat,
        diffDay: Int,
        dataTimeString: String,
        yearShow: Boolean,
        yearUnit: String?,
        monthUnit: String?,
        dayUnit: String?,
        hourUnit: String?,
        minuteUnit: String?,
        secondUnit: String?
    ): String? {
        return when (diffDay) {
            DIFF_TODAY ->
                TODAY + getFormatDateString(
                    sdf,
                    dataTimeString,
                    yearShow,
                    yearUnit,
                    monthUnit,
                    dayUnit,
                    hourUnit,
                    minuteUnit,
                    secondUnit,
                    false,
                    true,
                    false
                )
            DIFF_TOMORROW ->
                TOMORROW + getFormatDateString(
                    sdf,
                    dataTimeString,
                    yearShow,
                    yearUnit,
                    monthUnit,
                    dayUnit,
                    hourUnit,
                    minuteUnit,
                    secondUnit,
                    false,
                    true,
                    false
                )
            else -> getFormatDateString(
                sdf,
                dataTimeString,
                yearShow,
                yearUnit,
                monthUnit,
                dayUnit,
                hourUnit,
                minuteUnit,
                secondUnit,
                true,
                true,
                false
            )
        }
    }

    // 获取格式化的时间
    fun getFormatDateString(
        sdf: SimpleDateFormat,
        dataTimeString: String,
        yearShow: Boolean,
        yearUnit: String? = null,
        monthUnit: String? = null,
        dayUnit: String? = null,
        hourUnit: String? = null,
        minuteUnit: String? = null,
        secondUnit: String? = null,
        showDate: Boolean? = true,
        showTime: Boolean? = true,
        showSecond: Boolean? = true
    ): String {
        val currentDate = sdf.parse(dataTimeString)
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        val sb = StringBuilder()
        if (showDate == true) {
            if (yearShow) {
                sb.append("${calendar.get(Calendar.YEAR)}${yearUnit ?: '-'}")
            } else {
            }
            sb.append("${calendar.get(Calendar.MONTH) + 1}${monthUnit ?: '-'}")
            sb.append("${calendar.get(Calendar.DAY_OF_MONTH)}${dayUnit ?: ""}")
        } else {
        }
        if (showTime == true) {
            sb.append(" ")
            sb.append("${calendar.get(Calendar.HOUR_OF_DAY)}${hourUnit ?: ':'}")
            sb.append("${calendar.get(Calendar.MINUTE)}${minuteUnit ?: ':'}")
            if (showSecond == true) {
                sb.append("${calendar.get(Calendar.SECOND)}${secondUnit ?: ""}")
            } else {
            }
        } else {
        }
        return sb.toString()
    }
}