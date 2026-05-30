package com.panini.support.ui.screens.create

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.panini.support.core.UiState
import com.panini.support.domain.model.TicketCategory
import com.panini.support.domain.model.TicketPriority
import com.panini.support.ui.components.SectionLabel
import com.panini.support.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTicketScreen(
    viewModel: CreateTicketViewModel,
    onBack: () -> Unit,
    onTicketCreated: () -> Unit
) {
    val form        by viewModel.form.collectAsState()
    val submitState by viewModel.submitState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.submitted.collect { onTicketCreated() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Ticket", fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Información básica ──────────────────────
            Card(
                colors    = CardDefaults.cardColors(containerColor = PaniniBgCard),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SectionLabel("INFORMACIÓN BÁSICA")

                    OutlinedTextField(
                        value         = form.title,
                        onValueChange = viewModel::onTitleChange,
                        label         = { Text("Título del incidente *") },
                        modifier      = Modifier.fillMaxWidth(),
                        singleLine    = true,
                        isError       = form.titleError != null,
                        supportingText = form.titleError?.let { { Text(it, color = PaniniRed) } }
                    )

                    OutlinedTextField(
                        value         = form.description,
                        onValueChange = viewModel::onDescriptionChange,
                        label         = { Text("Descripción del problema *") },
                        modifier      = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        maxLines      = 6,
                        isError       = form.descError != null,
                        supportingText = form.descError?.let { { Text(it, color = PaniniRed) } }
                    )

                    OutlinedTextField(
                        value         = form.affectedRegion,
                        onValueChange = viewModel::onRegionChange,
                        label         = { Text("Región afectada") },
                        modifier      = Modifier.fillMaxWidth(),
                        singleLine    = true
                    )
                }
            }

            // ── Prioridad ───────────────────────────────
            Card(
                colors    = CardDefaults.cardColors(containerColor = PaniniBgCard),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionLabel("PRIORIDAD")
                    DropdownSelector(
                        label    = "Prioridad",
                        options  = TicketPriority.values().map { it to it.label },
                        selected = form.priority,
                        onSelect = viewModel::onPriorityChange
                    )
                }
            }

            // ── Categoría ───────────────────────────────
            Card(
                colors    = CardDefaults.cardColors(containerColor = PaniniBgCard),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionLabel("CATEGORÍA")
                    DropdownSelector(
                        label    = "Tipo de incidente",
                        options  = TicketCategory.values().map { it to it.label },
                        selected = form.category,
                        onSelect = viewModel::onCategoryChange
                    )
                }
            }

            // ── Proveedor ────────────────────────────────
            Card(
                colors    = CardDefaults.cardColors(containerColor = PaniniBgCard),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SectionLabel("PROVEEDOR")
                    DropdownSelector(
                        label    = "Proveedor relacionado",
                        options  = viewModel.suppliers.map { it.first to it.second },
                        selected = form.supplierId,
                        onSelect = { id ->
                            val name = viewModel.suppliers.find { it.first == id }?.second ?: ""
                            viewModel.onSupplierChange(id, name)
                        }
                    )
                }
            }

            // ── Error global ─────────────────────────────
            if (submitState is UiState.Error) {
                Text(
                    text  = (submitState as UiState.Error).message,
                    color = PaniniRed,
                    fontSize = 13.sp
                )
            }

            // ── Submit button ────────────────────────────
            Button(
                onClick  = { viewModel.submit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled  = submitState !is UiState.Loading,
                colors   = ButtonDefaults.buttonColors(containerColor = PaniniBlue)
            ) {
                if (submitState is UiState.Loading) {
                    CircularProgressIndicator(
                        color       = Color.White,
                        modifier    = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Crear Ticket", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Generic Dropdown Selector
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> DropdownSelector(
    label: String,
    options: List<Pair<T, String>>,
    selected: T,
    onSelect: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val currentLabel = options.find { it.first == selected }?.second ?: ""

    ExposedDropdownMenuBox(
        expanded         = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value            = currentLabel,
            onValueChange    = {},
            readOnly         = true,
            label            = { Text(label) },
            trailingIcon     = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier         = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded         = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (value, text) ->
                DropdownMenuItem(
                    text    = { Text(text, fontSize = 13.sp) },
                    onClick = {
                        onSelect(value)
                        expanded = false
                    }
                )
            }
        }
    }
}
