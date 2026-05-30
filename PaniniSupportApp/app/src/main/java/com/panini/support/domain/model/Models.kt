package com.panini.support.domain.model

import java.time.LocalDateTime

// ─────────────────────────────────────────────────────────────
// Enums
// ─────────────────────────────────────────────────────────────

enum class TicketStatus(val label: String) {
    OPEN("Abierto"),
    IN_PROGRESS("En Progreso"),
    PENDING_SUPPLIER("Esperando Proveedor"),
    RESOLVED("Resuelto"),
    CLOSED("Cerrado"),
    CANCELLED("Cancelado")
}

enum class TicketPriority(val label: String, val order: Int) {
    CRITICAL("Crítica", 1),
    HIGH("Alta",      2),
    MEDIUM("Media",   3),
    LOW("Baja",       4)
}

enum class TicketCategory(val label: String) {
    INVENTORY_SHORTAGE("Faltante de Inventario"),
    DISTRIBUTION_DELAY("Retraso en Distribución"),
    SUPPLIER_QUALITY("Calidad de Proveedor"),
    LOGISTICS_ERROR("Error Logístico"),
    DUPLICATE_SHIPMENT("Envío Duplicado"),
    INVOICE_DISCREPANCY("Discrepancia en Factura"),
    PACKAGING_DAMAGE("Daño en Empaque"),
    MISSING_DOCUMENTATION("Documentación Faltante")
}

// ─────────────────────────────────────────────────────────────
// Domain models (independientes de DTOs o UI)
// ─────────────────────────────────────────────────────────────

data class Ticket(
    val id: String,
    val title: String,
    val description: String,
    val status: TicketStatus,
    val priority: TicketPriority,
    val category: TicketCategory,
    val supplierId: String,
    val supplierName: String,
    val affectedRegion: String,
    val createdBy: String,
    val assignedTo: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val resolvedAt: LocalDateTime?,
    val comments: List<TicketComment> = emptyList()
)

data class TicketComment(
    val id: String,
    val authorName: String,
    val content: String,
    val createdAt: LocalDateTime
)

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: String
)
