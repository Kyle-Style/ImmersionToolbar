package vip.superbrain.immersion.toolbar.utils

import vip.superbrain.immersion.toolbar.utils.EmojiHelper.EmojiParser.Modifier.Black
import vip.superbrain.immersion.toolbar.utils.EmojiHelper.EmojiParser.Modifier.ColorFul
import vip.superbrain.immersion.toolbar.utils.EmojiHelper.EmojiParser.Modifier.Joiner
import vip.superbrain.immersion.toolbar.utils.EmojiHelper.EmojiParser.Modifier.KeyCap
import vip.superbrain.immersion.toolbar.utils.EmojiHelper.EmojiParser.Modifier.TagRange
import vip.superbrain.immersion.toolbar.utils.EmojiHelper.EmojiParser.STATE.DEFAULT
import vip.superbrain.immersion.toolbar.utils.EmojiHelper.EmojiParser.STATE.EMOJI
import vip.superbrain.immersion.toolbar.utils.EmojiHelper.EmojiParser.STATE.EMOJI_JOIN
import vip.superbrain.immersion.toolbar.utils.EmojiHelper.EmojiParser.STATE.EMOJI_MODIFIER
import vip.superbrain.immersion.toolbar.utils.EmojiHelper.EmojiParser.STATE.NATIONAL_FLAG
import vip.superbrain.immersion.toolbar.utils.EmojiHelper.EmojiParser.STATE.PRE_EMOJI
import java.util.*

class EmojiHelper private constructor() {

    private val parser: EmojiParser

    init {
        parser = EmojiParser()
    }

