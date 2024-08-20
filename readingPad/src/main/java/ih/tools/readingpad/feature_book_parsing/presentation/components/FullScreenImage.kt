package ih.tools.readingpad.feature_book_parsing.presentation.components

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun FullScreenImage(
    imageData: ByteArray,
    onClose: () -> Unit,
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val imageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size).asImageBitmap()
    val imageWidth = imageBitmap.width
    val imageHeight = imageBitmap.height
    val density = LocalDensity.current
    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp

    val screenWidth = with(density) { screenWidthDp.toPx() }
    val screenHeight = with(density) { screenHeightDp.toPx() }

    Box(

        modifier = Modifier
            .fillMaxSize(),
        //.padding(bottom = 100.dp, top = 100.dp),
        //.background(Color.Black.copy(alpha = 0.5f)) // Semi-transparent background
        contentAlignment = Alignment.Center,
    ) {
        Scrim(onClose = onClose)

        PhotoImage(imageData)


//        IconButton(onClick = onClose, modifier = Modifier.align(Alignment.TopEnd)) {
//            Icon(Icons.Filled.Close, contentDescription = "Close")
//        }
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//                .then(if (scale > 1f)
//                    Modifier.offset {
//                    IntOffset(offset.x
//                        .roundToInt()
//                        .let { value ->
//                            val min = (-(imageWidth * scale - screenWidth) / 2).roundToInt()
//                            val max = ((imageWidth * scale - screenWidth) / 2).roundToInt()
//                            if (min > max) value.coerceIn(max, min) else value.coerceIn(
//                                min,
//                                max
//                            )
//                        },
//                        offset.y
//                            .roundToInt()
//                            .let { value ->
//                                val min = (-(imageHeight * scale - screenHeight) / 2).roundToInt()
//                                val max = ((imageHeight * scale - screenHeight) / 2).roundToInt()
//                                if (min > max) value.coerceIn(max, min) else value.coerceIn(
//                                    min,
//                                    max
//                                )
//                            }
//                    )
//                }else Modifier)
//                .graphicsLayer(
//                    scaleX = scale,
//                    scaleY = scale
//                )
//                .pointerInput(Unit) {
//                    detectTransformGestures { centroid, pan, zoom, rotation ->
//                        scale = (scale * zoom).coerceIn(1f, 5f) // Limit zoom between 1x and 5x
//                        // offset = offset + pan
//                        if (scale > 1f) {
////                            val maxX = (imageWidth * scale - screenWidth) / 2
////                            val maxY = (imageHeight * scale - screenHeight) / 2
////                            offset = Offset(
////
////                                (offset.x + pan.x).coerceIn(-maxX, maxX),
////                                (offset.y + pan.y).coerceIn(-maxY,maxY)
////                            )
//                            offset = Offset(
//                                (offset.x + pan.x).let { value ->
//                                    val min = (-(imageWidth * scale - screenWidth) / 2)
//                                    val max = ((imageWidth * scale - screenWidth) / 2)
//                                    if (min > max) value.coerceIn(max, min) else value.coerceIn(
//                                        min,
//                                        max
//                                    )
//                                },
//                                (offset.y + pan.y).let { value ->
//                                    val min = (-(imageHeight * scale - screenHeight) / 2)
//                                    val max = ((imageHeight * scale - screenHeight) / 2)
//                                    if (min > max) value.coerceIn(max, min) else value.coerceIn(
//                                        min,
//                                        max
//                                    )
//                                }
//                            )
//                        }
//                    }
//                }
//        ) {
//
//            Image(
//                bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
//                    .asImageBitmap(),
//                contentDescription = "Full screen image",
//                modifier = Modifier.fillMaxSize()
//            )
//        }
    }
}

@Composable
fun PhotoImage(image: ByteArray, modifier: Modifier = Modifier) {
    var zoom by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    if (image.isEmpty()) {
        // Display a placeholder or error message
        Text("Image not available")
    } else {
        runCatching {
            BitmapFactory.decodeByteArray(image, 0, image.size)
        }.onSuccess { bitmap ->
            val imageBitmap = bitmap.asImageBitmap()
            val imageWidth = imageBitmap.width.toFloat()
            val imageHeight = imageBitmap.height.toFloat()
            val zoomState = rememberTransformableState { zoomChange, panChange, rotationChange ->
                zoom = (zoom * zoomChange).coerceIn(1f, 5f)
            }
            Image(
                bitmap = BitmapFactory.decodeByteArray(image, 0, image.size).asImageBitmap(),
                //  painter = painterResource(id = R.drawable.andre),
                contentDescription = "Full screen image",
                modifier = modifier
                    .clipToBounds()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = { tapOffset ->
                                zoom = if (zoom > 1f) 1f else 2f
                                offset = calculateDoubleTapOffset(zoom, size, tapOffset)
                            }
                        )
                    }
                    .pointerInput(Unit) {
                        detectTransformGestures { centroid, pan, gestureZoom, _ ->
                            offset =
                                offset.calculateNewOffset(centroid, pan, zoom, gestureZoom, size)
                            zoom = maxOf(1f, zoom * gestureZoom)
                        }
                    }
                    .graphicsLayer {
                        translationX = -offset.x * zoom
                        translationY = -offset.y * zoom
                        scaleX = zoom
                        scaleY = zoom
                        transformOrigin = TransformOrigin(0f, 0f)
                    }
                    .aspectRatio(imageWidth / imageHeight)
                    .transformable(zoomState)
            )
        } .onFailure {e->
            // Display an error message or placeholder
            Log.e("PhotoImage", "Error decoding image: ${e.message}")
            Text("Error loading image")
        }
    }
}

@Composable
private fun Scrim(onClose: () -> Unit, modifier: Modifier = Modifier) {
    val strClose = "Close"
    Box(
        modifier
            .fillMaxSize()
            .pointerInput(onClose) { detectTapGestures { onClose() } }
            .semantics {
                onClick(strClose) { onClose(); true }
            }
            .focusable()
            .onKeyEvent {
                if (it.key == Key.Escape) {
                    onClose(); true
                } else {
                    false
                }
            }
            .background(Color.DarkGray.copy(alpha = 0.9f))
    )
}


fun calculateDoubleTapOffset(
    zoom: Float,
    size: IntSize,
    tapOffset: Offset
): Offset {
    val newOffset = Offset(tapOffset.x, tapOffset.y)
    return Offset(
        newOffset.x.coerceIn(0f, (size.width / zoom) * (zoom - 1f)),
        newOffset.y.coerceIn(0f, (size.height / zoom) * (zoom - 1f))
    )
}

fun Offset.calculateNewOffset(
    centroid: Offset,
    pan: Offset,
    zoom: Float,
    gestureZoom: Float,
    size: IntSize
): Offset {
    val newScale = maxOf(1f, zoom * gestureZoom)
    val newOffset = (this + centroid / zoom) -
            (centroid / newScale + pan / zoom)
    return Offset(
        newOffset.x.coerceIn(0f, (size.width / zoom) * (zoom - 1f)),
        newOffset.y.coerceIn(0f, (size.height / zoom) * (zoom - 1f))
    )
}