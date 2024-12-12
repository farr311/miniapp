package org.tindex.miniapp.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TouchAction
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseMove
import com.varabyte.kobweb.compose.ui.modifiers.onTouchEnd
import com.varabyte.kobweb.compose.ui.modifiers.onTouchMove
import com.varabyte.kobweb.compose.ui.modifiers.onTouchStart
import com.varabyte.kobweb.compose.ui.modifiers.outline
import com.varabyte.kobweb.compose.ui.modifiers.touchAction
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.graphics.Canvas2d
import com.varabyte.kobweb.silk.components.graphics.ONE_FRAME_MS_60_FPS
import com.varabyte.kobweb.silk.components.graphics.RenderScope
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.CanvasState
import org.w3c.dom.Path2D
import org.w3c.dom.get
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.round



fun CanvasState.save(block: () -> Unit) {
    save()
    block()
    restore()
}

@Composable
fun Chart(
    dataSets: List<ChartDataSet<*>>,
    offsets: Offsets = Offsets(),
    backgroundColor: Color = Colors.Transparent,
    height: Int = 100,
    width: Int = 100
) {
    var actualWidth by remember { mutableStateOf(0.0) }
    var actualHeight by remember { mutableStateOf(0.0) }

    var canvasStart by remember { mutableStateOf(0.0) }
    var canvasTop by remember { mutableStateOf(0.0) }

    var pointToHighlight by remember { mutableStateOf(-1)}
    var active by remember { mutableStateOf(false)}

    val size = dataSets[0].data.size
    dataSets.forEach {
        if (it.data.size != size) {
            throw RuntimeException("All data sets must have the same data length")
        }
    }

    fun processMoveEvent(x: Int, y: Int) {
        val insideHorizontally = x >= canvasStart && x <= canvasStart + actualWidth
        val insideVertically = y >= canvasTop && y <= canvasTop + actualHeight
        val focused = insideHorizontally && insideVertically
        if (focused) {
            active = true
            pointToHighlight = resolveClosestPoint(x - canvasStart, actualWidth, dataSets[0].data.size, offsets)
        } else {
            pointToHighlight = -1
            active = false
        }
    }


    /*
    * TODO:
    *  1. Add the vertical line for selection
    *  2. Add legend
    *  3. Add lines
    *  4. Add numbers
    * */

    Canvas2d(
        width = if (width == 0) 500 else width,
        height = if (height == 0) 500 else height,
        ref = ref { element ->
            actualWidth = element.getBoundingClientRect().width
            actualHeight = element.getBoundingClientRect().height
            canvasStart = element.getBoundingClientRect().left
            canvasTop = element.getBoundingClientRect().top
        },
        minDeltaMs = ONE_FRAME_MS_60_FPS * 5,
        modifier = Modifier
            .then(if (width != 0) Modifier.width(width.px) else Modifier.fillMaxWidth())
            .then(if (height != 0) Modifier.height(height.px) else Modifier.fillMaxHeight())
            .userSelect(userSelect = UserSelect.None)
            .touchAction(TouchAction.None)
            .outline(0.px)
            .styleModifier {
                property("-webkit-touch-callout", "none")
                property("-webkit-user-select", "none")
                property("-khtml-user-select", "none")
                property("-moz-user-select", "none")
                property("-ms-user-select", "none")
                property("-webkit-tap-highlight-color", "rgba(255, 255, 255, 0)")
            }
            .onMouseMove {
                processMoveEvent(it.x.toInt(), it.y.toInt())
            }.onMouseLeave {
                pointToHighlight = -1
                active = false
            }.onTouchStart {
                active = true
            }
            .onTouchEnd {
                active = false
            }
            .onTouchMove {
                val x = it.touches[it.touches.length - 1]!!.clientX
                val y = it.touches[it.touches.length - 1]!!.clientY
                processMoveEvent(x, y)
            }
            .backgroundColor(backgroundColor)
    ) {
        ctx.save {
            for (dataSet in dataSets) {
                buildLineChart(
                    scope = this,
                    width = actualWidth,
                    height = actualHeight,
                    offsets = offsets,
                    active = active,
                    dataSet = dataSet
                )

                buildPoints(
                    ctx = ctx,
                    dataSet = dataSets[0],
                    width = actualWidth,
                    height = actualHeight,
                    offsets = offsets,
                    highlightedPoint = pointToHighlight,
                )
            }
        }
    }
}

private fun resolveClosestPoint(x: Double, width: Double, size: Int, offsets: Offsets): Int {
    val horizontalScaleFactor = (width - offsets.start - offsets.end) / (size - 1)
    val closestPoint = round(x / horizontalScaleFactor).toInt() - 1
    if (closestPoint > width || closestPoint < 0) {
        return -1
    }
    return closestPoint
}

private fun <T : Number> buildLineChart(
    scope: RenderScope<CanvasRenderingContext2D>,
    width: Double,
    height: Double,
    dataSet: ChartDataSet<T>,
    offsets: Offsets,
    active: Boolean,
) {
    val mappedData = dataSet.mappedData
    val delta = dataSet.delta

    val strokePath = buildPath(mappedData, width, height, offsets, delta)
    val areaPath = buildPath(mappedData, width, height, offsets, delta, false)

    val style = if (active) dataSet.style.active else dataSet.style.default

    val fillStyle = scope.ctx.resolveStyle(style.areaStyle)
    val lineStyle = scope.ctx.resolveStyle(style.lineStyle)

    scope.ctx.clearRect(0.0, 0.0, width, height)
    scope.ctx.translate(0.0, height)
    scope.ctx.scale(scope.width / width, scope.height / height)
    scope.ctx.fillStyle = fillStyle
    scope.ctx.strokeStyle = lineStyle
    scope.ctx.lineWidth = 2.0
    scope.ctx.fill(areaPath)
    scope.ctx.stroke(strokePath)
}

