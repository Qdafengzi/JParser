package model.entity

data class JsonData(
    val index: Int = 0,
    val content: String = "",//原始内容
    val type: Int = 0,// 0:普通 1:对象 2:数组
    val paceCount: Int = 0,//空格的个数
    val key: String = "",
    val value: String = "",
    val objectName: String = "",//对象的名称
    val arrayName: String = "",
    val singleArrayContent:String = "",//单行数组的内容
    val arrayStart: Boolean = false,
    val objStart: Boolean = false,
    val arrayEndIndex: Int = 0,//数组的结尾对应的行数
    val objEndIndex: Int = 0,//对象的结尾行数
    val objLastOne: Boolean = false,//对象的最后一个字段
    val foldFlag: Boolean = false,//折叠
    val foldIcon:String = "images/ic_minus.svg",
    val valueType: Int = 0,//0:字符串 1 数字 2 Boolean 3 null 4数组 对象是简单类型
)