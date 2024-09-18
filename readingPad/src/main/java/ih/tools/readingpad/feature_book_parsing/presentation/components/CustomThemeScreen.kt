package ih.tools.readingpad.feature_book_parsing.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.feature_theme_color.domain.model.ThemeColorType
import ih.tools.readingpad.ui.UIStateViewModel

@Composable
fun CustomThemeScreen(
    viewModel: BookContentViewModel,
    uiStateViewModel: UIStateViewModel,
    dialogWidth: Dp,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val onDismiss = {
        //viewModel.setShowCustomThemePage(false)
        uiStateViewModel.showDialog(null)
        uiStateViewModel.showScreen(null)
    }
//    var backgroundColor by remember { mutableIntStateOf(viewModel.backgroundColor.value) }
//    var fontColor by remember { mutableIntStateOf(viewModel.fontColor.value) }

    //an outer box that fills the entire screen but transparent to allow the custom placement of the inner box
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp)
            .clickable(onClick = {
                onDismiss()
            }),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .width(dialogWidth)
                .clickable { }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                ThemePreviewer(

                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(12)
                        )
                        .fillMaxWidth(),
                    uiStateViewModel = uiStateViewModel
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                        )
                        .fillMaxWidth()
                ) {
                    when (selectedTabIndex) {
                        0 -> BackgroundColorScreen(modifier = Modifier, viewModel, uiStateViewModel)
                        1 -> FontColorScreen(modifier = Modifier, viewModel, uiStateViewModel)
                    }
                }

                //Spacer(modifier = Modifier.height(2.dp))

                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Transparent,

                    modifier = Modifier
                    //.padding(4.dp)
                ) {
                    // first tab, Background color
                    Tab(
                        selected = (selectedTabIndex == 0),
                        onClick = { selectedTabIndex = 0 },
                        if (selectedTabIndex == 0) {
                            Modifier
//                                .padding(horizontal = 8.dp)
                                .background(
                                    MaterialTheme.colorScheme.secondaryContainer,
                                    shape = RoundedCornerShape(bottomStart = 12.dp)
                                )
                        } else {
                            Modifier
                            // .padding(horizontal = 8.dp)
//                                .background(MaterialTheme.colorScheme.onSecondaryContainer,
//                                    shape = RoundedCornerShape(12))
                        },
                        selectedContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        unselectedContentColor = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Filled.FormatColorFill,
                                contentDescription = "Background color"
                            )
                            Text(text = "Background")
                        }
                    }
                    // second tab, Font color
                    Tab(
                        selected = (selectedTabIndex == 1),
                        onClick = { selectedTabIndex = 1 },
                        if (selectedTabIndex == 1) {
                            Modifier
                                //  .padding(horizontal = 8.dp)
                                .background(
                                    MaterialTheme.colorScheme.secondaryContainer,
                                    shape = RoundedCornerShape(bottomEnd = 12.dp)
                                )
                        } else {
                            Modifier
                            //   .padding(horizontal = 8.dp)
//                                .background(MaterialTheme.colorScheme.onSecondaryContainer,
//                                    shape = RoundedCornerShape(12))
                        },
                        selectedContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        unselectedContentColor = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Filled.FormatColorText,
                                contentDescription = "Font color"
                            )
                            Text(text = "Font")
                        }
                    }

                }
            }
        }
    }
}


@Composable
fun ThemePreviewer(
    modifier: Modifier,
    uiStateViewModel: UIStateViewModel

) {
    val fontColor by uiStateViewModel.currentThemeFontColor.observeAsState()
    val backgroundColor by uiStateViewModel.currentThemeBackgroundColor.observeAsState()
    //val fontSize by viewModel.fontSize.collectAsState()

    Row(
        modifier
            //.fillMaxSize()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(8.dp)
                // .fillMaxHeight()
                .weight(3f)
                .background(
                    Color(backgroundColor!!),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            Text(
                text = "This is an Example",
                color = Color(fontColor!!),
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }

        FilledIconButton(onClick = {
            uiStateViewModel.showScreen(null)
            uiStateViewModel.showDialog(null)
            uiStateViewModel.setFontColor(fontColor!!)
            uiStateViewModel.setBackgroundColor(backgroundColor!!)
//            viewModel.setShowCustomThemePage(false)
//            viewModel.setFontColor(fontColor!!)
//            viewModel.setBackgroundColor(backgroundColor!!)
        }) {
            Icon(
                Icons.Filled.DoneAll,
                contentDescription = "save theme",
                modifier = Modifier.weight(1f)
            )
        }

    }

}


@Composable
fun FontColorScreen(modifier: Modifier, viewModel: BookContentViewModel, uiStateViewModel: UIStateViewModel) {
    ColorPickerScreen(modifier = modifier, colorType = ThemeColorType.FONT, viewModel = viewModel, uiStateViewModel = uiStateViewModel)
}

@Composable
fun BackgroundColorScreen(modifier: Modifier, viewModel: BookContentViewModel,
                          uiStateViewModel: UIStateViewModel) {
    ColorPickerScreen(
        modifier = modifier,
        colorType = ThemeColorType.BACKGROUND,
        viewModel = viewModel,
        uiStateViewModel = uiStateViewModel
    )
}

@Composable
fun ColorPickerScreen(
    modifier: Modifier,
    colorType: ThemeColorType,
    viewModel: BookContentViewModel,
    uiStateViewModel: UIStateViewModel
) {
    Box(
        modifier = modifier
    )
    {
        Column() {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ColorPicker(
                    modifier = Modifier,
                    colorType = colorType,
                    viewModel = viewModel,
                    uiStateViewModel = uiStateViewModel
                )
            }
        }
    }
}