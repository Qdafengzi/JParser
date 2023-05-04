import androidx.compose.animation.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import model.viewmodel.JsonAction
import model.viewmodel.JsonViewModel
import ui.theme.*

val viewModel = JsonViewModel()

@Composable
@Preview
fun App() {
//    JsonPrettyErrorDialog(viewModel)
    MaterialTheme {
        Row(
            Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.weight(1f).background(color = backgroundColor)) {
                LeftContent(viewModel)
            }

            Divider(modifier = Modifier.width(1.dp).fillMaxHeight())
            Column(
                modifier = Modifier.weight(2f).fillMaxHeight().background(color = backgroundColor)
            ) {
                RightContent(viewModel)
            }
        }
    }
}

@Composable
fun RightContent(viewModel: JsonViewModel) {
    val uiState = viewModel.uiState.collectAsState().value
    val list = uiState.list

    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        val spaceBase = 15
        val state = rememberLazyListState()
        LazyColumn(
            state = state,
            modifier = Modifier.weight(1f).fillMaxHeight()
                .padding(start = 10.dp, top = 20.dp, end = 10.dp, bottom = 20.dp)
        ) {
            itemsIndexed(items = list, key = { index, item ->
                "$index${item.content}"
            }, itemContent = { index, item ->
//                            println("$index==================>update")
                SelectionContainer {
                    when (item.type) {
                        0 -> {
                            AnimatedVisibility(
                                visible = !item.foldFlag,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically(),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Spacer(Modifier.padding(start = (item.paceCount * spaceBase).dp))
                                    ////0:字符串 1 数字 2 Boolean 3 null
                                    val color = when (item.valueType) {
                                        0 -> Color(0xffa8f362)
                                        1 -> Color(0xFFca4ef2)
                                        2 -> Color(0xFFEF1D4A)
                                        3 -> Color(0xFFFFAE10)
                                        else -> Color(0xFFF8672C)
                                    }

                                    Canvas(modifier = Modifier.size(8.dp), onDraw = {
                                        drawRoundRect(
                                            color = color, cornerRadius = CornerRadius(2f, 2f)
                                        )
                                    })

                                    Text(
                                        text = item.key,
                                        color = textColor,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )

                                    Text(
                                        text = " : ", color = textColor, fontSize = 14.sp, modifier = Modifier.padding()
                                    )

                                    Text(
                                        text = item.value,
                                        color = textColor,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding()
                                    )
                                }
                            }
                        }

                        1 -> {
                            AnimatedVisibility(
                                visible = !item.foldFlag,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically(),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Spacer(Modifier.padding(start = (item.paceCount * spaceBase).dp))
                                    Icon(
                                        painter = painterResource(item.foldIcon),
                                        contentDescription = null,
                                        modifier = Modifier.size(spaceBase.dp).clickable {
                                            viewModel.disPatch(
                                                jsonAction = JsonAction.HandleExpandAndFold(
                                                    item, index
                                                )
                                            )
//                                                               viewModel.aaa(item)

                                        },
                                        tint = objColor
                                    )
                                    Icon(
                                        painter = painterResource("images/ic_data_object.svg"),
                                        contentDescription = null,
                                        modifier = Modifier.size(spaceBase.dp),
                                        tint = objColor
                                    )

                                    if (item.objectName.isNotBlank()) {
                                        Text(
                                            text = item.objectName,
                                            color = objColor,
                                            fontSize = 14.sp,
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }
                                }
                            }
                        }

                        2 -> {
                            AnimatedVisibility(
                                visible = !item.foldFlag,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically(),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Spacer(Modifier.padding(start = (item.paceCount * spaceBase).dp))
                                    Icon(
                                        painter = painterResource(item.foldIcon),
                                        contentDescription = null,
                                        modifier = Modifier.size(spaceBase.dp).clickable {
                                            viewModel.disPatch(
                                                jsonAction = JsonAction.HandleExpandAndFold(
                                                    item, index
                                                )
                                            )
//                                                            viewModel.aaa(item)
                                        },
                                        tint = arrayColor,
                                    )
                                    Icon(
                                        painter = painterResource("images/ic_data_array.svg"),
                                        contentDescription = null,
                                        modifier = Modifier.size(spaceBase.dp),
                                        tint = arrayColor
                                    )
                                    Text(
                                        text = item.arrayName,
                                        color = arrayColor,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding()
                                    )
                                }
                            }

                        }
                        //简单类型的数组
                        4->{
                            AnimatedVisibility(
                                visible = !item.foldFlag,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically(),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Spacer(Modifier.padding(start = (item.paceCount * spaceBase).dp))
                                    Text(
                                        text = item.singleArrayContent,
                                        color = textColor,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding()
                                    )
                                }
                            }
                        }

                        -1 -> {
                            if (item.content.trim() == "}," || item.content.trim() == "}" || item.content.trim() == "]" || item.content.trim() == "],") {
//                                       Row(modifier = Modifier.fillMaxWidth()) {
//                                           Spacer(Modifier.padding(start = (item.paceCount * spaceBase).dp))
//                                           Text(
//                                               text = item.content.trim(), color = Color.White, fontSize = 14.sp,
//                                               modifier = Modifier.padding()
//                                           )
//                                       }
                            }
                        }


                        else -> {}
                    }


                }
            })
        }

        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(state), style = LocalScrollbarStyle.current.copy(
                unhoverColor = scrollUnActiveColor,
                hoverColor = scrollActiveColor,
            )
        )
    }
}

@Composable
fun LeftContent(viewModel: JsonViewModel) {
    val uiState = viewModel.uiState.collectAsState().value
    val prettyFormat = uiState.prettyJson
    val parseError = uiState.parseError

    Column(Modifier.fillMaxSize()) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            val scrollState = rememberScrollState()

            OutlinedTextField(
                value = prettyFormat,
                onValueChange = {
                    viewModel.disPatch(JsonAction.PrettyJson(it))
                },
                modifier = Modifier.weight(1f)
                    .fillMaxHeight()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .verticalScroll(scrollState),
                textStyle = TextStyle.Default.copy(
                    color = Color.White,
                    lineHeight = 28.sp,
                    fontSize = 14.sp,
                ),
                isError = parseError,
                placeholder = {
                    Text(
                        modifier = Modifier.wrapContentWidth(),
                        style = LocalTextStyle.current.copy(
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start,
                        ),
                        text = "把JSON内容粘贴到这里",
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Red.copy(alpha = 0.4f),
                    cursorColor = Color.White,
                    textColor = Color.White,
                    placeholderColor = Color.White.copy(alpha = 0.4f),
                ),
            )
            VerticalScrollbar(
                modifier = Modifier.fillMaxHeight(),
                adapter = rememberScrollbarAdapter(scrollState),
                style = LocalScrollbarStyle.current.copy(
                    unhoverColor = scrollUnActiveColor,
                    hoverColor = scrollActiveColor,
                )
            )
        }
    }
}


fun main() = application {
    Window(
        title = "JParser", onCloseRequest = ::exitApplication,

        ) {
        App()
    }
}
