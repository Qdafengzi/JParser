package model.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import model.entity.JsonData
import utils.JsonUtils
import utils.JsonUtils.toPrettyFormat


data class JsonUiState(
    val list: List<JsonData> = emptyList(),
    val prettyJson: String = "",
//    val showParseErrorDialog: Boolean = false,
    val parseError:Boolean = false,//解析错误
)

sealed class JsonAction {
    data class HandleExpandAndFold(val item: JsonData, val index: Int) : JsonAction()
    data class PrettyJson(val json: String) : JsonAction()
    data class UpdateJsonData(val json: String) : JsonAction()

//    data class ShowErrorDialog(val show: Boolean) : JsonAction()

}

class JsonViewModel {
    private val _uiState = MutableStateFlow(JsonUiState())
    val uiState = _uiState.asStateFlow()


    fun disPatch(jsonAction: JsonAction) {
        when (jsonAction) {
            is JsonAction.HandleExpandAndFold -> {
                handleExpandAndFold(jsonAction.item, jsonAction.index)
            }

            is JsonAction.PrettyJson -> {
                prettyJson(jsonAction.json)
            }

//            is JsonAction.ShowErrorDialog -> {
//                _uiState.update {
//                    it.copy(showParseErrorDialog = jsonAction.show)
//                }
//            }

            is JsonAction.UpdateJsonData -> {
                prettyJson(jsonAction.json)
            }

            else -> {

            }
        }
    }

    private fun prettyJson(json: String) {
        if(json.isBlank()) {
            _uiState.update {
                it.copy(prettyJson = "", list = emptyList(),parseError = false)
            }
            return
        }

        try {
            val prettyJson = toPrettyFormat(json) ?: ""
            _uiState.update {
                it.copy(prettyJson = prettyJson, parseError = false)
            }
            getJsonListData(prettyJson)
        } catch (e: Exception) {
//            _uiState.update {
//                it.copy(showParseErrorDialog = true)
//            }
            _uiState.update {
                it.copy(prettyJson = json, list = emptyList(),parseError = true)
            }
        }
    }

    private fun getJsonListData(json: String) {
        _uiState.update {
            it.copy(list = emptyList())
        }

        val paresJsonData = JsonUtils.paresJsonData(json)
        println("size=======>${paresJsonData.size}")
        _uiState.update {
            it.copy(list = paresJsonData)
        }
    }

    private fun handleExpandAndFold(item: JsonData, index: Int) {
        //查看下一个item的折叠状态 才对
        val foldStatus = _uiState.value.list[index + 1].foldFlag
        println("=============>类型：${item.type}  折叠：${foldStatus}")
        val endIndex = when (item.type) {
            1 -> {
                item.objEndIndex
            }

            2 -> {
                item.arrayEndIndex
            }

            else -> {
                0
            }
        }
        val uiList = _uiState.value.list.toMutableList()
        run breaking@{
            uiList.forEachIndexed { index, jsonData ->
                if (index < item.index) {
                    return@forEachIndexed
                }
                if (index == item.index) {
                    val icon = if (jsonData.foldIcon == "images/ic_minus.svg") {
                        "images/ic_add.svg"
                    } else {
                        "images/ic_minus.svg"
                    }
                    uiList[index] = jsonData.copy(foldIcon = icon)
                    return@forEachIndexed
                }
                if (index <= endIndex) {
                    val icon = if (!foldStatus) {
                        "images/ic_add.svg"
                    } else {
                        "images/ic_minus.svg"
                    }

                    uiList[index] = jsonData.copy(foldFlag = !foldStatus, foldIcon = icon)
//                    uiList[index] = jsonData.copy(fold = !jsonData.fold)
                    println("$index===========>${uiList[index].foldFlag}            endIndex:$endIndex")
                } else {
                    return@breaking
                }
            }
            //更新数据
        }
        _uiState.update {
            it.copy(list = uiList.toList())
        }
    }
}