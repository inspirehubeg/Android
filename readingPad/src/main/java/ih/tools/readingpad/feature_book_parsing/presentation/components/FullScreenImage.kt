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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import ih.tools.readingpad.R

@Composable
fun FullScreenImage(
    imageData: ByteArray,
    onClose: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Scrim(onClose = onClose)
        PhotoImage(imageData)
    }
}

@Composable
fun PhotoImage(image: ByteArray, modifier: Modifier = Modifier) {
    var zoom by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    if (image.isEmpty()) {
        // Display a placeholder or error message
        Text(stringResource(R.string.image_not_available))
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
                    .aspectRatio(imageWidth / imageHeight)
            ) {
                val zoomState =
                    rememberTransformableState { zoomChange, panChange, _ ->
                        zoom = (zoom * zoomChange).coerceIn(1f, 3f)
                        val extraWidth = (zoom - 1) * constraints.maxWidth.toFloat()
                        val extraHeight = ((zoom - 1) * constraints.maxHeight.toFloat())
                        Log.d("PhotoImage", "extraWidth: $extraWidth, extraHeight: $extraHeight")

                        val maxX = extraWidth / 2
                        val maxY = extraHeight / 2
                        offset = Offset(
                            x = (offset.x + zoom * panChange.x).coerceIn(-maxX, maxX),
                            y = (offset.y + zoom * panChange.y).coerceIn(-maxY, maxY)
                        )
                    }

                Image(
                    bitmap = imageBitmap,
                    //  painter = painterResource(id = R.drawable.andre),
                    contentDescription = stringResource(R.string.full_screen_image),
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
                                    Log.d(
                                        "PhotoImage",
                                        "onDoubleTap: zoom =${zoom}, tapOffset = $tapOffset"
                                    )

                                    offset = calculateDoubleTapOffset(zoom, size, tapOffset)
                                    Log.d(
                                        "PhotoImage",
                                        "onDoubleTap: offset = $offset, size = $size"
                                    )
                                }
                            )
                        }
                )
            }
        }.onFailure { e ->
            // Display an error message or placeholder
            Log.e("PhotoImage", "Error decoding image: ${e.message}")
            Text(stringResource(R.string.error_loading_image))
        }
    }
}


@Composable
fun Scrim(onClose: () -> Unit, modifier: Modifier = Modifier) {
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
    val imageCenter = Offset(size.width / 2f, size.height / 2f)

    /** if the tap offset is on the left half of the image, the new offset should shift to the right by the amount of
    the difference between the tap and the center of the image.
    the shift to the right should be a positive Float value
    if the tap offset is on the right half of the image, the new offset should shift to the left by the amount of
    the difference between the tap and the center of the image.
    the shift to the left should be a negative Float value*/
    val newX = imageCenter.x - tapOffset.x

    /** if the tap offset is on the top half of the image, the new offset should shift down by the amount of
    the difference between the tap and the center of the image.
    the shift down should be a positive Float value
    if the tap offset is on the bottom half of the image, the new offset should shift up by the amount of
    the difference between the tap and the center of the image.
    the shift up should be a negative Float value*/
    val newY = imageCenter.y - tapOffset.y

    /** now we have the new position where the image should be when double clicked which is zoom = 2f,
    so we multiply it by zoom -1 = 2-1 = 1 which doesn't affect the calculation
    to manage the return of the image to the original size when double taped again which is zoom = 1f,
    we need to center it to (0,0) so we multiply it by 1-1 = 0 which always returns 0 making the new offset (0,0)*/
    val newOffset = Offset(newX * (zoom - 1f), newY * (zoom - 1f))

    return newOffset
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