package org.tindex.miniapp.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.RectF
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.tindex.miniapp.components.CanvasGradientFillStyle
import org.tindex.miniapp.components.Chart
import org.tindex.miniapp.components.ChartDataSet
import org.tindex.miniapp.components.PlainFillStyle
import org.tindex.miniapp.components.layouts.PageLayout
import kotlin.random.Random

@Page
@Composable
fun HomePage() {
    PageLayout("Home") {
        HomePageHeader()

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.px)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(leftRight = 16.px),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(2.px)
            ) {
                IndexStats()
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.px)
            ) {
                IndexChart()
                ChartControl()
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(leftRight = 16.px, top = 16.px),
                verticalArrangement = Arrangement.spacedBy(4.px)
            ) {
                TopToken()
                TokenList()
            }
        }
    }
}

@Composable
fun HomePageHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.px)
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
fun IndexStats() {
    SpanText(
        text = "Index points",
        modifier = Modifier.fontSize(14.px)
            .lineHeight(20.px)
            .color(Color.rgb(0xFFFFFF))
    )
    SpanText(
        text = "500",
        modifier = Modifier.fontSize(30.px)
            .lineHeight(36.px)
            .color(Color.rgb(0xFFFFFF))
            .fontWeight(FontWeight.Bold)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.px)
    ) {
        SpanText(
            text = "32.3",
            modifier = Modifier.fontSize(16.px)
                .lineHeight(24.px)
                .color(Color.rgb(0x7BE3A1))
        )
        Column(
            modifier = Modifier.padding(
                leftRight = 6.px,
                topBottom = 1.px
            ).backgroundColor(
                Color.argb(0x3355BF7C)
            ).clip(RectF(cornerRadius = 4.px))
        ) {
            SpanText(
                text = "3.12%",
                modifier = Modifier.fontSize(14.px)
                    .lineHeight(20.px)
                    .color(Color.rgb(0x7BE3A1))
            )
        }
    }
}

@Composable
fun IndexChart() {
    val data: MutableList<Long> = mutableListOf()

    var rangeStart = -100L
    var rangeEnd = 100L

    for (i in 1..100) {
        val number = Random.nextLong(rangeStart, rangeEnd)
        data.add(number)

        rangeStart += (20).toInt()
        rangeEnd += (20).toInt()
    }

    Chart(
        dataSets = listOf(
            ChartDataSet(
                data = data,//mutableListOf(0.0, -50.0, -25.0, -25.0, 0.0, 25.0, -100.0, 50.0, 25.0, 50.0, 100.0, 50.0, 75.0, 25.0),
                style = ChartDataSet.ChartStyle(
                    default = ChartDataSet.Style(
                        lineStyle = PlainFillStyle(Color.rgb(247, 147, 26)),
                        areaStyle = CanvasGradientFillStyle(
                            direction = LinearGradient.Direction.ToBottom,
                            entries = listOf(
                                CanvasGradientFillStyle.Entry(0.0, Color.rgba(247, 147, 26, 33)),
                                CanvasGradientFillStyle.Entry(1.0, Color.rgba(247, 147, 26, 0))
                            )
                        ),
                    ),
                    active = ChartDataSet.Style(
                        lineStyle = PlainFillStyle(Color.rgb(61, 136, 247)),
                        areaStyle = CanvasGradientFillStyle(
                            direction = LinearGradient.Direction.ToBottom,
                            entries = listOf(
                                CanvasGradientFillStyle.Entry(0.0, Color.rgba(61, 136, 247, 33)),
                                CanvasGradientFillStyle.Entry(1.0, Color.rgba(61, 136, 247, 0))
                            )
                        ),
                    )
                ),
                points = ChartDataSet.Points(
                    active = ChartDataSet.PointStyle(
                        display = true,
                        size = 5.0,
                        strokeStyle = PlainFillStyle(Color.rgba(61, 136, 247, 33)),
                        areaStyle = PlainFillStyle(Color.rgb(61, 136, 247))
                    )
                )
            )
        ),
        height = 190,
        width = 0
    )
}

@Composable
fun ChartControl() {
    val periods = listOf(
        Pair("1h", true),
        Pair("24h", false),
        Pair("1w", false),
        Pair("1m", false),
        Pair("All", false)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(leftRight = 16.px)
            .height(28.px),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (period in periods) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .backgroundColor(if (period.second) Color.rgb(0x2C2C2E) else Colors.Transparent)
                    .clip(RectF(cornerRadius = 4.px))
                    .padding(leftRight = 8.px),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SpanText(
                    text = period.first,
                    modifier = Modifier
                        .fontSize(14.px)
                        .lineHeight(20.px)
                        .fontWeight(FontWeight.Medium)
                        .color(if (period.second) Color.rgb(0x3D88F7) else Color.rgb(0xFFFFFF))
                        .textAlign(TextAlign.Center)
                )
            }
        }

        Column(
            Modifier
                .minWidth(28.px)
                .width(28.px)
                .height(28.px)
                .backgroundColor(Color.rgb(0x2C2C2E))
                .clip(RectF(cornerRadius = 4.px))
        ) {

        }
    }
}

