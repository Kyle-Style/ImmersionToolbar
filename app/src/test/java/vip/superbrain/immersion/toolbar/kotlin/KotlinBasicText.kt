package vip.superbrain.immersion.toolbar.kotlin

import org.junit.Test

class KotlinBasicText {

    @Test
    fun standardLibTest() {
        val because = "只是因为在人群中多看了你一眼"
        //onEach
        var onEachList = arrayListOf<String>("Kotlin", "Java", "JS", "Html", "Python")
        onEachList.filter {
            it.isNotEmpty() && it.isNotBlank()
        }.onEach {
            println(it.plus(666))
            it.plus(666)
        }.forEach {
            print(it + "\t")
        }
        //also
        var alsoString = because
        alsoString.also {
            println(it)
        }
        //takeIf
        var takeIfNotNull = because
        var takeIfNull = null
        assert(takeIfNotNull.takeIf { takeIfNotNull.isNullOrEmpty() }.isNullOrEmpty())
        //takeIfNull
        assert(takeIfNotNull.takeUnless { takeIfNotNull.isEmpty() }?.contains("只是") != null)
        //groupingBy
        var words = "one two three four five six seven eight nine ten".split(' ')
        var wordsCount = words.groupBy {
            it.first()
        }.count()
        println("wordsCount " + wordsCount)
        //map
        var groupMap = hashMapOf<String, Any>(
            "中国" to "李时珍",
            "中国" to "周恩来",
            "中国" to "毛泽东",
            "中国" to "曾国藩",
            "中国" to "华佗",
            "中国" to "老子",
            "美国" to "华盛顿",
            "美国" to "克林顿",
            "美国" to "林肯",
            "美国" to "奥巴马"
        )
        val mapToMapMap = groupMap.toMap()
        println(mapToMapMap)
        println(mapToMapMap.get("中国2"))
        println(mapToMapMap.getValue("美国"))
        println(mapToMapMap)
        //minOf maxOf
        var firstNum = 99
        var secondNum = 88
        var thirdNum = 77
        var min = minOf(firstNum, secondNum, thirdNum)
        var max = maxOf(firstNum, secondNum, thirdNum)
        println("min ${min} max ${max}")
        //array
        var arrayTest = arrayOf(
            "sndif",
            "skdnfjwnfijw",
            "kdniwpkepkrop",
            "mnsidw",
            "xcvwe",
            "jowjoen",
            "difwhuefbb"
        )
        var arrayEqual = arrayOf(
            "sndif",
            "skdnfjwnfijw",
            "kdniwpkepkrop",
            "mnsidw",
            "xcvwe",
            "jowjoen",
            "difwhuefbb"
        )
        var arrayNotEqual = arrayOf(
            "sndif",
            "skdnfjwnfijw",
            "kdniwpkepkrop",
            "mnsidw",
            "xcvwe",
            "jowjoen",
            "difwhuefb000b"
        )
        println("content equals " + arrayTest.contentEquals(arrayEqual))
        println("content not equals " + arrayTest.contentEquals(arrayNotEqual))
        println("content hash code " + arrayTest.contentHashCode())
        //strings
        var defaultStrings = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
        var curtomStrings = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
        println("defaultStrings " + defaultStrings.joinToString())
        println("defaultStrings " + defaultStrings.joinToString {
            it.toString().plus("\t⑥")
        })
        ///chunked windowed zipWithNext
        val items = (1..9).map { it * it }
        val chunkedIntoLists = items.chunked(4)
        val points3d = items.chunked(3) { (x, y, z) -> Triple(x, y, z) }
        val windowed = items.windowed(4)
        val slidingAverage = items.windowed(4) { it.average() }
        val pairwiseDifferences = items.zipWithNext { a, b -> b - a }
        println("chunkedIntoLists   " + chunkedIntoLists)
        println("points3d   " + points3d)
        println("windowed   " + windowed)
        println("slidingAverage   " + slidingAverage)
        println("pairwiseDifferences   " + pairwiseDifferences)
        //fill replaceAll shuffle/shuffled
        val itemsReplace = (1..10).toMutableList()
        println("itemsReplace   ${itemsReplace}")
        itemsReplace.shuffle()
        println("itemsReplace   shuffle   ${itemsReplace}")
        itemsReplace.replaceAll { it * 2 }
        println("itemsReplace 2*   ${itemsReplace}")
        itemsReplace.fill(5)
        println("itemsReplace fill   ${itemsReplace}")
        //NPE
        var npe: String? = null
        if (npe.isNullOrEmpty()) {
            println("npe    ${npe?.length}")
        }
        npeTest(npe)
        //list filter
        var listFilter = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
        println(listFilter.filter { it > 4 })
    }

    private fun npeTest(input: String?) {
        if (input.isNullOrEmpty()) {
            println("length of '$input'    ${input?.length}")
        }
    }
}