    companion object {

        @JvmStatic
        val instance: EmojiHelper by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            EmojiHelper()
        }
    }

    // 获取视觉上的符号个数
    fun getVisionSymbolCount(str: CharSequence): Int {
        parser.read(str)
        return parser.getCurrentCharSize()
    }

    fun analyzeText(str: CharSequence): List<Node> {

        parser.read(str)
        return parser.getCharList().map { data ->
            val length = data.codePoint.fold(0) { sum, code ->
                sum + Character.charCount(code)
            }
            Node(data.startIndex, length, data.isEmoji, data.codePoint.toList())
        }
    }

    /**
     * 判断字符串中指定位置的字符是否是 `emoji` 表情。
     * 注意这里的索引是可见的索引数，而不是字符的索引数。
     * 一个 `emoji` 可能由多个 `unicode` 码点组成，那么它是一个可见的长度，但却是多个字符长度。
     *
     * @param str 可能包含 `emoji` 的字符串
     * @param idx 视觉上的索引数，即按照一个 `emoji` 长度为一来计算的索引
     *
     * @return 是否 `emoji` 表情。
     */
    fun isEmojiOfVisionIndex(str: CharSequence, idx: Int): Boolean =
        isEmojiOfVisionIndex(analyzeText(str), idx)

    /**
     * 判断字符串中指定位置的字符是否是 `emoji` 表情。
     * 注意这里的索引是可见的索引数，而不是字符的索引数。
     * 一个 `emoji` 可能由多个 `unicode` 码点组成，那么它是一个可见的长度，但却是多个字符长度。
     *
     * @param nodeList 通过 [analyzeText] 得出的结果。
     * @param idx 视觉上的索引数，即按照一个 `emoji` 长度为一来计算的索引
     *
     * @return 是否 `emoji` 表情。
     */
    fun isEmojiOfVisionIndex(nodeList: List<Node>, idx: Int): Boolean {
        return nodeList[idx].isEmoji
    }

    /**
     * 判断字符串中指定位置的字符是否是 `emoji` 表情。
     * 注意这里的索引是字符的索引数。
     * 一个 `emoji` 可能由多个 `unicode` 码点组成，那么它是一个可见的长度，但却是多个字符长度。
     *
     * @param str 可能包含 `emoji` 的字符串。
     * @param idx 字符的索引数，即 idx 范围为 [0, String::length] 。
     *
     * @return 是否 `emoji` 表情。
     */
    fun isEmojiOfCharIndex(str: CharSequence, idx: Int): Boolean =
        isEmojiOfCharIndex(analyzeText(str), idx)

    /**
     * 判断字符串中指定位置的字符是否是 `emoji` 表情。
     * 注意这里的索引是字符的索引数。
     * 一个 `emoji` 可能由多个 `unicode` 码点组成，那么它是一个可见的长度，但却是多个字符长度。
     *
     * @param nodeList 通过 [analyzeText] 得出的结果。
     * @param idx 字符的索引数，即 idx 范围为 [0, String::length]。
     *
     * @return 是否 `emoji` 表情。
     */
    fun isEmojiOfCharIndex(nodeList: List<Node>, idx: Int): Boolean {
        val visionIdx = nodeList.binarySearch { node ->
            when {
                idx < node.startIndex -> 1
                idx >= node.startIndex + node.length -> -1
                else -> 0
            }
        }
        if (visionIdx < 0) {
            return false
        }
        return isEmojiOfVisionIndex(nodeList, visionIdx)
    }

    /**
     * 裁剪包含 `emoji` 表情的字符串，并确保 `emoji` 不会被砍成两半。
     */
    fun subSequence(str: CharSequence, end: Int): CharSequence = subSequence(str, 0, end)

    /**
     * 裁剪包含 `emoji` 表情的字符串，并确保 `emoji` 不会被砍成两半。
     */
    fun subSequence(str: CharSequence, start: Int, end: Int): CharSequence {
        if (start < 0 || end > str.length) {
            throw IndexOutOfBoundsException(
                "The index should be in range [0,${str.length}]," +
                        "but actually start = $start and end = $end."
            )
        }
        if (start > end) {
            throw IndexOutOfBoundsException(
                "The start index should be not bigger than end," +
                        "but actually start = $start and end = $end."
            )
        }
        if (start == end) {
            return ""
        }

        parser.read(str, start + end)
        val charList = parser.getCharList()

        val startIdx = charList.getOrNull(start)?.startIndex
            ?: return ""

        val endIdx = charList.getOrNull(end - 1)?.let {
            it.startIndex + it.codePoint.fold(0) { sum, cp -> sum + Character.charCount(cp) }
        }
        return if (endIdx == null) {
            str.subSequence(startIdx, str.length)
        } else {
            str.subSequence(startIdx, endIdx)
        }
    }

    private class EmojiParser {

        object STATE {
            const val DEFAULT = 0x0
            const val EMOJI = 0x1
            const val PRE_EMOJI = 0x10
            const val NATIONAL_FLAG = EMOJI or 0x100
            const val EMOJI_MODIFIER = EMOJI or 0x1000
            const val EMOJI_JOIN = 0x10000
        }

        object Modifier {
            // 连接两个emoji元素成为一个新的emoji
            const val Joiner = 0x200D
            // 黑
            const val Black = 0xFE0E
            // 颜色
            const val ColorFul = 0xFE0F
            // 键帽
            const val KeyCap = 0x20E3
            // 标签
            val TagRange = 0xE0020..0xE007F
        }

        private val modifierSet = setOf(
            Black,
            ColorFul,
            KeyCap
        ) + TagRange


        private val charUnitList = mutableListOf<InnerNode>()

        private var currentIndex = 0

        private var currentCodePoint = 0x0

        private var currentChar = InnerNode(0)

        private var currentState = DEFAULT

        private fun endChar() {
            currentState = DEFAULT
            if (currentChar.codePoint.isNotEmpty()) {
                charUnitList.add(currentChar)
                currentChar = InnerNode(currentIndex)
            }
        }

        private fun assertEmoji() {
            currentChar.isEmoji = true
        }

        private fun moveToNext() {
            currentChar.codePoint.add(currentCodePoint)
            currentIndex += Character.charCount(currentCodePoint)
        }

        private fun moveToPrev() {
            val lastCodePoint = currentChar.codePoint.removeLast()
            currentIndex -= Character.charCount(lastCodePoint)
        }

        fun read(str: CharSequence, end: Int = str.length) {
            while (currentIndex < str.length) {
                currentCodePoint = Character.codePointAt(str, currentIndex)
                when {
                    currentState == EMOJI_JOIN -> when {
                        isEmojiCodePoint(currentCodePoint) -> {
                            currentState = EMOJI
                            moveToNext()
                        }
                        else -> {
                            moveToPrev()
                            endChar()
                        }
                    }
                    currentState == NATIONAL_FLAG -> when {
                        isRegionalIndicator(currentCodePoint) -> {
                            moveToNext()
                            assertEmoji()
                            endChar()
                        }
                        else -> {
                            assertEmoji()
                            endChar()
                        }
                    }
                    currentState == PRE_EMOJI -> when {
                        modifierSet.contains(currentCodePoint) -> {
                            currentState = EMOJI_MODIFIER
                            moveToNext()
                        }
                        else -> {
                            endChar()
                        }
                    }
                    currentState and EMOJI != 0 -> when {
                        Joiner == currentCodePoint -> {
                            currentState = EMOJI_JOIN
                            moveToNext()
                        }
                        modifierSet.contains(currentCodePoint) -> {
                            currentState = EMOJI_MODIFIER
                            moveToNext()
                        }
                        else -> {
                            assertEmoji()
                            endChar()
                        }
                    }
                    else -> when {
                        isRegionalIndicator(currentCodePoint) -> {
                            currentState = NATIONAL_FLAG
                            moveToNext()
                        }
                        maybeEmojiCodePoint(currentCodePoint) -> {
                            currentState = PRE_EMOJI
                            moveToNext()
                        }
                        isEmojiCodePoint(currentCodePoint) -> {
                            currentState = EMOJI
                            moveToNext()
                        }
                        else -> {
                            moveToNext()
                            endChar()
                        }
                    }
                }

                if (getCurrentCharSize() >= end) {
                    break
                }
            }

            if (currentState != DEFAULT) {
                if (currentState and EMOJI != 0) {
                    assertEmoji()
                }
                endChar()
            }
        }

        fun getCurrentIndex(): Int = currentIndex

        fun getCurrentCharSize(): Int = charUnitList.size

        fun getCharList(): List<InnerNode> = charUnitList

        private fun isEmojiCodePoint(codePoint: Int) =
            (codePoint in 0x1F200..0x1FFFF) || (codePoint in 0x2500..0x2FFF) || isSpecialSymbol(
                codePoint
            )

        private fun isSpecialSymbol(codePoint: Int) =
            codePoint == 0x3030 || codePoint == 0x00A9 || codePoint == 0x00AE || codePoint == 0x2122

        private fun maybeEmojiCodePoint(codePoint: Int) = codePoint in 0x0..0x39

        private fun isRegionalIndicator(codePoint: Int) = codePoint in 0x1F000..0x1F1FF
    }

    private data class InnerNode(
        val startIndex: Int,
        var isEmoji: Boolean = false,
        val codePoint: Deque<Int> = LinkedList()
    )

    data class Node(
        val startIndex: Int,
        val length: Int,
        val isEmoji: Boolean,
        val codePoint: List<Int>
    )
}