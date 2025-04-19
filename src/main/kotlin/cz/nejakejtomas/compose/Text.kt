//package cz.nejakejtomas.compose
//
//import androidx.compose.foundation.text.BasicText
//import androidx.compose.foundation.text.InlineTextContent
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Shadow
//import androidx.compose.ui.graphics.isSpecified
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.text.AnnotatedString
//import androidx.compose.ui.text.TextLayoutResult
//import androidx.compose.ui.text.font.FontStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.TextUnit
//import androidx.compose.ui.unit.isSpecified
//import cz.nejakejtomas.compose.McTheme.background
//
//// TODO: Find better way to do annotated text (and fix visual differences) between this and original Minecraft font
//// Add obfuscated text
//@Composable
//fun Text(
//    text: String,
//    modifier: Modifier = Modifier,
//    color: Color = Color.Unspecified,
//    fontSize: TextUnit = TextUnit.Unspecified,
//    fontStyle: FontStyle? = null,
//    fontWeight: FontWeight? = null,
//    textDecoration: TextDecoration? = null,
//    textAlign: TextAlign? = null,
//    lineHeight: TextUnit = TextUnit.Unspecified,
//    overflow: TextOverflow = TextOverflow.Clip,
//    softWrap: Boolean = true,
//    maxLines: Int = Int.MAX_VALUE,
//    minLines: Int = 1,
//    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
//    hasShadow: Boolean = true,
//) {
//    val style = McTheme.textStyle.merge(
//        fontSize = fontSize,
//        fontWeight = fontWeight,
//        textAlign = textAlign ?: TextAlign.Unspecified,
//        lineHeight = lineHeight,
//        fontFamily = McTheme.sevenFamily,
//        textDecoration = textDecoration,
//        fontStyle = fontStyle,
//    )
//
//    val overrideColorOrUnspecified = if (color.isSpecified) {
//        color
//    } else if (style.color.isSpecified) {
//        style.color
//    } else {
//        McTheme.McColor.White
//    }
//
//    val shadowOffset = with(LocalDensity.current) { if (!style.fontSize.isSpecified) 0f else style.fontSize.toPx() * 0.125f }
//
//    val shadow = if (hasShadow) Shadow(
//        color = overrideColorOrUnspecified.background, offset = Offset(shadowOffset, shadowOffset), blurRadius = 0f
//    )
//    else null
//
//    BasicText(
//        text = text,
//        modifier = modifier,
//        style = style.merge(shadow = shadow),
//        onTextLayout = onTextLayout,
//        overflow = overflow,
//        softWrap = softWrap,
//        maxLines = maxLines,
//        minLines = minLines,
//        color = { overrideColorOrUnspecified }
//    )
//}
//
//@Composable
//fun Text(
//    text: AnnotatedString,
//    modifier: Modifier = Modifier,
//    color: Color = Color.Unspecified,
//    fontSize: TextUnit = TextUnit.Unspecified,
//    fontStyle: FontStyle? = null,
//    fontWeight: FontWeight? = null,
//    textDecoration: TextDecoration? = null,
//    textAlign: TextAlign? = null,
//    lineHeight: TextUnit = TextUnit.Unspecified,
//    overflow: TextOverflow = TextOverflow.Clip,
//    softWrap: Boolean = true,
//    maxLines: Int = Int.MAX_VALUE,
//    minLines: Int = 1,
//    inlineContent: Map<String, InlineTextContent> = mapOf(),
//    onTextLayout: (TextLayoutResult) -> Unit = {},
//    hasShadow: Boolean = true,
//) {
//    val style = McTheme.textStyle.merge(
//        fontSize = fontSize,
//        fontWeight = fontWeight,
//        textAlign = textAlign ?: TextAlign.Unspecified,
//        lineHeight = lineHeight,
//        fontFamily = McTheme.sevenFamily,
//        textDecoration = textDecoration,
//        fontStyle = fontStyle,
//    )
//
//    val overrideColorOrUnspecified = if (color.isSpecified) {
//        color
//    } else if (style.color.isSpecified) {
//        style.color
//    } else {
//        McTheme.McColor.White
//    }
//
//    val shadowOffset = with(LocalDensity.current) { if (!style.fontSize.isSpecified) 0f else style.fontSize.toPx() * 0.125f }
//
//    val shadow = if (hasShadow) Shadow(
//        color = overrideColorOrUnspecified.background, offset = Offset(shadowOffset, shadowOffset), blurRadius = 0f
//    )
//    else null
//
//    BasicText(
//        text = text,
//        modifier = modifier,
//        style = style.merge(shadow = shadow),
//        onTextLayout = onTextLayout,
//        overflow = overflow,
//        softWrap = softWrap,
//        maxLines = maxLines,
//        minLines = minLines,
//        inlineContent = inlineContent,
//        color = { overrideColorOrUnspecified }
//    )
//}