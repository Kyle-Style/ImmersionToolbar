package vip.superbrain.immersion.toolbar.java

import org.junit.Test

import org.junit.Assert.*
import vip.superbrain.immersion.toolbar.avatar.WeChatAvatarHelper
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class WeChatGroupAvatarUnitTest {

    val TAG = WeChatGroupAvatarUnitTest::class.java.simpleName

    @Test
    fun sqrtTest() {
        testSqrtCeil(1)
        testSqrtCeil(2)
        testSqrtCeil(3)
        testSqrtCeil(4)
        testSqrtCeil(5)
        testSqrtCeil(6)
        testSqrtCeil(7)
        testSqrtCeil(8)
        testSqrtCeil(9)
        testSqrtCeil(10)
        testSqrtCeil(11)
        testSqrtCeil(12)
        testSqrtCeil(13)
        testSqrtCeil(14)
    }

    private fun testSqrtCeil(size: Int) {
        var maxColumn = WeChatAvatarHelper.instance.getColumnCount(size)
        System.out.println("maxColumn $maxColumn  size    $size")
    }

    @Test
    fun forTest() {
        testColumnRow(42)
        System.out.println("\n")
        testColumnRow(41)
        System.out.println("\n")
        testColumnRow(40)
        System.out.println("\n")
        testColumnRow(39)
        System.out.println("\n")
        testColumnRow(38)
        System.out.println("\n")
        testColumnRow(37)
        System.out.println("\n")
        testColumnRow(36)
        System.out.println("\n")
        testColumnRow(35)
        System.out.println("\n")
        testColumnRow(34)
        System.out.println("\n")
        testColumnRow(33)
        System.out.println("\n")
        testColumnRow(32)
        System.out.println("\n")
        testColumnRow(31)
        System.out.println("\n")
        testColumnRow(30)
        System.out.println("\n")
        testColumnRow(29)
        System.out.println("\n")
        testColumnRow(28)
        System.out.println("27")
        testColumnRow(26)
        System.out.println("\n")
        testColumnRow(25)
        System.out.println("\n")
        testColumnRow(24)
        System.out.println("\n")
        testColumnRow(23)
        System.out.println("\n")
        testColumnRow(22)
        System.out.println("\n")
        testColumnRow(21)
        System.out.println("\n")
        testColumnRow(20)
        System.out.println("\n")
        testColumnRow(19)
        System.out.println("\n")
        testColumnRow(18)
        System.out.println("\n")
        testColumnRow(17)
        System.out.println("\n")
        testColumnRow(16)
        System.out.println("\n")
        testColumnRow(15)
        System.out.println("\n")
        testColumnRow(14)
        System.out.println("\n")
        testColumnRow(13)
        System.out.println("\n")
        testColumnRow(12)
        System.out.println("\n")
        testColumnRow(11)
        System.out.println("\n")
        testColumnRow(10)
        System.out.println("\n")
        testColumnRow(9)
        System.out.println("\n")
        testColumnRow(8)
        System.out.println("\n")
        testColumnRow(7)
        System.out.println("\n")
        testColumnRow(6)
        System.out.println("\n")
        testColumnRow(5)
        System.out.println("\n")
        testColumnRow(4)
        System.out.println("\n")
        testColumnRow(3)
        System.out.println("\n")
        testColumnRow(2)
        System.out.println("\n")
        testColumnRow(1)
    }

    private fun testColumnRow(size: Int) {
        // 行列数
        val columnCount = WeChatAvatarHelper.instance.getColumnCount(size)
        val rowCount = WeChatAvatarHelper.instance.getRowCount(size, columnCount)
//        System.out.println("size    $size   columnCount    $columnCount row $rowCount")
        var column = 0
        var row = 0
        var savedRow = 0
        for (position in size - 1 downTo  0) {
//            // 列
//            column = position % columnCount
//            // 行
//            row = position.toFloat().div(columnCount).plus(1).toInt() - 1
            // 列
            column = WeChatAvatarHelper.instance.getColumn(position, columnCount)
            // 行
            row = WeChatAvatarHelper.instance.getRow(position, rowCount, columnCount)

            if (savedRow != row) {
                System.out.println()
                savedRow = row
            } else {
            }
            System.out.print("\t${size - 1 - position},$row,$column")
//            System.out.println("position $position  mode $mode   column $column row $row")
        }
    }
}
