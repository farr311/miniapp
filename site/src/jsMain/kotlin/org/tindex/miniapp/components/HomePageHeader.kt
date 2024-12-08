package org.tindex.miniapp.components

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.background
import com.varabyte.kobweb.compose.css.height
import com.varabyte.kobweb.compose.css.width
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.outlineColor
import com.varabyte.kobweb.compose.ui.modifiers.outlineStyle
import com.varabyte.kobweb.compose.ui.modifiers.outlineWidth
import com.varabyte.kobweb.silk.components.graphics.Canvas2d
import com.varabyte.kobweb.silk.components.graphics.ONE_FRAME_MS_60_FPS
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.shapes.Path
import com.varabyte.kobweb.silk.theme.shapes.PolygonF
import com.varabyte.kobweb.silk.theme.shapes.Shape
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Canvas
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.CanvasState
import org.w3c.dom.Path2D
import kotlin.js.Date
import kotlin.math.PI
import kotlin.random.Random


@Composable
fun HomePageHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.px)
    ) {
        SpanText(
            text = "Total balance",
            modifier = Modifier
                .color(Color.rgb(0xFFFFFF))
                .fontSize(14.px)
                .lineHeight(20.px)
                .fontWeight(FontWeight.Medium)
        )
        Div {
            SpanText(
                text = "$",
                modifier = Modifier
                    .color(Color.rgb(0x888888))
                    .fontSize(30.px)
                    .lineHeight(36.px)
                    .fontWeight(FontWeight.Bold)
            )
            SpanText(
                text = "10.",
                modifier = Modifier
                    .color(Color.rgb(0xFFFFFF))
                    .fontSize(30.px)
                    .lineHeight(36.px)
                    .fontWeight(FontWeight.Bold)
            )
            SpanText(
                text = "00",
                modifier = Modifier
                    .color(Color.rgb(0xFFFFFF))
                    .fontSize(20.px)
                    .lineHeight(28.px)
                    .fontWeight(FontWeight.Bold)
            )
            ///Text("Total balance")
            //Text("Total balance")
        }
        SpanText(
            text = "+3.12",
            modifier = Modifier
                .color(Color.rgb(0x888888))
                .fontSize(16.px)
                .lineHeight(24.px)
                .fontWeight(FontWeight.Medium)
        )
    }
}

@Composable
fun Chart() {
    val data: MutableList<Pair<Float, Float>> = mutableListOf()
    data.add(Pair(0f, 0f))
    for (i in 1..200) {

        //Math
        data.add(Pair(i.toFloat(), Random.nextFloat() * 100))
    }
    data.add(Pair(0f, 200f))

    val s = PolygonF(
        *data.toTypedArray()
    )

   /* Box(
        modifier = Modifier.fillMaxWidth()
            .height(500.px)
            //.border(width = 2.px, color = Color.rgb(0xF7931A), style = LineStyle.Solid)
            .backgroundColor(Color.rgb(100, 100, 100))
            .outlineStyle(LineStyle.Solid)
            .outlineColor(Color.rgb(0xF7931A))
            .outlineWidth(20.px)
            .clip(s)

    )*/

    Canvas2d(300, 300, minDeltaMs = ONE_FRAME_MS_60_FPS * 5) {
        val date = Date()
        val r = 150.0

        // Let's be a little lazy and use some colors from the palette which is already color mode aware
        val colorBorder = Color.rgb(255, 0, 0)
        val colorTicks = Color.rgb(255, 0, 0)
        val colorHourHand = Color.rgb(255, 0, 0)
        val colorMinuteHand = Color.rgb(255, 0, 0)
        val colorSecondHand = Color.rgb(255, 0, 0)

        ctx.save {
            ctx.strokeStyle = colorTicks
            ctx.fillStyle = Color.rgb(100, 100, 100)

            ctx.clearRect(0.0, 0.0, width.toDouble(), height.toDouble())
            ctx.translate(r, r)
            ctx.lineWidth = 8.0

            // Hour marks
            ctx.save {
                for (i in 0 until 12) {
                    ctx.beginPath()
                    ctx.rotate(PI / 6)
                    ctx.moveTo(100.0, 0.0)
                    ctx.lineTo(120.0, 0.0)
                    ctx.stroke()
                }
            }

            // Minute marks
            ctx.save {
                ctx.lineWidth = 5.0
                for (i in 0 until 60) {
                    if (i % 5 != 0) {
                        ctx.beginPath()
                        ctx.moveTo(117.0, 0.0)
                        ctx.lineTo(120.0, 0.0)
                        ctx.stroke()
                    }
                    ctx.rotate(PI / 30)
                }
            }

            val sec = date.getSeconds()
            val min = date.getMinutes()
            val hr = date.getHours() % 12

            // Hour hand
            ctx.save {
                ctx.strokeStyle = colorHourHand
                ctx.rotate(hr * (PI / 6) + (PI / 360) * min + (PI / 21600) * sec)
                ctx.lineWidth = 14.0
                ctx.beginPath()
                ctx.moveTo(-20.0, 0.0)
                ctx.lineTo(80.0, 0.0)
                ctx.stroke()
            }

            // write Minutes
            ctx.save {
                ctx.strokeStyle = colorMinuteHand
                ctx.rotate((PI / 30) * min + (PI / 1800) * sec)
                ctx.lineWidth = 10.0
                ctx.beginPath()
                ctx.moveTo(-28.0, 0.0)
                ctx.lineTo(112.0, 0.0)
                ctx.stroke()
            }

            // Write seconds
            ctx.save {
                ctx.rotate(sec * PI / 30)
                ctx.strokeStyle = colorSecondHand
                ctx.fillStyle = colorSecondHand
                ctx.lineWidth = 6.0
                ctx.beginPath()
                ctx.moveTo(-30.0, 0.0)
                ctx.lineTo(83.0, 0.0)
                ctx.stroke()
                ctx.beginPath()
                ctx.arc(0.0, 0.0, 10.0, 0.0, PI * 2, true)
                ctx.fill()

                // The loop at the end of the second hand
                ctx.beginPath()
                ctx.arc(95.0, 0.0, 10.0, 0.0, PI * 2, true)
                ctx.stroke()
                ctx.fillStyle = Color.argb(0, 0, 0, 0)
                ctx.arc(0.0, 0.0, 3.0, 0.0, PI * 2, true)
                ctx.fill()
            }

            // The outer circle that frames the clock
            ctx.beginPath()
            ctx.lineWidth = 14.0
            ctx.strokeStyle = colorBorder
            ctx.arc(0.0, 0.0, 142.0, 0.0, PI * 2, true)
            ctx.stroke()
        }
    }

}

fun CanvasState.save(block: () -> Unit) {
    save()
    block()
    restore()
}

@Composable
fun TokenBlock() {
    Row() {

    }
}

class ChartShape(
    override val path: Path?
) : Shape {

}