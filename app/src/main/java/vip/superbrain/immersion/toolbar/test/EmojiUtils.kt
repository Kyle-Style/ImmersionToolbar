package vip.superbrain.immersion.toolbar.test

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
class EmojiUtils {

    private fun isEmojiCharacter(codePoint: Int): Boolean {
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
            return 0;
        }
        var cpCount = src.codePointCount(0, src.length)
        var firCodeIndex = src.offsetByCodePoints(0, 0)
        var lstCodeIndex = src.offsetByCodePoints(0, cpCount - 1)
        var position = 0
        for (index in firCodeIndex..lstCodeIndex) {
            var codepoint = src.codePointAt(index)
            if (!isEmojiCharacter(codepoint)) {
            }
            position += if(Character.isSupplementaryCodePoint(codepoint)) 2 else 1
        }
        return position;
    }
}