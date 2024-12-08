package org.tindex.miniapp.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.breakpoint.displayIfAtLeast
import com.varabyte.kobweb.silk.style.toAttrs
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.ColorSchemes
import com.varabyte.kobweb.silk.theme.shapes.RectF
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.tindex.miniapp.HeadlineTextStyle
import org.tindex.miniapp.SubheadlineTextStyle
import org.tindex.miniapp.components.Chart
import org.tindex.miniapp.components.HomePageHeader
import org.tindex.miniapp.components.layouts.PageLayout
import org.tindex.miniapp.toSitePalette

// Container that has a tagline and grid on desktop, and just the tagline on mobile
val HeroContainerStyle = CssStyle {
    base { Modifier.fillMaxWidth().gap(2.cssRem) }
    Breakpoint.MD { Modifier.margin { top(20.vh) } }
}

// A demo grid that appears on the homepage because it looks good
val HomeGridStyle = CssStyle.base {
    Modifier
        .gap(0.5.cssRem)
        .width(70.cssRem)
        .height(18.cssRem)
}

private val GridCellColorVar by StyleVariable<Color>()
val HomeGridCellStyle = CssStyle.base {
    Modifier
        .backgroundColor(GridCellColorVar.value())
        .boxShadow(blurRadius = 0.6.cssRem, color = GridCellColorVar.value())
        .borderRadius(1.cssRem)
}

@Composable
private fun GridCell(color: Color, row: Int, column: Int, width: Int? = null, height: Int? = null) {
    Div(
        HomeGridCellStyle.toModifier()
            .setVariable(GridCellColorVar, color)
            .gridItem(row, column, width, height)
            .toAttrs()
    )
}

@Page
@Composable
fun HomePage() {
    PageLayout("Home") {
        HomePageHeader()

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(leftRight = 16.px),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(2.px)
        ) {
            SpanText(
                text = "Points",
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
            Chart()
        }


        Row(HeroContainerStyle.toModifier()) {
            Box {
                val sitePalette = ColorMode.current.toSitePalette()
                Column(Modifier.gap(2.cssRem)) {
                    Div(HeadlineTextStyle.toAttrs()) {
                        SpanText(
                            "Use this template as your starting point for ", Modifier.color(
                                when (ColorMode.current) {
                                    ColorMode.LIGHT -> Colors.Black
                                    ColorMode.DARK -> Colors.White
                                }
                            )
                        )
                        SpanText(
                            "Kobweb",
                            Modifier
                                .color(sitePalette.brand.accent)
                                // Use a shadow so this light-colored word is more visible in light mode
                                .textShadow(0.px, 0.px, blurRadius = 0.5.cssRem, color = Colors.Gray)
                        )
                    }

                    Div(SubheadlineTextStyle.toAttrs()) {
                        SpanText("You can read the ")
                        Link("/about", "About")
                        SpanText(" page for more information.")
                    }

                    val ctx = rememberPageContext()
                    Button(onClick = {
                        // Change this click handler with your call-to-action behavior
                        // here. Link to an order page? Open a calendar UI? Play a movie?
                        // Up to you!
                        ctx.router.tryRoutingTo("/about")
                    }, colorScheme = ColorSchemes.Blue) {
                        Text("This could be your CTA")
                    }
                }
            }

            Div(HomeGridStyle
                .toModifier()
                .displayIfAtLeast(Breakpoint.MD)
                .grid {
                    rows { repeat(3) { size(1.fr) } }
                    columns { repeat(5) {size(1.fr) } }
                }
                .toAttrs()
            ) {
                val sitePalette = ColorMode.current.toSitePalette()
                GridCell(sitePalette.brand.primary, 1, 1, 2, 2)
                GridCell(ColorSchemes.Monochrome._600, 1, 3)
                GridCell(ColorSchemes.Monochrome._100, 1, 4, width = 2)
                GridCell(sitePalette.brand.accent, 2, 3, width = 2)
                GridCell(ColorSchemes.Monochrome._300, 2, 5)
                GridCell(ColorSchemes.Monochrome._800, 3, 1, width = 5)
            }
        }
    }
}
