package com.panini.support.data.remote.mock

import com.panini.support.domain.model.*
import java.time.LocalDateTime

object MockDataSource {

    val tickets: MutableList<Ticket> = mutableListOf(
        Ticket(
            id             = "TKT-2026-001",
            title          = "Faltante stickers Serie América del Sur — 15,000 unidades",
            description    = "El proveedor IMPSA reporta un faltante de 15,000 unidades de stickers de la serie América del Sur (números 201-250). Afecta directamente la distribución programada para Costa Rica y Panamá esta semana. Se requiere revisión urgente con el proveedor y reposición de stock.",
            status         = TicketStatus.OPEN,
            priority       = TicketPriority.CRITICAL,
            category       = TicketCategory.INVENTORY_SHORTAGE,
            supplierId     = "SUP-004",
            supplierName   = "IMPSA Impresiones S.A.",
            affectedRegion = "América Central",
            createdBy      = "Carlos Méndez",
            assignedTo     = "Laura Vega",
            createdAt      = LocalDateTime.now().minusDays(2),
            updatedAt      = LocalDateTime.now().minusHours(3),
            resolvedAt     = null,
            comments       = listOf(
                TicketComment("CMT-001", "Laura Vega",
                    "Contactado IMPSA. Confirmaron error en línea de producción. ETA corrección: 5 días hábiles.",
                    LocalDateTime.now().minusHours(2))
            )
        ),
        Ticket(
            id             = "TKT-2026-002",
            title          = "Retraso en despacho desde bodega México — lote MX-4520",
            description    = "El lote MX-4520 correspondiente a 80,000 paquetes de sobres (series Europa) lleva 4 días de retraso desde la bodega de Guadalajara. El operador logístico FedEx Freight no ha emitido guía de tránsito. Puntos de venta en CDMX reportan desabasto.",
            status         = TicketStatus.IN_PROGRESS,
            priority       = TicketPriority.HIGH,
            category       = TicketCategory.DISTRIBUTION_DELAY,
            supplierId     = "SUP-001",
            supplierName   = "FedEx Freight México",
            affectedRegion = "México — CDMX y Guadalajara",
            createdBy      = "Roberto Sánchez",
            assignedTo     = "Ana Torres",
            createdAt      = LocalDateTime.now().minusDays(4),
            updatedAt      = LocalDateTime.now().minusHours(1),
            resolvedAt     = null,
            comments       = listOf(
                TicketComment("CMT-002", "Ana Torres",
                    "FedEx confirma que el lote está en aduana. Estimado de liberación: mañana antes de las 14:00.",
                    LocalDateTime.now().minusHours(5)),
                TicketComment("CMT-003", "Roberto Sánchez",
                    "Avisado a puntos de venta. Se coordina entrega directa desde aduana si se aprueba.",
                    LocalDateTime.now().minusHours(1))
            )
        ),
        Ticket(
            id             = "TKT-2026-003",
            title          = "Daño en empaques — lote Brasil Serie Especial",
            description    = "Se recibieron 3,500 paquetes de la Serie Especial Brasil con empaques dañados por humedad. El daño ocurrió durante el transporte marítimo desde São Paulo. Los stickers internos parecen en buen estado pero los sobres no pueden comercializarse en el estado actual.",
            status         = TicketStatus.PENDING_SUPPLIER,
            priority       = TicketPriority.HIGH,
            category       = TicketCategory.PACKAGING_DAMAGE,
            supplierId     = "SUP-007",
            supplierName   = "Gráfica Panini Brasil Ltda.",
            affectedRegion = "Brasil",
            createdBy      = "Fernanda Lima",
            assignedTo     = null,
            createdAt      = LocalDateTime.now().minusDays(6),
            updatedAt      = LocalDateTime.now().minusDays(1),
            resolvedAt     = null,
            comments       = listOf(
                TicketComment("CMT-004", "Fernanda Lima",
                    "Se solicitó análisis de responsabilidad al seguro de transporte marítimo.",
                    LocalDateTime.now().minusDays(1))
            )
        ),
        Ticket(
            id             = "TKT-2026-004",
            title          = "Factura proveedor España — discrepancia de 12,000 EUR",
            description    = "La factura F-2026-0092 del proveedor LogiStar España tiene una discrepancia de €12,000 respecto al contrato firmado en febrero. Se facturó el transporte premium sin autorización previa. Se requiere nota de crédito o justificación detallada.",
            status         = TicketStatus.OPEN,
            priority       = TicketPriority.MEDIUM,
            category       = TicketCategory.INVOICE_DISCREPANCY,
            supplierId     = "SUP-012",
            supplierName   = "LogiStar España S.L.",
            affectedRegion = "Europa — España",
            createdBy      = "Miguel Fernández",
            assignedTo     = "Contabilidad",
            createdAt      = LocalDateTime.now().minusDays(3),
            updatedAt      = LocalDateTime.now().minusDays(3),
            resolvedAt     = null,
            comments       = emptyList()
        ),
        Ticket(
            id             = "TKT-2026-005",
            title          = "Envío duplicado stickers Golden Edition — Argentina",
            description    = "Se detectó envío duplicado del lote AR-GE-200 (Golden Edition) a Buenos Aires. El distribuidor ya recibió el primer lote correctamente. El segundo lote (2,000 paquetes) está en bodega sin instrucciones de retiro. Riesgo de sobre stock y afectación al precio.",
            status         = TicketStatus.RESOLVED,
            priority       = TicketPriority.MEDIUM,
            category       = TicketCategory.DUPLICATE_SHIPMENT,
            supplierId     = "SUP-003",
            supplierName   = "DHL Express Argentina",
            affectedRegion = "Argentina — Buenos Aires",
            createdBy      = "Juliana Ramos",
            assignedTo     = "Juliana Ramos",
            createdAt      = LocalDateTime.now().minusDays(8),
            updatedAt      = LocalDateTime.now().minusDays(1),
            resolvedAt     = LocalDateTime.now().minusDays(1),
            comments       = listOf(
                TicketComment("CMT-005", "Juliana Ramos",
                    "DHL retiró el lote duplicado. Retornado a bodega central. Caso cerrado.",
                    LocalDateTime.now().minusDays(1))
            )
        ),
        Ticket(
            id             = "TKT-2026-006",
            title          = "Error logístico — stickers enviados a punto de venta incorrecto",
            description    = "El lote CL-0034 destinado a Falabella Santiago fue enviado por error al centro de distribución de Falabella Perú. El error fue cometido por el operador en bodega. Afecta lanzamiento regional del miércoles.",
            status         = TicketStatus.IN_PROGRESS,
            priority       = TicketPriority.CRITICAL,
            category       = TicketCategory.LOGISTICS_ERROR,
            supplierId     = "SUP-009",
            supplierName   = "Almacenes Centrales Chile SpA",
            affectedRegion = "Chile — Perú",
            createdBy      = "Diego Castillo",
            assignedTo     = "Diego Castillo",
            createdAt      = LocalDateTime.now().minusDays(1),
            updatedAt      = LocalDateTime.now().minusHours(2),
            resolvedAt     = null,
            comments       = listOf(
                TicketComment("CMT-006", "Diego Castillo",
                    "Coordinando transporte urgente Perú-Chile. Costo adicional en evaluación.",
                    LocalDateTime.now().minusHours(2))
            )
        ),
        Ticket(
            id             = "TKT-2026-007",
            title          = "Documentación aduanera faltante — importación Colombia",
            description    = "El lote CO-5500 detenido en aduana de Bogotá por falta del certificado sanitario INVIMA. El proveedor en Italia no adjuntó el documento al momento del despacho. Posible multa de $800 USD por día de almacenamiento.",
            status         = TicketStatus.OPEN,
            priority       = TicketPriority.HIGH,
            category       = TicketCategory.MISSING_DOCUMENTATION,
            supplierId     = "SUP-015",
            supplierName   = "Panini S.p.A. Italia",
            affectedRegion = "Colombia",
            createdBy      = "Valentina Ospina",
            assignedTo     = null,
            createdAt      = LocalDateTime.now().minusHours(8),
            updatedAt      = LocalDateTime.now().minusHours(8),
            resolvedAt     = null,
            comments       = emptyList()
        ),
        Ticket(
            id             = "TKT-2026-008",
            title          = "Baja calidad impresión — lote Uruguay colección premium",
            description    = "El distribuidor principal en Uruguay reportó que 1,200 paquetes de la colección premium presentan problemas de impresión: colores deslavados y texto ilegible en stickers de portada. Los paquetes ya están en puntos de venta.",
            status         = TicketStatus.OPEN,
            priority       = TicketPriority.HIGH,
            category       = TicketCategory.SUPPLIER_QUALITY,
            supplierId     = "SUP-007",
            supplierName   = "Gráfica Panini Brasil Ltda.",
            affectedRegion = "Uruguay",
            createdBy      = "Marcelo Suárez",
            assignedTo     = null,
            createdAt      = LocalDateTime.now().minusHours(5),
            updatedAt      = LocalDateTime.now().minusHours(5),
            resolvedAt     = null,
            comments       = emptyList()
        )
    )

    val suppliers = listOf(
        Pair("SUP-001", "FedEx Freight México"),
        Pair("SUP-003", "DHL Express Argentina"),
        Pair("SUP-004", "IMPSA Impresiones S.A."),
        Pair("SUP-007", "Gráfica Panini Brasil Ltda."),
        Pair("SUP-009", "Almacenes Centrales Chile SpA"),
        Pair("SUP-012", "LogiStar España S.L."),
        Pair("SUP-015", "Panini S.p.A. Italia"),
        Pair("SUP-018", "Transportes Andinos S.A.")
    )

    var nextTicketNumber = 9

    fun generateTicketId(): String {
        val id = "TKT-2026-${nextTicketNumber.toString().padStart(3, '0')}"
        nextTicketNumber++
        return id
    }
}
