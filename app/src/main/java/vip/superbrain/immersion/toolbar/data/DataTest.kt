package vip.superbrain.immersion.toolbar.data

/**
 * @Description
 *
 * @Author wangleilei
 * @Email wangleilei@mockuai.com
 * @Date 2020-02-18 16:21
 */
class DataTest {

    var data: ArrayList<String> = arrayListOf()
        set(value) {
            println("1 field $field")
            println("2 value $value")
            field?.clear()
            println("3 field $field")
            field = value
            println("4 field $field")
            println("5 value $value")
        }

    fun getData222():ArrayList<String> {
        print("getData222   $data")
        return data
    }
}