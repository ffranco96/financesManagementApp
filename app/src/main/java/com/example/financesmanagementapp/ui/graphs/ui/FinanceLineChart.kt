package com.example.financesmanagementapp.ui.graphs.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.financesmanagementapp.domain.model.Record
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.roundToInt

data class ChartPoint(val date: LocalDate, val balance: Double)

private fun buildChartPoints(records: List<Record>): List<ChartPoint> {
    val sorted = records.sortedBy {
        try {
            LocalDateTime.parse(it.date)
        } catch (_: Exception) {
            try {
                LocalDate.parse(it.date).atStartOfDay()
            } catch (_: Exception) {
                LocalDateTime.MIN
            }
        }
    }
    var running = 0.0
    return sorted.map { record ->
        running += record.amount
        val date = try {
            LocalDateTime.parse(record.date).toLocalDate()
        } catch (_: Exception) {
            try {
                LocalDate.parse(record.date)
            } catch (_: Exception) {
                LocalDate.now()
            }
        }
        ChartPoint(date, running)
    }
}

private fun formatARS(value: Double): String {
    val abs = abs(value)
    return when {
        abs >= 1_000_000 -> "${if (value < 0) "-" else ""}${"%.2f".format(abs / 1_000_000)}M"
        abs >= 1_000     -> "${if (value < 0) "-" else ""}${"%.0f".format(abs / 1_000)}K"
        else             -> "${if (value < 0) "-" else ""}${"%.0f".format(abs)}"
    }
}

private val monthFormatter = DateTimeFormatter.ofPattern("MMM")
private val fullFormatter = DateTimeFormatter.ofPattern("dd MMM")

private val BgDark = Color(0xFF0E1117)
private val GreenLine = Color(0xFF00E676)
private val GreenFillTop = Color(0x5500E676)
private val GreenFillBot = Color(0x0000E676)
private val RedLine = Color(0xFFFF5252)
private val RedFillTop = Color(0x55FF5252)
private val RedFillBot = Color(0x00FF5252)
private val ZeroLine = Color(0x33FFFFFF)
private val AxisLabel = Color(0xFF8A9BB0)
private val White = Color(0xFFFFFFFF)

