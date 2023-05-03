package ui.dialog

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.viewmodel.JsonAction
import model.viewmodel.JsonViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun JsonPrettyErrorDialog(viewModel: JsonViewModel) {
//    val showErrorDialog = viewModel.uiState.collectAsState().value.showParseErrorDialog
//    if (showErrorDialog) {
//        AlertDialog(modifier = Modifier.width(400.dp).wrapContentHeight(), onDismissRequest = {
//            viewModel.disPatch(JsonAction.ShowErrorDialog(false))
//        },
//            confirmButton = {
//                Button(onClick = {
//                    viewModel.disPatch(JsonAction.ShowErrorDialog(false))
//                }) {
//                    Text(text = "确定")
//
//                }
//            },
//            dismissButton = {
//                Button(onClick = {
//                    viewModel.disPatch(JsonAction.ShowErrorDialog(false))
//                }) {
//                    Text(text = "关闭")
//                }
//            },
//            text = {
//                Text("JSON格式解析异常")
//            },
//            title = {
//                Text("异常提示！")
//            }
//        )
//    }

}