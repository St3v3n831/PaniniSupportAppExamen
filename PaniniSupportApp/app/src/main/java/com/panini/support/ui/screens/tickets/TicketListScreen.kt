package com.panini.support.ui.screens.tickets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.panini.support.core.FeatureFlags
import com.panini.support.core.UiState
import com.panini.support.domain.model.Ticket
import com.panini.support.domain.model.TicketPriority
import com.panini.support.domain.model.TicketStatus
import com.panini.support.ui.components.*
import com.panini.support.ui.theme.*
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    viewModel: TicketListViewModel,
    onTicketClick: (String) -> Unit,
    onCreateTicket: () -> Unit,
    onOpenFlags: () -> Unit
) {
    val uiState      by viewModel.uiState.collectAsState()
    val flags        by FeatureFlags.flags.collectAsState()
    val snackbarHost = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { msg ->
            snackbarHost.showSnackbar(msg)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Support Tickets", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("FIFA World Cup 2026", fontSize = 11.sp, color = PaniniGold)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadTickets() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Recargar", tint = Color.White)
                    }
                    IconButton(onClick = onOpenFlags) {
                        Icon(Icons.Default.Settings, contentDescription = "Feature Flags", tint = PaniniGold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = PaniniBlue,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick        = onCreateTicket,
                containerColor = PaniniGold,
                contentColor   = PaniniTextPrim
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear ticket")
            }
        },
        snackbarHost   = { SnackbarHost(snackbarHost) },
        containerColor = PaniniBgLight
    ) { padding ->

        when (val state = uiState) {
            is UiState.Loading -> LoadingContent(Modifier.padding(padding))
            is UiState.Error   -> ErrorContent(
                message  = state.message,
                onRetry  = { viewModel.loadTickets() },
                modifier = Modifier.padding(padding)
            )
            is UiState.Success -> {

                val filtered = state.data.filter { ticket ->
                    if (!flags.showResolvedTickets && ticket.status == TicketStatus.RESOLVED) return@filter false
                    if (!flags.showCriticalCategory && ticket.priority == TicketPriority.CRITICAL) return@filter false
                    true
                }

                if (filtered.isEmpty()) {
                    Box(
                        Modifier.padding(padding).fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay tickets para mostrar.", color = PaniniTextSec)
                    }
                } else {
                    LazyColumn(
                        modifier            = Modifier.padding(padding),
                        contentPadding      = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Text(
                                text     = "${filtered.size} ticket(s) — ordenados por prioridad",
                                fontSize = 12.sp,
                                color    = PaniniTextSec,
                                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                            )
                        }
                        items(filtered, key = { it.id }) { ticket ->
                            TicketCard(
                                ticket  = ticket,
                                onClick = { onTicketClick(ticket.id) }
                            )
                        }
                    }
                }
            }
            is UiState.Idle -> { /* no-op */ }
        }
    }
}

@Composable
fun TicketCard(ticket: Ticket, onClick: () -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape     = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = PaniniBgCard),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(ticket.id, fontSize = 11.sp, color = PaniniTextSec, fontWeight = FontWeight.Medium)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    PriorityBadge(ticket.priority)
                    StatusBadge(ticket.status)
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text       = ticket.title,
                fontSize   = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color      = PaniniTextPrim,
                maxLines   = 2,
                overflow   = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text     = "🏭 ${ticket.supplierName}",
                        fontSize = 11.sp,
                        color    = PaniniTextSec,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.widthIn(max = 200.dp)
                    )
                    Text(text = ticket.category.label, fontSize = 11.sp, color = PaniniBlue)
                }
                Text(
                    text     = ticket.createdAt.format(formatter),
                    fontSize = 11.sp,
                    color    = PaniniTextSec
                )
            }
        }
    }
}