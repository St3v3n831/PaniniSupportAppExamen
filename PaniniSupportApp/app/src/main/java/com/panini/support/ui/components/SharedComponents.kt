package com.panini.support.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.panini.support.domain.model.TicketPriority
import com.panini.support.domain.model.TicketStatus
import com.panini.support.ui.theme.*

// ─────────────────────────────────────────────────────────────
// Priority Badge
// ─────────────────────────────────────────────────────────────

@Composable
fun PriorityBadge(priority: TicketPriority, modifier: Modifier = Modifier) {
    val (bg, fg) = when (priority) {
        TicketPriority.CRITICAL -> PaniniRed    to Color.White
        TicketPriority.HIGH     -> PaniniOrange to Color.White
        TicketPriority.MEDIUM   -> PaniniAmber  to PaniniTextPrim
        TicketPriority.LOW      -> Color(0xFFCFD8DC) to PaniniTextSec
    }
    Surface(
        modifier  = modifier,
        shape     = RoundedCornerShape(4.dp),
        color     = bg,
        tonalElevation = 0.dp
    ) {
        Text(
            text     = priority.label.uppercase(),
            color    = fg,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

// ─────────────────────────────────────────────────────────────
// Status Badge
// ─────────────────────────────────────────────────────────────

@Composable
fun StatusBadge(status: TicketStatus, modifier: Modifier = Modifier) {
    val (bg, fg) = when (status) {
        TicketStatus.OPEN             -> StatusOpen            to Color.White
        TicketStatus.IN_PROGRESS      -> StatusInProgress      to Color.White
        TicketStatus.PENDING_SUPPLIER -> StatusPendingSupplier to Color.White
        TicketStatus.RESOLVED         -> StatusResolved        to Color.White
        TicketStatus.CLOSED           -> StatusClosed          to Color.White
        TicketStatus.CANCELLED        -> StatusCancelled       to Color.White
    }
    Surface(
        modifier  = modifier,
        shape     = RoundedCornerShape(50.dp),
        color     = bg.copy(alpha = 0.15f),
        tonalElevation = 0.dp
    ) {
        Row(
            verticalAlignment   = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(bg, shape = RoundedCornerShape(50))
            )
            Spacer(Modifier.width(5.dp))
            Text(
                text     = status.label,
                color    = bg,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Loading Indicator
// ─────────────────────────────────────────────────────────────

@Composable
fun LoadingContent(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = PaniniBlue)
    }
}

// ─────────────────────────────────────────────────────────────
// Error Content
// ─────────────────────────────────────────────────────────────

@Composable
fun ErrorContent(
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier            = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "⚠️", fontSize = 40.sp)
        Spacer(Modifier.height(12.dp))
        Text(
            text     = message,
            color    = PaniniTextSec,
            fontSize = 14.sp
        )
        if (onRetry != null) {
            Spacer(Modifier.height(16.dp))
            OutlinedButton(onClick = onRetry) { Text("Reintentar") }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Section Label
// ─────────────────────────────────────────────────────────────

@Composable
fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text       = text,
        fontSize   = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color      = PaniniTextSec,
        letterSpacing = 0.8.sp,
        modifier   = modifier
    )
}
