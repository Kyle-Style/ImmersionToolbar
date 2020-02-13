package vip.superbrain.immersion.toolbar

import android.text.Spannable
import android.util.Log
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.EmojiSpan
import com.vdurmont.emoji.EmojiParser
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.experimental.and


/**
 * <pre>
 * 本类的主要功能是将带有emoji的字符串，格式化成unicode字符串，并且提供可见unicode字符反解成emoji字符
 *
 *
 * 相关识知点：
 * <b>
 * Unicode平面，
 * BMP的字符可以使用charAt(index)来处理,计数可以使用length()
 * 其它平面字符，需要用codePointAt(index),计数可以使用codePointCount(0,str.lenght())</b>
 *
 * Unicode可以逻辑分为17平面（Plane），每个平面拥有65536（ = 216）个代码点，虽然目前只有少数平面被使
 * 用。
 * 平面0 (0000–FFFF): 基本多文种平面（Basic Multilingual Plane, BMP）.
 * 平面1 (10000–1FFFF): 多文种补充平面（Supplementary Multilingual Plane, SMP）.
 * 平面2 (20000–2FFFF): 表意文字补充平面（Supplementary Ideographic Plane, SIP）.
 * 平面3 (30000–3FFFF): 表意文字第三平面（Tertiary Ideographic Plane, TIP）.
 * 平面4 to 13 (40000–DFFFF)尚未使用
 * 平面14 (E0000–EFFFF): 特别用途补充平面（Supplementary Special-purpose Plane, SSP）
 * 平面15 (F0000–FFFFF)保留作为私人使用区（Private Use Area, PUA）
 * 平面16 (100000–10FFFF)，保留作为私人使用区（Private Use Area, PUA）
 *
 * 参考：
 * 维基百科: http://en.wikipedia.org/wiki/Emoji
 * GITHUB: http://punchdrunker.github.io/iOSEmoji/
 * 杂项象形符号:1F300-1F5FF
 * 表情符号：1F600-1F64F
 * 交通和地图符号:1F680-1F6FF
 * 杂项符号：2600-26FF
 * 符号字体:2700-27BF
 * 国旗：1F100-1F1FF
 * 箭头：2B00-2BFF 2900-297F
 * 各种技术符号：2300-23FF
 * 字母符号: 2100–214F
 * 中文符号： 303D 3200–32FF 2049 203C
 *  Private Use Area:E000-F8FF;
 *  High Surrogates D800..DB7F;
 *  High Private Use Surrogates  DB80..DBFF
 *  Low Surrogates DC00..DFFF  D800-DFFF E000-F8FF
 *  标点符号：2000-200F 2028-202F 205F 2065-206F
 *  变异选择器：IOS独有 FE00-FE0F
 * </pre>
 */
object EmojiUtils {

    fun isEmojiCharacter(codePoint: Int): Boolean {
        return (codePoint >= 0x2600 && codePoint <= 0x27BF // 杂项符号与符号字体
                || codePoint == 0x303D || codePoint == 0x2049 || codePoint == 0x203C || codePoint >= 0x2000 && codePoint <= 0x200F //
                || codePoint >= 0x2028 && codePoint <= 0x202F //
                || codePoint == 0x205F //
                || codePoint >= 0x2065 && codePoint <= 0x206F //
                /* 标点符号占用区域 */
                || codePoint >= 0x2100 && codePoint <= 0x214F // 字母符号
                || codePoint >= 0x2300 && codePoint <= 0x23FF // 各种技术符号
                || codePoint >= 0x2B00 && codePoint <= 0x2BFF // 箭头A
                || codePoint >= 0x2900 && codePoint <= 0x297F // 箭头B
                || codePoint >= 0x3200 && codePoint <= 0x32FF // 中文符号
                || codePoint >= 0xD800 && codePoint <= 0xDFFF // 高低位替代符保留区域
                || codePoint >= 0xE000 && codePoint <= 0xF8FF // 私有保留区域
                || codePoint >= 0xFE00 && codePoint <= 0xFE0F // 变异选择器
                || codePoint >= 0x10000) // Plane在第二平面以上的，char都不可以存，全部都转
    }

    public fun getStringRealCount(src: String): Int {
        if (src.isNullOrEmpty()) {
            return 0
        }
        var cpCount = src.codePointCount(0, src.length)
        var firCodeIndex = src.offsetByCodePoints(0, 0)
        var lstCodeIndex = src.offsetByCodePoints(0, cpCount - 1)
        var position = 0
        for (index in firCodeIndex..lstCodeIndex) {
            if (index < position) {
                continue
            }
            var codepoint = src.codePointAt(position)
            if (isEmojiCharacter(codepoint)) {
//                Log.e("RealCount", "$codepoint 是emoji")
            } else {
                Log.e("RealCount", "不是emoji")
            }
            position += if (Character.isSupplementaryCodePoint(codepoint)) 2 else 1
        }
        return position
    }

