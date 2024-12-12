package org.tindex.miniapp.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toAttrs
import com.varabyte.kobweb.silk.theme.shapes.RectF
import com.varabyte.kobweb.silk.theme.shapes.clip
import kotlinx.browser.document
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div

val PageContentStyle = CssStyle {
    base { Modifier.fillMaxSize() }
    Breakpoint.MD { Modifier.maxWidth(60.cssRem) }
}

@Composable
fun Footer() {
    val footerColor = Color.rgb(0x2C2C2E)
    val accentColor = Color.rgb(0x3D88F7)

    val tabs = listOf(
        Pair("Top", false),
        Pair("Home", true),
        Pair("Portfolio", false)
    )

    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .backgroundColor(Colors.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .position(Position.Fixed)
                .bottom(0.percent)
                .zIndex(Int.MAX_VALUE)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.px)
                    .padding(leftRight = 16.px, bottom = 16.px)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background()
                        .background(accentColor)
                        .clip(shape = RectF(cornerRadius = 14.px))
                        .boxShadow(
                            offsetY = (-2).px,
                            blurRadius = 8.px,
                            color = Color.rgba(0x1C1C1C33)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    SpanText(
                        text = "Invest",
                        modifier = Modifier
                            .fontSize(16.px)
                            .lineHeight(24.px)
                            .fontWeight(FontWeight.Medium)
                            .color(Color.rgb(0xFFFFFF))
                            .textAlign(TextAlign.Center)
                    )
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(52.px)
                    .backgroundColor(footerColor),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val ctx = rememberPageContext()
                for (tab in tabs) {
                    Column(
                        Modifier.fillMaxWidth()
                            .onClick {
                                ctx.router.tryRoutingTo("/about")
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SpanText(
                            text = tab.first,
                            modifier = Modifier
                                .fontSize(12.px)
                                .lineHeight(16.px)
                                .fontWeight(FontWeight.Medium)
                                .color(if (tab.second) accentColor else Color.rgba(0xFFFFFF99))
                                .textAlign(TextAlign.Center)
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun PageLayout(title: String, content: @Composable ColumnScope.() -> Unit) {
    LaunchedEffect(title) {
        document.title = "Kobweb - $title"
    }

    Box(
        Modifier
            .width(100.percent)
            .minHeight(100.percent),
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier.fillMaxSize(),//.gridRow(1),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            //Div(PageContentStyle.toAttrs()) {
                content()
           // }
        }

        Footer()
    }

}
