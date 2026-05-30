package com.panini.support.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─────────────────────────────────────────────────────────────
// Color Palette — Panini FIFA 2026
// ─────────────────────────────────────────────────────────────
val PaniniBlue       = Color(0xFF003DA5)   // Azul FIFA oficial
val PaniniLightBlue  = Color(0xFF1565C0)
val PaniniGold       = Color(0xFFF5A623)   // Dorado copa mundial
val PaniniRed        = Color(0xFFD32F2F)   // Prioridad Crítica
val PaniniOrange     = Color(0xFFE64A19)   // Prioridad Alta
val PaniniAmber      = Color(0xFFF9A825)   // Prioridad Media
val PaniniGreen      = Color(0xFF388E3C)   // Resuelto / Éxito
val PaniniBgLight    = Color(0xFFF5F7FA)
val PaniniBgCard     = Color(0xFFFFFFFF)
val PaniniTextPrim   = Color(0xFF0D1B2A)
val PaniniTextSec    = Color(0xFF546E7A)
val PaniniDivider    = Color(0xFFE0E7EF)

// Estado chips
val StatusOpen            = Color(0xFF1565C0)
val StatusInProgress      = Color(0xFF6A1B9A)
val StatusPendingSupplier = Color(0xFFE65100)
val StatusResolved        = Color(0xFF2E7D32)
val StatusClosed          = Color(0xFF546E7A)
val StatusCancelled       = Color(0xFFB71C1C)

private val LightColorScheme = lightColorScheme(
    primary          = PaniniBlue,
    onPrimary        = Color.White,
    primaryContainer = Color(0xFFD8E8FF),
    secondary        = PaniniGold,
    onSecondary      = PaniniTextPrim,
    background       = PaniniBgLight,
    surface          = PaniniBgCard,
    onBackground     = PaniniTextPrim,
    onSurface        = PaniniTextPrim,
    error            = PaniniRed,
    onError          = Color.White
)

val AppShapes = Shapes(
    small  = androidx.compose.foundation.shape.RoundedCornerShape(6.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    large  = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
)

@Composable
fun PaniniSupportTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        shapes      = AppShapes,
        content     = content
    )
}
