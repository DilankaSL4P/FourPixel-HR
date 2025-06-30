package com.fourpixel.fourpixelhrapplication.ui.theme

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ArcSpinner(
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
    color1: Color = Color(0xFFFFA000),
    color2: Color = Color(0xFFFFC107)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "double_arc_spinner_transition")


    val rotation1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing)
        ),
        label = "rotation1"
    )


    val rotation2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing)
        ),
        label = "rotation2"
    )

    Canvas(modifier = modifier.size(size)) {

        drawArc(
            color = color1,
            startAngle = rotation1,
            sweepAngle = 160f,
            useCenter = false,
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
        )

        drawArc(
            color = color2,
            startAngle = rotation2,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}