package ih.tools.readingpad.feature_book_parsing.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Rotate90DegreesCcw
import androidx.compose.material.icons.filled.Rotate90DegreesCw
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ih.tools.readingpad.feature_book_parsing.presentation.BookContentViewModel
import ih.tools.readingpad.ui.UIStateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenImageTopBar(
    viewModel: BookContentViewModel,
    uiStateViewModel: UIStateViewModel
) {

    Surface(modifier = Modifier.height(48.dp)) {
        CenterAlignedTopAppBar(
            colors = TopAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                scrolledContainerColor = Color.Transparent,
                navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            title = {
                // this is the middle part of the top bar
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    IconButton(onClick = {
                        if (uiStateViewModel.imageRotation.value == 0f || uiStateViewModel.imageRotation.value == 90f) {
                            uiStateViewModel.setImageRotation(uiStateViewModel.imageRotation.value - 90f)
                        }
                        //Log.d("FullScreenImage", "rotation: $rotation")
                    }) {
                        Icon( Icons.Default.Rotate90DegreesCcw, contentDescription = "Rotate Left")
                    }

                    IconButton(onClick = {
                        if (uiStateViewModel.imageRotation.value == 0f || uiStateViewModel.imageRotation.value == -90f) {
                            uiStateViewModel.setImageRotation(uiStateViewModel.imageRotation.value + 90f)
                        }
                    }) {
                        Icon( Icons.Default.Rotate90DegreesCw, contentDescription = "Rotate Right")
                    }

                }

            },
            actions = {
                // this is the right part of the top bar
                IconButton(onClick = {
//                    viewModel.setImageRotation(0f)
//                    viewModel.setShowFullScreenImage(false)
//                    viewModel.onImageClick(null) // Reset the clicked image state
                    uiStateViewModel.setImageRotation(0f)
                    uiStateViewModel.showScreen(null)
                    uiStateViewModel.onImageClick(null)
                }) {
                    Icon(Icons.Default.Close, contentDescription = "close image")
                }
            }
        )
    }

    }