    val PATTERN =
        Pattern.compile("[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\ud83e\\udc00-\\ud83e\\udfff]|[\\ud83f\\udc00-\\ud83f\\udfff]|[\\u2600-\\u27ff]|[\\u200D]|[\\u0020]|[\\u2642]|[\\u2640]|[\\uFE0F]|[\\u2300-\\u23FF]|[\\udb40\\udc00-\\udb40\\uddff]")

    fun getResult(src: CharSequence): CharSequence {
        return PATTERN.matcher(src).replaceAll("")
    }

    fun getEmojiCount222(charSequence: CharSequence): Int {
        var count = 0
        val processed = EmojiCompat.get().process(
            charSequence,
            0,
            charSequence.length - 1,
            Int.MAX_VALUE,
            EmojiCompat.REPLACE_STRATEGY_ALL
        )
        if (processed is Spannable) {
            val spannable = processed
            count = spannable.getSpans(0, spannable.length - 1, EmojiSpan::class.java).size
        }

        return EmojiParser.extractEmojis(charSequence.toString()).size
    }

    // 获取emoji的个数
    fun getEmojiCount(charSequence: CharSequence): Int {
        if (charSequence.isEmpty()) {
            return 0
        }
        var count = 0
        val processed = EmojiCompat.get().process(charSequence, 0, charSequence.length - 1, Int.MAX_VALUE, EmojiCompat.REPLACE_STRATEGY_ALL)
        if (processed is Spannable) {
            var spans = processed.getSpans(0, processed.length - 1, EmojiSpan::class.java)
            count = spans?.size ?: 0
        }
        return count
    }

    // 获取emoji的个数
    fun getCharCount(charSequence: CharSequence): Int {
        if (charSequence.isEmpty()) {
            return 0
        }
        var count = 0
        val processed = EmojiCompat.get().process(charSequence, 0, charSequence.length - 1, Int.MAX_VALUE, EmojiCompat.REPLACE_STRATEGY_ALL)
        Log.e("getCharCount", processed.length.toString())
        Log.e("getCharCount", processed.toList().size.toString())
        Log.e("getCharCount", processed.count().toString())
        Log.e("getCharCount", processed.count().toString())
        var content = charSequence.toString()
        if (processed is Spannable) {
            var spans = processed.getSpans(0, processed.length - 1, EmojiSpan::class.java)
            spans.forEach {
                content.replace(it.toString(), "")
            }
            count = spans?.size ?: 0
        }
        Log.e("count", count.toString())
        Log.e("content", content)
        return count
    }

    // 获取emoji的个数
    fun getCharCount2(charSequence: CharSequence): Int {
        if (charSequence.isEmpty()) {
            return 0
        }
//        var content = charSequence.toString()
//        val bs: ByteArray = content.toByteArray()
//        val buf: ByteBuffer = ByteBuffer.wrap(bs)
//        val charset: Charset = Charset.forName("UTF-32")
//        val cBuf: CharBuffer = charset.decode(buf)
//        var count= 0
//        cBuf.toString().map {
//            Log.e(count++.toString(), it.toString())
//        }
        return EmojiReader.getTextLength(charSequence)
    }


    fun byteToUnsignedInt(data: Byte): Int {
        return (data and 0xff.toByte()).toInt()
    }

    fun isUTF8(pBuffer: ByteArray): Boolean {
        var IsUTF8 = true
        var IsASCII = true
        val size = pBuffer.size
        var i = 0
        while (i < size) {
            val value = byteToUnsignedInt(pBuffer[i])
            if (value < 0x80) { // (10000000): 值小于 0x80 的为 ASCII 字符
                if (i >= size - 1) {
                    if (IsASCII) { // 假设纯 ASCII 字符不是 UTF 格式
                        IsUTF8 = false
                    }
                    break
                }
                i++
            } else if (value < 0xC0) { // (11000000): 值介于 0x80 与 0xC0 之间的为无效 UTF-8 字符
                IsASCII = false
                IsUTF8 = false
                break
            } else if (value < 0xE0) { // (11100000): 此范围内为 2 字节 UTF-8 字符
                IsASCII = false
                if (i >= size - 1) {
                    break
                }
                val value1 = byteToUnsignedInt(pBuffer[i + 1])
                if (value1 and 0xC0 != 0x80) {
                    IsUTF8 = false
                    break
                }
                i += 2
            } else if (value < 0xF0) {
                IsASCII = false
                // (11110000): 此范围内为 3 字节 UTF-8 字符
                if (i >= size - 2) {
                    break
                }
                val value1 = byteToUnsignedInt(pBuffer[i + 1])
                val value2 = byteToUnsignedInt(pBuffer[i + 2])
                if (value1 and 0xC0 != 0x80 || value2 and 0xC0 != 0x80) {
                    IsUTF8 = false
                    break
                }
                i += 3
            } else if (value < 0xF8) {
                IsASCII = false
                // (11111000): 此范围内为 4 字节 UTF-8 字符
                if (i >= size - 3) {
                    break
                }
                val value1 = byteToUnsignedInt(pBuffer[i + 1])
                val value2 = byteToUnsignedInt(pBuffer[i + 2])
                val value3 = byteToUnsignedInt(pBuffer[i + 3])
                if (value1 and 0xC0 != 0x80 || value2 and 0xC0 != 0x80 || value3 and 0xC0 != 0x80
                ) {
                    IsUTF8 = false
                    break
                }
                i += 3
            } else {
                IsUTF8 = false
                IsASCII = false
                break
            }
        }
        return IsUTF8
    }