private fun CanvasRenderingContext2D.resolveStyle(style: FillStyle): dynamic {
    return when (style) {
        is CanvasGradientFillStyle -> {
            createLinearGradient(0.0, -100.0, 0.0, 0.0).also {
                for (entry in style.entries) {
                    it.addColorStop(entry.offset, entry.color.toString())
                }
            }
        }
        is PlainFillStyle -> {
            style.color
        }
    }
}

private fun buildPoints(
    ctx: CanvasRenderingContext2D,
    dataSet: ChartDataSet<*>,
    width: Double,
    height: Double,
    highlightedPoint: Int,
    offsets: Offsets,
) {
    val mappedData = dataSet.mappedData
    val delta = dataSet.delta
    val points = dataSet.points

    val horizontalScaleFactor = (width - offsets.start - offsets.end) / (mappedData.size - 1)
    val verticalScaleFactor = (height - offsets.top - offsets.bottom) / delta

    for (i in mappedData.indices) {
        val x = (i).toDouble() * horizontalScaleFactor + offsets.start
        val y = mappedData[i] * verticalScaleFactor - offsets.bottom

        val active = highlightedPoint -1 == i
        val point = if (active) points.active else points.default

        ctx.fillStyle = ctx.resolveStyle(point.areaStyle)
        ctx.strokeStyle = ctx.resolveStyle(point.strokeStyle)


        ctx.beginPath()
        ctx.arc(x, y, point.size, 0.0, 2 * PI)

        ctx.fill()
        ctx.stroke()
    }
}

private fun buildPath(
    mappedData: List<Double>,
    width: Double,
    height: Double,
    offsets: Offsets,
    delta: Double,
    stroke: Boolean = true
): Path2D {
    val horizontalScaleFactor = (width - offsets.start - offsets.end) / (mappedData.size - 1)
    val verticalScaleFactor = (height - offsets.top - offsets.bottom) / delta

    val ctx = Path2D()

    val xStart = offsets.start
    val yStart = mappedData[0] * verticalScaleFactor - offsets.bottom

    if (!stroke) {
        ctx.moveTo(offsets.start, -offsets.bottom)
        ctx.lineTo(xStart, yStart)
    } else {
        ctx.moveTo(xStart, yStart)
    }

    for (i in 0 until mappedData.size - 1) {
        val x = (i + 1).toDouble() * horizontalScaleFactor + offsets.start
        val y = mappedData[i + 1] * verticalScaleFactor - offsets.bottom
        ctx.lineTo(x, y)
    }

    if (!stroke) {
        ctx.lineTo(width - offsets.end, height - offsets.bottom)
        ctx.lineTo(offsets.start, -offsets.bottom)
    }

    return ctx
}

data class ChartDataSet<T : Number>(
    val data: List<T>,
    val style: ChartStyle = ChartStyle(),
    val points: Points = Points()
) {
    val mappedData: List<Double>
    val delta: Double

    init {
        val minPoint = data.minOfOrNull { it.toDouble() }
        val maxPoint = data.maxOfOrNull { it.toDouble() }

        delta = maxPoint!! - minPoint!!
        mappedData = data.map { -abs((it.toDouble() - minPoint)) }
    }

    data class ChartStyle(
        val active: Style = Style(
            lineStyle = PlainFillStyle(Color.rgb(255, 255, 255)),
            areaStyle = PlainFillStyle(Color.rgba(0, 0, 0, 0)),
        ),
        val default: Style = Style(
            lineStyle = PlainFillStyle(Color.rgb(255, 255, 255)),
            areaStyle = PlainFillStyle(Color.rgba(0, 0, 0, 0)),
        )
    )

    data class Style(
        val lineStyle: FillStyle = PlainFillStyle(Color.rgb(255, 255, 255)),
        val areaStyle: FillStyle = PlainFillStyle(Color.rgba(0, 0, 0, 0)),
    )

    data class Points(
        val default: PointStyle = PointStyle(),
        val active: PointStyle = PointStyle()
    )

    data class PointStyle(
        val display: Boolean = false,
        val size: Double = 0.0,
        val strokeStyle: FillStyle = PlainFillStyle(Color.rgba(0, 0, 0, 0)),
        val areaStyle: FillStyle = PlainFillStyle(Color.rgba(0, 0, 0, 0))
    )
}

sealed interface FillStyle

data class PlainFillStyle(val color: Color) : FillStyle

data class CanvasGradientFillStyle(
    val type: GradientType = GradientType.LINEAR,
    val direction: LinearGradient.Direction,
    val entries: List<Entry>
) : FillStyle {
    enum class GradientType {
        LINEAR
    }

    data class Entry(
        val offset: Double,
        val color: Color
    )
}

class Offsets(
    val vertical: Double? = null,
    val horizontal: Double? = null,
    start: Double = 0.0,
    end: Double = 0.0,
    top: Double = 0.0,
    bottom: Double = 0.0
) {
    val start: Double = start
        get() = this.horizontal ?: field
    val end: Double = end
        get() = this.horizontal ?: field

    val top: Double = top
        get() = this.vertical ?: field
    val bottom: Double = bottom
        get() = this.vertical ?: field
}


/*
@Composable
fun TokenBlock() {
    Row() {

    }
}*/
