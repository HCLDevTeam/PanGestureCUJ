package com.example.pangestureapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import com.example.pangestureapp.ui.theme.PanGestureAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PanGestureAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PanGestureApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PanGestureAppPreview() {
   PanGestureApp()
}

@Composable
fun PanGestureApp(modifier: Modifier = Modifier) {
    ImageWithPan(modifier = modifier)
}
@Composable
fun ImageWithPan(modifier: Modifier = Modifier) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    // Zooming the image to show the pan effect
    val zoom = 2f

    Image(painter = painterResource(id = R.drawable.sample_image_dog),
        contentDescription = stringResource(R.string.sample_content_description_dog),
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .clipToBounds()
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures(onGesture = { _, pan, _, _ ->
                    offset = offset.calculatePanOffset(pan, zoom, size)
                })
            }
            .graphicsLayer {
                translationX = -offset.x
                translationY = -offset.y
                scaleX = zoom; scaleY = zoom
                transformOrigin = TransformOrigin(0f, 0f)
            })
}

fun Offset.calculatePanOffset(
    pan: Offset, zoom: Float, size: IntSize
): Offset {
    // Calculate the new offset by subtracting the pan from the current offset
    val newOffset = this-pan
    // Calculate the maximum allowed offset in X and Y directions
    // based on the size of the image and the zoom level
    val maxOffsetX = size.width * (zoom-1f)
    val maxOffsetY = size.height * (zoom-1f)
    // Constrain the new offset within the valid range
    return Offset(newOffset.x.coerceIn(0f, maxOffsetX), newOffset.y.coerceIn(0f, maxOffsetY))
}