package com.example.financesmanagementapp.ui.graphs.ui

import android.content.res.Configuration
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.financesmanagementapp.R
import com.example.financesmanagementapp.ui.graphs.model.CategoryTotal
import com.example.financesmanagementapp.ui.theme.FinancesManagementAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsScreen(
    navController: NavController,
    viewModel: ChartsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    ChartsScreenContent(
        uiState = uiState,
        onBackClick = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChartsScreenContent(
    uiState: ChartsUiState,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gráficos") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isEmpty) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay datos cargados",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            val total = uiState.categoryTotals.sumOf { kotlin.math.abs(it.totalAmount) }
            val totalLabel = "%.2f".format(total)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                DonutChart(
                    categories = uiState.categoryTotals,
                    totalLabel = totalLabel,
                    modifier = Modifier.size(240.dp),
                )

                Spacer(modifier = Modifier.height(28.dp))

                val left = uiState.categoryTotals.filterIndexed { i, _ -> i % 2 == 0 }
                val right = uiState.categoryTotals.filterIndexed { i, _ -> i % 2 == 1 }
                val rows = maxOf(left.size, right.size)

                Column(modifier = Modifier.fillMaxWidth()) {
                    repeat(rows) { row ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            left.getOrNull(row)?.let { cat ->
                                LegendItem(category = cat, modifier = Modifier.weight(1f))
                            } ?: Spacer(Modifier.weight(1f))
                            Spacer(Modifier.width(24.dp))
                            right.getOrNull(row)?.let { cat ->
                                LegendItem(category = cat, modifier = Modifier.weight(1f))
                            } ?: Spacer(Modifier.weight(1f))
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DonutChart(
    categories: List<CategoryTotal>,
    totalLabel: String,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 36.dp,
    gapDegrees: Float = 3f,
) {
    val nonZeroCategories = categories.filter { it.totalAmount != 0.0 }
    val total = nonZeroCategories.sumOf { kotlin.math.abs(it.totalAmount).toDouble() }.toFloat()

    var animationPlayed by remember { mutableStateOf(false) }

    val animScale by animateFloatAsState(
        targetValue = if (animationPlayed) 1f else 0f,
        animationSpec = tween(durationMillis = 900, easing = EaseOutCubic),
    )

    val animRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f,
        animationSpec = tween(durationMillis = 900, easing = EaseOutCubic),
    )

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    val context = LocalContext.current

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .rotate(animRotation)
        ) {
            val baseStrokePx = strokeWidth.toPx()
            val scaledStrokePx = baseStrokePx * animScale
            val stroke = Stroke(width = scaledStrokePx, cap = StrokeCap.Square)
            val maxDiameter = minOf(size.width, size.height)
            val scaledDiameter = maxDiameter * animScale
            val topLeft = Offset(
                x = (size.width - scaledDiameter) / 2f + scaledStrokePx / 2,
                y = (size.height - scaledDiameter) / 2f + scaledStrokePx / 2,
            )
            val arcSize = Size(
                scaledDiameter - scaledStrokePx,
                scaledDiameter - scaledStrokePx,
            )

            var startAngle = -90f

            nonZeroCategories.forEach { cat ->
                val fraction = kotlin.math.abs(cat.totalAmount).toFloat() / total
                val sweep = fraction * 360f - gapDegrees

                drawArc(
                    color = Color(context.getColor(cat.colorResId)),
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = stroke,
                )
                startAngle += fraction * 360f
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = totalLabel,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun LegendItem(category: CategoryTotal, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(
                    color = Color(context.getColor(category.colorResId)),
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = category.categoryName,
            fontSize = 12.sp,
            maxLines = 1,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "%.2f".format(category.totalAmount),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Preview(
    name = "Empty state",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun ChartsScreenEmptyPreview() {
    FinancesManagementAppTheme {
        ChartsScreenContent(
            uiState = ChartsUiState(isEmpty = true),
            onBackClick = {}
        )
    }
}

@Preview(
    name = "With data",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun ChartsScreenDataPreview() {
    val totals = listOf(
        CategoryTotal("Comida y alimentos", -150.0, R.color.categ_color_food),
        CategoryTotal("Salud", -50.0, R.color.categ_color_health),
        CategoryTotal("Salario", 200.0, R.color.categ_color_salary),
    )
    FinancesManagementAppTheme {
        ChartsScreenContent(
            uiState = ChartsUiState(categoryTotals = totals, isEmpty = false),
            onBackClick = {}
        )
    }
}

@Preview(
    name = "Empty state - dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ChartsScreenEmptyDarkPreview() {
    FinancesManagementAppTheme {
        ChartsScreenContent(
            uiState = ChartsUiState(isEmpty = true),
            onBackClick = {}
        )
    }
}
