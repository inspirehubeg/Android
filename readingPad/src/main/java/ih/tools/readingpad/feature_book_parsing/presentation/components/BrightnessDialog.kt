package ih.tools.readingpad.feature_book_parsing.presentation.components

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ih.tools.readingpad.ui.UIStateViewModel

@Composable
fun BrightnessDialog(
    uiStateViewModel: UIStateViewModel
){

    val sliderValue by uiStateViewModel.brightnessLevel.collectAsState()

    val window = (LocalContext.current as Activity).window

    //outer transparent box to allow the custom positioning of the slider
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp)
            .clickable(onClick = {
                uiStateViewModel.showDialog(null)
            })
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(16.dp)
                )
                .align(Alignment.BottomCenter)
        ) {
            Slider(
                value = sliderValue,
                onValueChange = { uiStateViewModel.setBrightnessValue(it)
                    val layoutParams = window.attributes
                    layoutParams.screenBrightness = it
                    window.attributes = layoutParams
                },
                valueRange = 0f..1f,
                steps = 24
            )
        }
    }
}