@Composable
fun FinanceLineChart(
    records: List<Record>,
    modifier: Modifier = Modifier,
) {
    val points = remember(records) { buildChartPoints(records) }

    var selectedIndex by remember { mutableStateOf(-1) }

    val drawProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1200),
        label = "drawProgress"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(BgDark, shape = RoundedCornerShape(16.dp))
            .padding(top = 16.dp)
    ) {
        if (points.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay datos suficientes",
                    color = AxisLabel,
                    fontSize = 14.sp
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(56.dp)
            ) {
                if (selectedIndex in points.indices) {
                    val pt = points[selectedIndex]
                    val color = if (pt.balance >= 0) GreenLine else RedLine
                    Column {
                        Text(
                            text = pt.date.format(fullFormatter),
                            color = AxisLabel,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            text = "$ ${formatARS(pt.balance)} ARS",
                            color = color,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    val lastBalance = points.lastOrNull()?.balance ?: 0.0
                    val color = if (lastBalance >= 0) GreenLine else RedLine
                    Column {
                        Text(
                            text = "Balance acumulado",
                            color = AxisLabel,
                            fontSize = 12.sp,
                        )
                        Text(
                            text = "$ ${formatARS(lastBalance)} ARS",
                            color = color,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val pointStep = 28.dp
            val leftPad = 56.dp
            val rightPad = 16.dp
            val canvasWidth = leftPad + (points.size - 1).coerceAtLeast(0) * pointStep + rightPad + pointStep

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                Canvas(
                    modifier = Modifier
                        .width(canvasWidth)
                        .fillMaxHeight()
                        .pointerInput(Unit) {
                            detectTapGestures { offset ->
                                selectedIndex = nearestIndex(
                                    tapX = offset.x,
                                    points = points,
                                    leftPad = leftPad.toPx(),
                                    pointStep = pointStep.toPx()
                                )
                            }
                        }
                ) {
                    drawChart(
                        points = points,
                        drawProgress = drawProgress,
                        selectedIdx = selectedIndex
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawChart(
    points: List<ChartPoint>,
    drawProgress: Float,
    selectedIdx: Int
) {
    if (points.isEmpty()) return

    val leftPad = 56.dp.toPx()
    val rightPad = 16.dp.toPx()
    val topPad = 12.dp.toPx()
    val bottomPad = 36.dp.toPx()

    val chartH = size.height - topPad - bottomPad

    val pointStep = 28.dp.toPx()

    val ninetyDaysAgo = LocalDate.now().minusDays(90)
    val recentPoints = points.filter { !it.date.isBefore(ninetyDaysAgo) }
    val minY = if (recentPoints.isEmpty()) points.minOf { it.balance } else recentPoints.minOf { it.balance }
    val maxY = if (recentPoints.isEmpty()) points.maxOf { it.balance } else recentPoints.maxOf { it.balance }

    val tickSize = 100_000.0
    val tickMin = Math.floor(minY / tickSize) * tickSize
    val tickMax = Math.ceil(maxY / tickSize) * tickSize
    val tickRange = (tickMax - tickMin).coerceAtLeast(tickSize)

    fun toScreenX(index: Int): Float =
        leftPad + index * pointStep

    fun toScreenY(value: Double): Float =
        topPad + chartH * (1f - ((value - tickMin) / tickRange)).toFloat()

    val tickCount = ((tickMax - tickMin) / tickSize).roundToInt().coerceIn(2, 8)
    for (i in 0..tickCount) {
        val value = tickMin + i * tickSize
        val y = toScreenY(value)
        if (y < topPad || y > topPad + chartH) continue

        val lineColor =
            if (value == 0.0) ZeroLine.copy(alpha = 0.6f) else ZeroLine.copy(alpha = 0.2f)
        drawLine(
            color = lineColor,
            start = Offset(leftPad, y),
            end = Offset(size.width - rightPad, y),
            strokeWidth = if (value == 0.0) 1.5.dp.toPx() else 0.7.dp.toPx(),
            pathEffect = if (value == 0.0) null else PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
        )

        drawContext.canvas.nativeCanvas.drawText(
            formatARS(value),
            4f,
            y + 4.dp.toPx(),
            android.graphics.Paint().apply {
                color = android.graphics.Color.argb(160, 138, 155, 176)
                textSize = 9.dp.toPx()
                typeface = android.graphics.Typeface.DEFAULT
                isAntiAlias = true
            }
        )
    }

    var lastMonth = -1
    for (i in points.indices) {
        val x = toScreenX(i)
        val month = points[i].date.monthValue
        if (month != lastMonth) {
            lastMonth = month
            drawContext.canvas.nativeCanvas.drawText(
                points[i].date.format(monthFormatter).uppercase(),
                x,
                size.height - 6.dp.toPx(),
                android.graphics.Paint().apply {
                    color = android.graphics.Color.argb(160, 138, 155, 176)
                    textSize = 10.dp.toPx()
                    textAlign = android.graphics.Paint.Align.CENTER
                    isAntiAlias = true
                }
            )
        }
    }

    clipRect(leftPad, topPad, size.width - rightPad, topPad + chartH) {
        val visibleCount = (points.size * drawProgress).roundToInt().coerceAtLeast(1)

        val linePath = Path()
        for (i in 0 until visibleCount) {
            val x = toScreenX(i)
            val y = toScreenY(points[i].balance)
            if (i == 0) linePath.moveTo(x, y) else linePath.lineTo(x, y)
        }

        val isPositive = (points.lastOrNull()?.balance ?: 0.0) >= 0
        val lineColor = if (isPositive) GreenLine else RedLine
        val fillTop = if (isPositive) GreenFillTop else RedFillTop
        val fillBot = if (isPositive) GreenFillBot else RedFillBot

        val fillPath = Path().apply {
            addPath(linePath)
            val lastX = toScreenX(visibleCount - 1)
            val zeroY = toScreenY(0.0).coerceIn(topPad, topPad + chartH)
            lineTo(lastX, zeroY)
            lineTo(toScreenX(0), zeroY)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(fillTop, fillBot),
                startY = topPad,
                endY = topPad + chartH
            )
        )

        drawPath(
            path = linePath,
            color = lineColor,
            style = Stroke(
                width = 2.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        if (selectedIdx in points.indices && selectedIdx < visibleCount) {
            val sx = toScreenX(selectedIdx)
            val sy = toScreenY(points[selectedIdx].balance)

            drawLine(
                color = White.copy(alpha = 0.25f),
                start = Offset(sx, topPad),
                end = Offset(sx, topPad + chartH),
                strokeWidth = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f))
            )

            drawCircle(
                color = lineColor.copy(alpha = 0.25f),
                radius = 10.dp.toPx(),
                center = Offset(sx, sy)
            )
            drawCircle(
                color = lineColor,
                radius = 4.dp.toPx(),
                center = Offset(sx, sy)
            )
            drawCircle(
                color = BgDark,
                radius = 2.dp.toPx(),
                center = Offset(sx, sy)
            )
        }
    }
}

private fun nearestIndex(
    tapX: Float,
    points: List<ChartPoint>,
    leftPad: Float,
    pointStep: Float
): Int {
    if (points.isEmpty()) return -1
    val rawIndex = ((tapX - leftPad) / pointStep).roundToInt()
    return rawIndex.coerceIn(0, points.size - 1)
}
