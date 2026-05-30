package com.panini.support.ui.screens.detail

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
import com.panini.support.core.UiState
import com.panini.support.domain.model.*
import com.panini.support.ui.components.*
import com.panini.support.ui.theme.*
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    viewModel: TicketDetailViewModel,
    onBack: () -> Unit
) {
    val uiState      by viewModel.uiState.collectAsState()
    val actionState  by viewModel.actionState.collectAsState()
    val snackbarHost = remember { SnackbarHostState() }

    var showStatusDialog   by remember { mutableStateOf(false) }
    var showPriorityDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { msg -> snackbarHost.showSnackbar(msg) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Ticket", fontWeight = FontWeight.Bold) },
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
        snackbarHost   = { SnackbarHost(snackbarHost) },
        containerColor = PaniniBgLight
    ) { padding ->

        when (val state = uiState) {
            is UiState.Loading -> LoadingContent(Modifier.padding(padding))
            is UiState.Error   -> ErrorContent(
                message  = state.message,
                onRetry  = { viewModel.loadTicket() },
                modifier = Modifier.padding(padding)
            )
            is UiState.Success -> {
                val ticket = state.data
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // ── Header ──────────────────────────────
                    Card(
                        colors    = CardDefaults.cardColors(containerColor = PaniniBgCard),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(ticket.id, fontSize = 12.sp, color = PaniniTextSec)
                            Spacer(Modifier.height(4.dp))
                            Text(ticket.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(10.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                PriorityBadge(ticket.priority)
                                StatusBadge(ticket.status)
                            }
                        }
                    }

                    // ── Información ─────────────────────────
                    Card(
                        colors    = CardDefaults.cardColors(containerColor = PaniniBgCard),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            SectionLabel("INFORMACIÓN")
                            DetailRow("Proveedor",  ticket.supplierName)
                            DetailRow("Categoría",  ticket.category.label)
                            DetailRow("Región",     ticket.affectedRegion)
                            DetailRow("Creado por", ticket.createdBy)
                            ticket.assignedTo?.let { DetailRow("Asignado a", it) }
                            DetailRow("Creado", ticket.createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                            ticket.resolvedAt?.let {
                                DetailRow("Resuelto", it.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                            }
                        }
                    }

                    // ── Descripción ─────────────────────────
                    Card(
                        colors    = CardDefaults.cardColors(containerColor = PaniniBgCard),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            SectionLabel("DESCRIPCIÓN")
                            Spacer(Modifier.height(8.dp))
                            Text(ticket.description, fontSize = 13.sp, color = PaniniTextPrim, lineHeight = 20.sp)
                        }
                    }

                    // ── Acciones ────────────────────────────
                    Card(
                        colors    = CardDefaults.cardColors(containerColor = PaniniBgCard),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            SectionLabel("ACCIONES")
                            Spacer(Modifier.height(10.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedButton(
                                    onClick  = { showStatusDialog = true },
                                    enabled  = actionState !is UiState.Loading,
                                    modifier = Modifier.weight(1f)
                                ) { Text("Cambiar Estado", fontSize = 12.sp) }

                                Button(
                                    onClick  = { showPriorityDialog = true },
                                    enabled  = actionState !is UiState.Loading,
                                    colors   = ButtonDefaults.buttonColors(containerColor = PaniniBlue),
                                    modifier = Modifier.weight(1f)
                                ) { Text("Cambiar Prioridad", fontSize = 12.sp) }
                            }
                            if (actionState is UiState.Loading) {
                                Spacer(Modifier.height(8.dp))
                                LinearProgressIndicator(Modifier.fillMaxWidth(), color = PaniniBlue)
                            }
                        }
                    }

                    // ── Comentarios ─────────────────────────
                    if (ticket.comments.isNotEmpty()) {
                        Card(
                            colors    = CardDefaults.cardColors(containerColor = PaniniBgCard),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                SectionLabel("COMENTARIOS (${ticket.comments.size})")
                                Spacer(Modifier.height(10.dp))
                                ticket.comments.forEach { comment ->
                                    CommentItem(comment)
                                    Spacer(Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }

                if (showStatusDialog) {
                    StatusUpdateDialog(
                        currentStatus = ticket.status,
                        onDismiss     = { showStatusDialog = false },
                        onConfirm     = { status, comment ->
                            viewModel.updateStatus(status, comment)
                            showStatusDialog = false
                        }
                    )
                }

                if (showPriorityDialog) {
                    PriorityUpdateDialog(
                        currentPriority = ticket.priority,
                        onDismiss       = { showPriorityDialog = false },
                        onConfirm       = { priority, reason ->
                            viewModel.updatePriority(priority, reason)
                            showPriorityDialog = false
                        }
                    )
                }
            }
            is UiState.Idle -> { /* no-op */ }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 12.sp, color = PaniniTextSec, modifier = Modifier.weight(0.4f))
        Text(value, fontSize = 12.sp, color = PaniniTextPrim, fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(0.6f))
    }
}

@Composable
private fun CommentItem(comment: TicketComment) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(comment.authorName, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = PaniniBlue)
            Text(
                comment.createdAt.format(DateTimeFormatter.ofPattern("dd/MM HH:mm")),
                fontSize = 11.sp, color = PaniniTextSec
            )
        }
        Spacer(Modifier.height(2.dp))
        Text(comment.content, fontSize = 12.sp, color = PaniniTextPrim)
        Divider(Modifier.padding(top = 6.dp), color = PaniniDivider)
    }
}

@Composable
private fun StatusUpdateDialog(
    currentStatus: TicketStatus,
    onDismiss: () -> Unit,
    onConfirm: (TicketStatus, String?) -> Unit
) {
    var selected by remember { mutableStateOf(currentStatus) }
    var comment  by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title   = { Text("Actualizar Estado", fontWeight = FontWeight.Bold) },
        text    = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TicketStatus.values().forEach { status ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        RadioButton(selected = selected == status, onClick = { selected = status })
                        Text(status.label, fontSize = 13.sp)
                    }
                }
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value         = comment,
                    onValueChange = { comment = it },
                    label         = { Text("Comentario (opcional)") },
                    modifier      = Modifier.fillMaxWidth(),
                    maxLines      = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selected, comment.ifBlank { null }) },
                colors  = ButtonDefaults.buttonColors(containerColor = PaniniBlue)
            ) { Text("Confirmar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
private fun PriorityUpdateDialog(
    currentPriority: TicketPriority,
    onDismiss: () -> Unit,
    onConfirm: (TicketPriority, String?) -> Unit
) {
    var selected by remember { mutableStateOf(currentPriority) }
    var reason   by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title   = { Text("Actualizar Prioridad", fontWeight = FontWeight.Bold) },
        text    = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TicketPriority.values().forEach { priority ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        RadioButton(selected = selected == priority, onClick = { selected = priority })
                        Text(priority.label, fontSize = 13.sp)
                        Spacer(Modifier.width(6.dp))
                        PriorityBadge(priority)
                    }
                }
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value         = reason,
                    onValueChange = { reason = it },
                    label         = { Text("Motivo del cambio (opcional)") },
                    modifier      = Modifier.fillMaxWidth(),
                    maxLines      = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selected, reason.ifBlank { null }) },
                colors  = ButtonDefaults.buttonColors(containerColor = PaniniBlue)
            ) { Text("Confirmar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}