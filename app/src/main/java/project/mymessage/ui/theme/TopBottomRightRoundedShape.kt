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

class TopBottomRightRoundedShape(
        val topRight: CornerSize,
        val bottomRight: CornerSize
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
                moveTo(0f, 0f)

                // Draw a line to the top-right corner
                lineTo(width - topRight.toPx(size, density), 0f)

                // Draw the top-right rounded corner
                arcTo(
                    rect = Rect(
                        left = width - 2 * topRight.toPx(size, density),
                        top = 0f,
                        right = width,
                        bottom = 2 * topRight.toPx(size, density)
                    ),
                    startAngleDegrees = 270f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Draw a line down the right side
                lineTo(width, height - bottomRight.toPx(size, density))

                // Draw the bottom-right rounded corner
                arcTo(
                    rect = Rect(
                        left = width - 2 * bottomRight.toPx(size, density),
                        top = height - 2 * bottomRight.toPx(size, density),
                        right = width,
                        bottom = height
                    ),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )

                // Draw a line to the bottom-left corner
                lineTo(0f, height)

                // Close the path by drawing a line back to the start
                close()
            }
            return Outline.Generic(path)

            // Alternatively, for simpler cases (where the arcTo path calculation might be complex)
            // you can use RoundRect and manually clip corners:
            //  return Outline.RoundRect(
            //     RoundRect(
            //         rect = Rect(0f, 0f, size.width, size.height),
            //         topLeft = CornerSize(0.dp).toPx(size, density), // No rounding
            //         topRight = topRight.toPx(size, density),          // Top-right rounding
            //         bottomRight = bottomRight.toPx(size, density),    // Bottom-right rounding
            //         bottomLeft = CornerSize(0.dp).toPx(size, density)  // No rounding
            //     )
            // )
        }
    }

    // Helper function for convenience (optional)
    fun Modifier.topAndBottomRightRounded(topRightRadius: Dp, bottomRightRadius: Dp) =
        this.clip(
            TopBottomRightRoundedShape(
                topRight = CornerSize(topRightRadius),
                bottomRight = CornerSize(bottomRightRadius)
            )
        )

