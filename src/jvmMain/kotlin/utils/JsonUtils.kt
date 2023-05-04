package utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import model.entity.JsonData
import java.util.regex.Matcher
import java.util.regex.Pattern

object JsonUtils {

    /**
     * 解析成漂亮格式
     */
    fun toPrettyFormat(json: String): String? {
        try {
            val jsonObject = JsonParser.parseString(json).asJsonObject
            val gson = GsonBuilder().setPrettyPrinting().create()
            return gson.toJson(jsonObject)
        } catch (e: Exception) {
            throw JsonParseException("JSON格式不正确")
        }
    }

    fun paresJsonData(json: String): List<JsonData> {
        val list = mutableListOf<JsonData>()
        //先格式化
        val prettyFormat = try {
            toPrettyFormat(json) ?: ""
        } catch (e: Exception) {
            ""
        }
        val lines = prettyFormat.reader().readLines()
        //解析每一行
        lines.forEachIndexed { index, line ->
            //区分类型
            var type = -1
            var key = ""
            var value = ""

            val jsonData: JsonData

            if (line.trim().endsWith(": [") || line.trim() == "[") {
                //数组
                type = 2
                val space = getLineStartSpaceCount(line)
                val arrayName = if (line.trim().endsWith(": [")) {
                    line.trim().replace(": [", "").replace("\"", "")
                } else {
                    ""
                }
                //数组的结束位置
                val endIndex = findArrayEndIndex(lines, space, index)
                jsonData = JsonData(
                    index = index,
                    content = line,
                    type = type,
                    paceCount = space,
                    key = "",
                    value = "",
                    arrayName = arrayName,
                    arrayStart = true,
                    objStart = false,
                    arrayEndIndex = endIndex,
                    objEndIndex = 0,
                )

                list.add(jsonData)

            } else if (line.trim().endsWith(": {") || line.trim() == "{") {
                //对象
                type = 1
                //查找结束位置
                val space = getLineStartSpaceCount(line)
                val endIndex = findObjEndIndex(lines, space, index)
                val objectName = if (line.trim().endsWith(": {")) {
                    line.trim().replace(": {", "").replace("\"", "")
                } else {
                    ""
                }
                jsonData = JsonData(
                    index = index,
                    content = line,
                    type = type,
                    paceCount = space,
                    key = "",
                    value = "",
                    arrayName = "",
                    objectName = objectName,
                    arrayStart = false,
                    objStart = true,
                    arrayEndIndex = 0,
                    objEndIndex = endIndex,
                )
                list.add(jsonData)
            } else if (line.trim() == "}" || line.trim() == "}," || line.trim() == "]" || line.trim() == "],") {
                type = -1
                val space = getLineStartSpaceCount(line)
                jsonData = JsonData(
                    index = index,
                    content = line,
                    type = type,
                    paceCount = space,
                    key = "",
                    value = "",
                    arrayName = "",
                    arrayStart = false,
                    objStart = false,
                    arrayEndIndex = 0,
                    objEndIndex = 0,
                )
                list.add(jsonData)
            } else {
                //普通字段
                type = 0
                if (line.trim().contains(": ")) {
                    val split = line.trim().split(": ")
                    if (split.size <= 1) return@forEachIndexed

                    key = split[0].replace("\"", "")
                    value = split[1]
                    //0:字符串 1 数字 2 Boolean 3 null
                    val valueType = if (value.contains("\"")) {
                        0
                    } else if (value.contains("true") || value.contains("false")) {
                        2
                    } else if (value.contains("null")) {
                        3
                    } else {
                        1
                    }
                    val objLastOne = lines[index + 1].trim() == "}" || lines[index + 1].trim() == "},"

                    val space = getLineStartSpaceCount(line)
                    jsonData = JsonData(
                        index = index,
                        content = line,
                        type = type,
                        paceCount = space,
                        key = key,
                        value = value,
                        arrayName = "",
                        arrayStart = false,
                        objStart = false,
                        arrayEndIndex = 0,
                        objEndIndex = 0,
                        objLastOne = objLastOne,
                        valueType = valueType,
                    )
                    list.add(jsonData)
                } else {
                    val str = line.trim()
                    val regex = "^(" + Pattern.quote(str) + ")"
                    val pattern = Pattern.compile(regex)
                    val matcher: Matcher = pattern.matcher(str)
                    if (matcher.matches()) {
                        type = 4
                        val space = getLineStartSpaceCount(line)
                        jsonData = JsonData(
                            index = index,
                            content = line,
                            type = type,
                            paceCount = space,
                            arrayName = "",
                            singleArrayContent = line.trim(),
                        )
                        list.add(jsonData)
                    }



//                    Regex("")
//                    val regex = "^(" + Pattern.quote(line.trim()) + ")"
//                    val match = line.trim().let { line.trim().matches(regex) }
//                    line.trim().contains(": ")
                }
            }

        }
        return list
    }

    private fun findArrayEndIndex(lines: List<String>, space: Int, startIndex: Int): Int {
        lines.forEachIndexed { index, line ->
            if (index <= startIndex) {
                return@forEachIndexed
            }
            //当前的空格数
            val currentSpaceCount = getLineStartSpaceCount(line)
            if (currentSpaceCount == space) {
                //空格数相同 并且是对象的结束
                if (line.trim() == "]" || line.trim() == "],") {
                    return index
                }
            }
        }

        return 0
    }

    private fun findObjEndIndex(lines: List<String>, space: Int, startIndex: Int): Int {
        lines.forEachIndexed { index, line ->
            if (index <= startIndex) {
                return@forEachIndexed
            }
            //当前的空格数
            val currentSpaceCount = getLineStartSpaceCount(line)
            if (currentSpaceCount == space) {
                //空格数相同 并且是对象的结束
                if (line.trim() == "}" || line.trim() == "},") {
                    return index
                }
            }
        }

        return 0
    }

    private fun getLineStartSpaceCount(string: String?): Int {
        if (string == null) return 0

        if (string.isBlank()) return string.length

        val trimStart = string.trimStart()
        return string.length - trimStart.length
    }
}
