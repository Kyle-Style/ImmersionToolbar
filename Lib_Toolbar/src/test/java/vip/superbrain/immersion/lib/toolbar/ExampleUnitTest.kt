package vip.superbrain.immersion.lib.toolbar

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun getStringTest() {
        matchPattern("0.00")
        matchPattern("0.0")
        matchPattern("0")
        matchPattern("0.010")
        matchPattern("0.01")
        matchPattern("0.1")
        matchPattern("9999.9")
        matchPattern("9999.99")
        matchPattern("10000")
        matchPattern("10000.0")
        matchPattern("10000.00")
        matchPattern("10000.01")
    }

    private fun matchPattern(input: String): Boolean {
        val regExp =
            "^(((0\\.\\d?[1-9]|\\+?[1-9][0-9]{0,3})(\\.\\d{1,2})?)|10000|10000.0|10000.00)$"
        val p: Pattern = Pattern.compile(regExp)
        val m: Matcher = p.matcher(input)
        System.out.println("\tmatch\t" + m.matches() + "\tinput\t" + input)
        return m.matches()
    }
}
