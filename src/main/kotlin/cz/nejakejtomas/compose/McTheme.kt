//package cz.nejakejtomas.compose
//
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.sp
//import cz.nejakejtomas.minimap.generated.resources.Res
//import cz.nejakejtomas.minimap.generated.resources.seven
//import org.jetbrains.compose.resources.Font
//
//object McTheme {
//    val sevenFamily
//        @Composable
//        get() = FontFamily(
//            Font(Res.font.seven, FontWeight.Normal),
//        )
//
//    val textStyle
//        @Composable
//        get() = TextStyle.Default.copy(
//            fontFamily = sevenFamily,
//            color = McColor.White,
//            fontSize = 8.sp
//        )
//
//    object McColor {
//        val Black = Color(0xFF000000)
//        val DarkBlue = Color(0xFF0000AA)
//        val DarkGreen = Color(0xFF00AA00)
//        val DarkAqua = Color(0xFF00AAAA)
//        val DarkRed = Color(0xFFAA0000)
//        val DarkPurple = Color(0xFFAA00AA)
//        val Gold = Color(0xFFFFAA00)
//        val Gray = Color(0xFFAAAAAA)
//        val DarkGray = Color(0xFF555555)
//        val Blue = Color(0xFF5555FF)
//        val Green = Color(0xFF55FF55)
//        val Aqua = Color(0xFF55FFFF)
//        val Red = Color(0xFFFF5555)
//        val LightPurple = Color(0xFFFF55FF)
//        val Yellow = Color(0xFFFFFF55)
//        val White = Color(0xFFFFFFFF)
//    }
//
//    val Color.background
//        get() = Color(red / 4, green / 4, blue / 4)
//}