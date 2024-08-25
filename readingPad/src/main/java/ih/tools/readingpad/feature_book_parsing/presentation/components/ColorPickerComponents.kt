package ih.tools.readingpad.feature_book_parsing.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import ih.tools.readingpad.R
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.feature_note_color.domain.model.ThemeColor
import ih.tools.readingpad.feature_note_color.domain.model.ThemeColorType

@Composable
fun ColorPicker(
    viewModel: BookContentViewModel,
    modifier: Modifier,
    colorType: ThemeColorType
) {
    val colorPickerController = rememberColorPickerController()

    val savedFontColors by viewModel.savedFontColors.observeAsState(emptyList())
    val savedBackgroundColors by viewModel.savedBackgroundColors.observeAsState(emptyList())

    var initialColor = Color.Green
    var onColorChange: (ColorEnvelope) -> Unit = {}

    val currentThemeFontColor by viewModel.currentThemeFontColor.observeAsState()
    val currentThemeBackgroundColor by viewModel.currentThemeBackgroundColor.observeAsState()

    val (showDeleteAllDialog, setShowDeleteAllDialog) = remember { mutableStateOf(false) }
    val (showDeleteColorDialog, setShowDeleteColorDialog) = remember { mutableStateOf(false) }
    val (colorToDelete, setColorToDelete) = remember { mutableStateOf<ThemeColor?>(null) }

    if (colorType == ThemeColorType.FONT){
        initialColor = Color(currentThemeFontColor!!)
        onColorChange = {colorEnvelope ->
          viewModel.setCurrentThemeFontColor(colorEnvelope.color.toArgb())
        }
    } else {
        initialColor= Color(currentThemeBackgroundColor!!)
        onColorChange = {colorEnvelope ->
            viewModel.setCurrentThemeBackgroundColor(colorEnvelope.color.toArgb())
        }
    }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp.dp


    //the entire color picker box containing the color picker and the saved colors
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            //Row contains the pre defined colors, the color picker ,
            // the brightness slider and the save color button
            Row(
                modifier = Modifier
                    //.weight(5f)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                //Pre-defined colors
                Column(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(screenWidth * .7f)
                        .weight(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Top
                    ) {
                        if (colorType == ThemeColorType.FONT) {
                            items(ThemeColor.preDefinedFontColors) { color ->
                                val colorInt = color.toArgb()
                                Box(modifier = Modifier
                                    //.size(25.dp)
                                    .height(50.dp)
                                    .width(40.dp)
                                    .padding(end = 4.dp, top = 4.dp, bottom = 4.dp)
                                    .clip(RoundedCornerShape(10))
                                    .background(color)
                                    .border(
                                        width = 2.dp,
                                        color = if (viewModel.currentThemeFontColor.value == colorInt) {
                                            MaterialTheme.colorScheme.onSurface
                                        } else Color.Transparent,
                                        shape = RoundedCornerShape(10)
                                    )
                                    .clickable {
                                        //this changes the current selected color to show a border around the box
                                        viewModel.setCurrentThemeFontColor(colorInt)
                                    })

                            }
                        } else {
                            items(ThemeColor.preDefinedBackgroundColors) { color ->
                                val colorInt = color.toArgb()
                                Box(modifier = Modifier
                                    //.size(25.dp)
                                    .height(50.dp)
                                    .width(40.dp)
                                    .padding(end = 4.dp, top = 4.dp, bottom = 4.dp)
                                    .clip(RoundedCornerShape(10))
                                    .background(color)
                                    .border(
                                        width = 2.dp,
                                        color = if (viewModel.currentThemeBackgroundColor.value == colorInt) {
                                            MaterialTheme.colorScheme.onSurface
                                        } else Color.Transparent,
                                        shape = RoundedCornerShape(10)
                                    )
                                    .clickable {
                                        //this changes the current selected color to show a border around the box
                                        viewModel.setCurrentThemeBackgroundColor(colorInt)
                                    })

                            }
                        }
                    }
                }
                //Color picker
                Column(
                    modifier = Modifier.weight(4f)
                ) {
                    // color picker
                    HsvColorPicker(
                        initialColor = initialColor,
                        modifier = Modifier
                            //.fillMaxWidth()
                            //.background(Color.Red)
                            .width(screenWidth * .7f)
                            .height(screenWidth * .7f)
                            // .weight(3f)
                            .padding(8.dp),
                        controller = colorPickerController,
                        onColorChanged = onColorChange
                    )
                    //brightness
                    BrightnessSlider(
                        initialColor = initialColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 4.dp, start = 4.dp, end = 8.dp)
                            .height(15.dp),
                        controller = colorPickerController
                    )
                    //to keep both items in the center of the row
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, start = 8.dp, end = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // this row contains the color preview and the save color button
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // color tile
                            AlphaTile(
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(50.dp),
                                controller = colorPickerController
                            )
                            // save color
                            IconButton(modifier = Modifier
                                .width(50.dp)
                                .height(40.dp),
                                onClick = {
                                    viewModel.addThemeColor(
                                        argb = colorPickerController.selectedColor.value.toArgb(),
                                        type = colorType
                                    )
                                }) {
                                Icon(
                                    imageVector = Icons.Filled.SaveAlt,
                                    contentDescription = "save color"
                                )
                            }
                        }
                    }
                }
            }


            //saved colors and delete all Icon
            Row(
                modifier = Modifier
                    //.weight(1f)
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
            ) {
                LazyRow(
                    modifier = Modifier
                        .weight(7f)
                        .padding(start = 8.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    if (colorType == ThemeColorType.FONT) {
                        items(savedFontColors.reversed()) { color ->
                            val colorInt: Int = color.argb
                            Box(
                                modifier = Modifier
                                    //.size(25.dp)
                                    .height(50.dp)
                                    .width(50.dp)
                                    .padding(end = 8.dp)
                                    .clip(RoundedCornerShape(10))
                                    .background(Color(colorInt))
                                    .border(
                                        width = 2.dp,
                                        color = if (viewModel.currentThemeFontColor.value == colorInt) {
                                            MaterialTheme.colorScheme.onSurface
                                        } else Color.Transparent,
                                        shape = RoundedCornerShape(10)
                                    )
                                    .clickable {
                                        viewModel.setCurrentThemeFontColor(colorInt)
                                    },

                                ) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(15.dp)
                                        .clickable {
                                            setColorToDelete(color)
                                            // warning before deleting note
                                            setShowDeleteColorDialog(true)
                                        },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Cancel,
                                        contentDescription = "Delete Color"
                                    )
                                }
                            }
                        }
                    } else {
                        items(savedBackgroundColors.reversed()) { color ->
                            val colorInt: Int = color.argb
                            Box(
                                modifier = Modifier
                                    //.size(25.dp)
                                    .height(50.dp)
                                    .width(50.dp)
                                    .padding(end = 8.dp)
                                    .clip(RoundedCornerShape(10))
                                    .background(Color(colorInt))
                                    .border(
                                        width = 2.dp,
                                        color = if (viewModel.currentThemeBackgroundColor.value == colorInt) {
                                            MaterialTheme.colorScheme.onSurface
                                        } else Color.Transparent,
                                        shape = RoundedCornerShape(10)
                                    )
                                    .clickable {
                                        viewModel.setCurrentThemeBackgroundColor(colorInt)
                                    },

                                ) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(15.dp)
                                        .clickable {
                                            setColorToDelete(color)
                                            // warning before deleting note
                                            setShowDeleteColorDialog(true)
                                        },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Cancel,
                                        contentDescription = "Delete Color"
                                    )
                                }
                            }
                        }
                    }
                }
                IconButton(modifier = Modifier.weight(1f), onClick = {
                    // show a warning msg that all the saved colors will be deleted
                    setShowDeleteAllDialog(true)
                }) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = "Delete All Saved Colors"
                    )
                }
            }
        }
    }

    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { setShowDeleteAllDialog(false) },
            title = { Text("Warning") },
            text = { Text("Delete all saved colors?") },
            confirmButton = {
                Button(onClick = {
                    if (colorType == ThemeColorType.FONT){
                        viewModel.deleteAllThemeFontColors()
                    } else {
                        viewModel.deleteAllThemeBackgroundColors()
                    }
                    setShowDeleteAllDialog(false)
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = { setShowDeleteAllDialog(false) }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
    if (showDeleteColorDialog) {
        AlertDialog(
            onDismissRequest = { setShowDeleteColorDialog(false) },
            title = { Text(text = "Warning") },
            text = { Text("Delete this color?") },
            confirmButton = {
                Button(onClick = {
                    colorToDelete?.let { viewModel.deleteThemeColor(it) }
                    setColorToDelete(null)
                    setShowDeleteColorDialog(false)
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(onClick = {
                    setColorToDelete(null)
                    setShowDeleteColorDialog(false)
                }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}








