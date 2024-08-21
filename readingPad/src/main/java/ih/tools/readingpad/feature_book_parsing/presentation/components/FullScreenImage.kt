package ih.tools.readingpad.feature_book_parsing.presentation.components

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntSize

@Composable
fun FullScreenImage(
    imageData: ByteArray,
    onClose: () -> Unit,
) {
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

    // to calculate the height and width of the screen
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp


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

            BoxWithConstraints(
                modifier = Modifier
                    .background(Color.Red)
                    //  .width(200.dp)
                    // .height(700.dp)
                    .aspectRatio(imageWidth / imageHeight)

//                    .pointerInput(Unit) {
//                        detectTransformGestures { centroid, pan, gestureZoom, _ ->
//                            offset =
//                                offset.calculateNewOffset(centroid, pan, zoom, gestureZoom, size)
//                            zoom = maxOf(1f, zoom * gestureZoom)
//                        }
//                    },
//                contentAlignment = Alignment.Center
//
            ) {
                val zoomState =
                    rememberTransformableState { zoomChange, panChange, _ ->
                        zoom = (zoom * zoomChange).coerceIn(1f, 3f)
                        val extraWidth =(zoom -1) * constraints.maxWidth.toFloat()
                        val extraHeight =((zoom -1)* constraints.maxHeight.toFloat())
                        Log.d("PhotoImage", "extraWidth: $extraWidth, extraHeight: $extraHeight")

                        val maxX = extraWidth / 2
                        val maxY = extraHeight / 2
                        offset = Offset(
                           x= (offset.x + zoom * panChange.x).coerceIn(-maxX, maxX),
                           y= (offset.y + zoom * panChange.y).coerceIn(-maxY, maxY)
                        )
                    }

                Image(
                    bitmap = imageBitmap,
                    //  painter = painterResource(id = R.drawable.andre),
                    contentDescription = "Full screen image",
                    modifier = modifier
                        .aspectRatio(imageWidth / imageHeight)
                        .graphicsLayer {
                            translationX = offset.x
                            translationY = offset.y
                            scaleX = zoom
                            scaleY = zoom
                        }
                        .transformable(state = zoomState)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = { tapOffset ->
                                    zoom = if (zoom > 1f) 1f else 2f
                                    Log.d("PhotoImage", "onDoubleTap: zoom =${zoom}, tapOffset = $tapOffset")

                                    offset = calculateDoubleTapOffset(zoom, size, tapOffset)
                                    Log.d("PhotoImage", "onDoubleTap: offset = $offset, size = $size")
                                }
                            )
                        }
                )
            }
        }.onFailure { e ->
            // Display an error message or placeholder
            Log.e("PhotoImage", "Error decoding image: ${e.message}")
            Text("Error loading image")
        }
    }
}


@Composable
private fun Scrim(onClose: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxSize()
            .focusable()
            .clickable {
                onClose()
            }
            .background(Color.DarkGray.copy(alpha = 0.9f))
    )
}


fun calculateDoubleTapOffset(
    zoom: Float,
    size: IntSize,
    tapOffset: Offset
): Offset {
    val imageCenter = Offset(size.width/2f, size.height/2f)
    /** if the tap offset is on the left half of the image, the new offset should shift to the right by the amount of
     the difference between the tap and the center of the image.
     the shift to the right should be a positive Float value
     if the tap offset is on the right half of the image, the new offset should shift to the left by the amount of
     the difference between the tap and the center of the image.
     the shift to the left should be a negative Float value*/
    val newX = imageCenter.x-tapOffset.x

   /** if the tap offset is on the top half of the image, the new offset should shift down by the amount of
     the difference between the tap and the center of the image.
     the shift down should be a positive Float value
     if the tap offset is on the bottom half of the image, the new offset should shift up by the amount of
     the difference between the tap and the center of the image.
     the shift up should be a negative Float value*/
    val newY =  imageCenter.y-tapOffset.y
    /** now we have the new position where the image should be when double clicked which is zoom = 2f,
     so we multiply it by zoom -1 = 2-1 = 1 which doesn't affect the calculation
     to manage the return of the image to the original size when double taped again which is zoom = 1f,
     we need to center it to (0,0) so we multiply it by 1-1 = 0 which always returns 0 making the new offset (0,0)*/
    val newOffset = Offset(newX * (zoom - 1f), newY * (zoom - 1f))

    return  newOffset
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