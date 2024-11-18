package com.note.core.designsystem

import androidx.compose.ui.graphics.Color

val KotlinColor = Color(0xFFA97BFF)
val JavaColor = Color(0xFFB07219)
val JavaScriptColor = Color(0xFFF1E05A)
val SwiftColor = Color(0xFFF05138)
val HtmlColor = Color(0xFFE34C26)
val CssColor = Color(0xFF563D7C)
val PhpColor = Color(0xFF4F5D95)
val PythonColor = Color(0xFF3572A5)
val VueColor = Color(0xFF41B883)
val CColor = Color(0xFF555555)
val CSharpColor = Color(0xFF178600)
val CPlusPlusColor = Color(0xFFF34B7D)
val RubyColor = Color(0xFF701516)
val DartColor = Color(0xFF00B4AB)
val LanguageDefaultColor = Color(0xFF814CCC)

fun getColorByLanguage(language: String): Color {
    return when (language.lowercase()) {
        "kotlin" -> KotlinColor
        "java" -> JavaColor
        "javascript" -> JavaScriptColor
        "swift" -> SwiftColor
        "html" -> HtmlColor
        "css" -> CssColor
        "php" -> PhpColor
        "python" -> PythonColor
        "vue" -> VueColor
        "c" -> CColor
        "c#" -> CSharpColor
        "c++" -> CPlusPlusColor
        "ruby" -> RubyColor
        "dart" -> DartColor
        else -> LanguageDefaultColor
    }
}