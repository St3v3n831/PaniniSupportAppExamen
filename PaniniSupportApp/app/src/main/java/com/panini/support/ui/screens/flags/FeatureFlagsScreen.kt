package com.panini.support.ui.screens.flags

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.panini.support.ui.components.SectionLabel
import com.panini.support.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureFlagsScreen(
    onBack: () -> Unit,
    viewModel: FeatureFlagsViewModel = viewModel()
) {
    val flags by viewModel.flags.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Feature Flags", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Control de funcionalidades", fontSize = 11.sp, color = PaniniGold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = PaniniBlue,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = PaniniBgLight
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // ── Visualización ────────────────────────────
            Card(
                colors    = CardDefaults.cardColors(containerColor = PaniniBgCard),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    SectionLabel("VISUALIZACIÓN")
                    Spacer(Modifier.height(4.dp))

                    FlagToggleRow(
                        title           = "Mostrar tickets resueltos",
                        description     = "Incluye tickets con estado Resuelto en el listado",
                        checked         = flags.showResolvedTickets,
                        onCheckedChange = viewModel::setShowResolvedTickets
                    )
                    Divider(color = PaniniDivider)
                    FlagToggleRow(
                        title           = "Mostrar categoría Crítica",
                        description     = "Muestra tickets de prioridad CRÍTICA en el listado",
                        checked         = flags.showCriticalCategory,
                        onCheckedChange = viewModel::setShowCriticalCategory
                    )
                }
            }

            // ── Estado actual ────────────────────────────
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = PaniniGold.copy(alpha = 0.10f)
                )
            ) {
                Column(Modifier.padding(14.dp)) {
                    SectionLabel("ESTADO ACTUAL")
                    Spacer(Modifier.height(8.dp))
                    FlagStatusRow("Tickets resueltos",  flags.showResolvedTickets)
                    FlagStatusRow("Categoría crítica",  flags.showCriticalCategory)
                }
            }
        }
    }
}

@Composable
private fun FlagToggleRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = PaniniTextPrim)
            Text(description, fontSize = 11.sp, color = PaniniTextSec, lineHeight = 16.sp)
        }
        Spacer(Modifier.width(12.dp))
        Switch(
            checked         = checked,
            onCheckedChange = onCheckedChange,
            colors          = SwitchDefaults.colors(
                checkedThumbColor   = Color.White,
                checkedTrackColor   = PaniniGreen,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = PaniniTextSec.copy(alpha = 0.4f)
            )
        )
    }
}

@Composable
private fun FlagStatusRow(label: String, enabled: Boolean) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = PaniniTextSec)
        Text(
            text       = if (enabled) "✅ Activo" else "🔴 Inactivo",
            fontSize   = 12.sp,
            fontWeight = FontWeight.Medium,
            color      = if (enabled) PaniniGreen else PaniniRed
        )
    }
}