    fun charCount(pBuffer: ByteArray): Int {
        val size = pBuffer.size
        Log.e("size", size.toString())
        var i = 0
        var count = 0
        while (i < size) {

            val value = byteToUnsignedInt(pBuffer[i])
            Log.e("Buffer2", pBuffer[i].toString(2))
            if (value < 0x80) { // (10000000): 值小于 0x80 的为 ASCII 字符
                i++
                count++
//                Log.e("value", "0x80")
            } else if (value < 0xC0) { // (11000000): 值介于 0x80 与 0xC0 之间的为无效 UTF-8 字符
//                i++
//                count++
//                Log.e("value", "0xC0")
                break
            } else if (value < 0xE0) { // (11100000): 此范围内为 2 字节 UTF-8 字符
                i += 2
                count++
//                Log.e("value", "0xE0")
            } else if (value < 0xF0) {
                i += 3
                count++
//                Log.e("value", "0xF0")
            } else if (value < 0xF8) {
                i += 3
                count++
//                Log.e("value", "0xF8")
            } else {
//                i++
//                count++
//                Log.e("value", "else")
                break
            }
        }
        return count
    }

    // 获取emoji的个数
    fun getCharCount3(charSequence: CharSequence): Int {
        if (charSequence.isEmpty()) {
            return 0
        }
        var content = charSequence.toString()
        val bs: ByteArray = content.toByteArray()
//        val buf: ByteBuffer = ByteBuffer.wrap(bs)
//        val charset: Charset = Charset.forName("UTF-32")
//        val cBuf: CharBuffer = charset.decode(buf)
//        var count= 0
//        cBuf.toString().map {
//            Log.e(count++.toString(), it.toString())
//        }
        return charCount(bs)
    }

    fun codePoint(charSequence: CharSequence): Int {
        if (charSequence.isEmpty()) {
            return 0
        }
        var content = charSequence.toString()
        var cpCount = content.codePointCount(0, content.length - 1)
        for (position in 0 until cpCount) {
            //这里的offset是字符的位置
            var offset = content.offsetByCodePoints(0, position)
            var codePoint = content.codePointAt(offset)
            var charCount = Character.charCount(codePoint)
            System.out.println("content $content offset $offset position $position codePoint ${codePoint.toString(2)} msg ${content.subSequence(offset, offset + charCount)}")
        }
        var count = 0
        return count
    }


    /**
     * 含有unicode 的字符串转一般字符串
     * @param unicodeStr 混有 Unicode 的字符串
     * @return
     */
    fun unicodeCount(unicodeStr: String): Int {
        val length = unicodeStr.length
        var count = 0
        //正则匹配条件，可匹配“\\u”1到4位，一般是4位可直接使用 String regex = "\\\\u[a-f0-9A-F]{4}";
        val regex = "\\\\u[a-f0-9A-F]{1,4}"
        val pattern = Pattern.compile(regex)
        val matcher: Matcher = pattern.matcher(unicodeStr)
        var position = 0
        while (matcher.find()) {
            val oldChar: String = matcher.group() //原本的Unicode字符
            // int index = unicodeStr.indexOf(oldChar);
// 在遇见重复出现的unicode代码的时候会造成从源字符串获取非unicode编码字符的时候截取索引越界等
            val index: Int = matcher.start()
            count = index + oldChar.length //统计下标移动的位置
            position++
        }
        println("position   $position")
        return position
    }


    fun unicode(source: String): String? {
        val sb = StringBuffer()
        val source_char = source.toCharArray()
        var unicode: String? = null
        for (i in source_char.indices) {
            unicode = Integer.toHexString(source_char[i].toInt())
            if (unicode.length <= 2) {
                unicode = "00$unicode"
            }
            sb.append("\\u$unicode")
        }
        println(sb)
        return sb.toString()
    }

    fun decodeUnicode(unicode: String): String? {
        val sb = StringBuffer()
        val hex = unicode.split("\\\\u").toTypedArray()
        for (i in 1 until hex.size) {
            val data = hex[i].toInt(16)
            sb.append(data.toChar())
        }
        return sb.toString()
    }

    fun decodeUnicode2(dataStr: String): String? {
        var start = 0
        var end = 0
        val buffer = StringBuffer()
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2)
            var charStr: String? = null
            charStr = if (end == -1) {
                dataStr.substring(start + 2, dataStr.length)
            } else {
                dataStr.substring(start + 2, end)
            }
            val letter = charStr.toInt(16).toChar()
            buffer.append(letter.toString())
            start = end
        }
        return buffer.toString()
    }

}