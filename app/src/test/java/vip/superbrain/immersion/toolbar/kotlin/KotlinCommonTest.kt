package vip.superbrain.immersion.toolbar.kotlin

import org.junit.Test

class KotlinCommonTest {


    @Test
    fun functionTest() {
        var testLong = 9999999L
        var text = "${testLong.also {
            if (it?.compareTo(99) == 1) 99 else it
        }}笔${testLong.also {
            if (it?.compareTo(99) == 1) "+" else ""
        }}"
        System.out.println(text)
        var long = 8888L
        System.out.println(long == 8888L)
    }
}