@Composable
fun TopToken() {
    Token("Toncoin", "TON", 6.92, 3.12)
}

@Composable
fun TokenList() {
    val open = remember { mutableStateOf(false) }

    console.log(open.value)

    if (open.value) {
        OpenTokenList(open)
    } else {
        ClosedTokenList(open)
    }
}

@Composable
fun ClosedTokenList(open: MutableState<Boolean>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.px)
                .padding(leftRight = 16.px, topBottom = 12.px)
                .backgroundColor(Color.rgb(0x2C2C2E))
                .clip(RectF(10.px))
                .zIndex(3f)
                .onClick {
                    open.value = true
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.px)
            ) {
                Column(
                    Modifier
                        .minSize(16.px)
                        .width(16.px)
                        .height(16.px)
                        .backgroundColor(Color.rgb(0x0088CC))
                        .clip(Circle())
                ) {

                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {
                    SpanText(
                        text = "More indexed tokens",
                        modifier = Modifier
                            .fontSize(16.px)
                            .lineHeight(24.px)
                            .fontWeight(FontWeight.Medium)
                            .color(Color.rgb(0xFFFFFF))
                            .textAlign(TextAlign.Left)
                    )
                }
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(leftRight = 8.px, topBottom = 18.px)
                .zIndex(2f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.px)
                    .backgroundColor(Color.rgb(0x282829))
                    .clip(RectF(10.px))
            ) {}
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(leftRight = 14.px, topBottom = 24.px)
                .zIndex(1f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.px)
                    .backgroundColor(Color.rgb(0x232325))
                    .clip(RectF(10.px))
            ) {}
        }
    }
}

@Composable
fun OpenTokenList(open: MutableState<Boolean>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.px)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SpanText(
                text = "Other indexed tokens",
                modifier = Modifier
                    .fontSize(16.px)
                    .lineHeight(24.px)
                    .fontWeight(FontWeight.Medium)
                    .color(Color.rgb(0xFFFFFF))
                    .textAlign(TextAlign.Left)
            )

            SpanText(
                text = "Show less",
                modifier = Modifier
                    .fontSize(16.px)
                    .lineHeight(24.px)
                    .fontWeight(FontWeight.Normal)
                    .color(Color.rgb(0x3D88F7))
                    .onClick {
                        open.value = false
                    }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.px, bottom = 150.px),
            verticalArrangement = Arrangement.spacedBy(4.px)
        ) {
            for (i in 1..9) {
                Token("Bitcoin", "BTC", 92345.0, 3.12)
            }
        }
    }
}

@Composable
fun Token(name: String, shortName: String, priceDollars: Double, changePercent: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(leftRight = 16.px, topBottom = 10.px)
            .backgroundColor(Color.rgb(0x2C2C2E))
            .clip(RectF(10.px)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.px)
        ) {
            Column(
                Modifier
                    .minSize(32.px)
                    .width(32.px)
                    .height(32.px)
                    .backgroundColor(Color.rgb(0x0088CC))
                    .clip(Circle())
            ) {

            }

            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start
            ) {
                SpanText(
                    text = name,
                    modifier = Modifier
                        .fontSize(16.px)
                        .lineHeight(24.px)
                        .fontWeight(FontWeight.Medium)
                        .color(Color.rgb(0xFFFFFF))
                        .textAlign(TextAlign.Left)
                )
                SpanText(
                    text = shortName,
                    modifier = Modifier
                        .fontSize(16.px)
                        .lineHeight(24.px)
                        .fontWeight(FontWeight.Normal)
                        .color(Color.argb(0x99FFFFFF))
                        .textAlign(TextAlign.Left)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(21.px)
        ) {
            Chart(
                dataSets = listOf(
                    ChartDataSet(
                        data = mutableListOf(0.0, -50.0, -25.0, -25.0, 0.0, 25.0, -100.0, 50.0, 25.0, 50.0, 100.0, 50.0, 75.0, 25.0),
                        style = ChartDataSet.ChartStyle(
                            default = ChartDataSet.Style(
                                lineStyle = PlainFillStyle(Color.rgb(0x7BE3A1))
                            )
                        ),
                    )
                ),
                height = 44,
                width = 65
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                SpanText(
                    text = "$$priceDollars",
                    modifier = Modifier
                        .fontSize(16.px)
                        .lineHeight(24.px)
                        .fontWeight(FontWeight.Medium)
                        .color(Color.rgb(0xFFFFFF))
                        .textAlign(TextAlign.Left)
                )
                Column(
                    modifier = Modifier.padding(
                        leftRight = 6.px,
                        topBottom = 1.px
                    ).backgroundColor(
                        Color.argb(0x3355BF7C)
                    ).clip(RectF(cornerRadius = 4.px))
                ) {
                    SpanText(
                        text = "$changePercent%",
                        modifier = Modifier.fontSize(14.px)
                            .lineHeight(20.px)
                            .color(Color.rgb(0x7BE3A1))
                    )
                }
            }
        }
    }
}
