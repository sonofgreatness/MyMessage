package project.mymessage.ui.theme

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class TopBottomLeftRoundedShape(
    val topLeft: CornerSize,
    val bottomLeft: CornerSize
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val width = size.width
        val height = size.height

        val path = Path().apply {
            // Start at the top-left corner
            moveTo(0f, 0f + topLeft.toPx(size, density))

            // Draw the top-left rounded corner
            arcTo(
                rect = Rect(
                    left = 0f,
                    top = 0f,
                    right = 2 * topLeft.toPx(size, density),
                    bottom = 2 * topLeft.toPx(size, density)
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // Draw a line to the top-right corner
            lineTo(width, 0f)

            // Draw a line down the right side
            lineTo(width, height)

            // Draw a line to the bottom-left corner
            lineTo(bottomLeft.toPx(size, density), height)

            // Draw the bottom-left rounded corner
            arcTo(
                rect = Rect(
                    left = 0f,
                    top = height - 2 * bottomLeft.toPx(size, density),
                    right = 2 * bottomLeft.toPx(size, density),
                    bottom = height
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            // Close the path by drawing a line back to the start
            close()
        }
        return Outline.Generic(path)
    }
}

// Helper function for convenience (optional)
fun Modifier.topAndBottomLeftRounded(topLeftRadius: Dp, bottomLeftRadius: Dp) =
    this.clip(
        TopBottomLeftRoundedShape(
            topLeft = CornerSize(topLeftRadius),
            bottomLeft = CornerSize(bottomLeftRadius)
        )
    )