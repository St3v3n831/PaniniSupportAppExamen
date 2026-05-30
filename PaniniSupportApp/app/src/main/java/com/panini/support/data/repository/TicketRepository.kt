package com.panini.support.data.repository

import com.panini.support.core.ApiResult
import com.panini.support.core.UserMessages
import com.panini.support.data.remote.ApiService
import com.panini.support.data.remote.mock.MockDataSource
import com.panini.support.data.remote.model.*
import com.panini.support.domain.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import java.time.LocalDateTime

// ─────────────────────────────────────────────────────────────
// Repository interface
// Permite cambiar la implementación Mock ↔ Real sin tocar ViewModels
// ─────────────────────────────────────────────────────────────

interface TicketRepository {
    val ticketEvents: SharedFlow<TicketEvent>

    suspend fun getTickets(): ApiResult<List<Ticket>>
    suspend fun getTicketById(ticketId: String): ApiResult<Ticket>
    suspend fun createTicket(
        title: String,
        description: String,
        priority: TicketPriority,
        category: TicketCategory,
        supplierId: String,
        supplierName: String,
        affectedRegion: String
    ): ApiResult<Ticket>

    suspend fun updateTicketStatus(
        ticketId: String,
        status: TicketStatus,
        comment: String? = null
    ): ApiResult<Ticket>

    suspend fun updateTicketPriority(
        ticketId: String,
        priority: TicketPriority,
        reason: String? = null
    ): ApiResult<Ticket>
}

// ─────────────────────────────────────────────────────────────
// Eventos de dominio (comunicación basada en eventos — Req. 5.1)
// ─────────────────────────────────────────────────────────────

sealed class TicketEvent {
    data class TicketCreated(val ticket: Ticket) : TicketEvent()
    data class StatusUpdated(val ticket: Ticket) : TicketEvent()
    data class PriorityUpdated(val ticket: Ticket) : TicketEvent()
}

// ─────────────────────────────────────────────────────────────
// Implementación Mock
// ─────────────────────────────────────────────────────────────

class MockTicketRepository : TicketRepository {

    private val _ticketEvents = MutableSharedFlow<TicketEvent>(extraBufferCapacity = 10)
    override val ticketEvents: SharedFlow<TicketEvent> = _ticketEvents

    override suspend fun getTickets(): ApiResult<List<Ticket>> {
        delay(600)
        return ApiResult.Success(
            MockDataSource.tickets.sortedWith(compareBy { it.priority.order })
        )
    }

    override suspend fun getTicketById(ticketId: String): ApiResult<Ticket> {
        delay(300)
        val ticket = MockDataSource.tickets.find { it.id == ticketId }
        return if (ticket != null) {
            ApiResult.Success(ticket)
        } else {
            ApiResult.Error(message = UserMessages.Ticket.NOT_FOUND, statusCode = 404)
        }
    }

    override suspend fun createTicket(
        title: String,
        description: String,
        priority: TicketPriority,
        category: TicketCategory,
        supplierId: String,
        supplierName: String,
        affectedRegion: String
    ): ApiResult<Ticket> {
        delay(800)
        val newTicket = Ticket(
            id             = MockDataSource.generateTicketId(),
            title          = title,
            description    = description,
            status         = TicketStatus.OPEN,
            priority       = priority,
            category       = category,
            supplierId     = supplierId,
            supplierName   = supplierName,
            affectedRegion = affectedRegion,
            createdBy      = "Usuario Actual",
            assignedTo     = null,
            createdAt      = LocalDateTime.now(),
            updatedAt      = LocalDateTime.now(),
            resolvedAt     = null,
            comments       = emptyList()
        )
        MockDataSource.tickets.add(newTicket)
        _ticketEvents.emit(TicketEvent.TicketCreated(newTicket))
        return ApiResult.Success(newTicket)
    }

    override suspend fun updateTicketStatus(
        ticketId: String,
        status: TicketStatus,
        comment: String?
    ): ApiResult<Ticket> {
        delay(500)
        val index = MockDataSource.tickets.indexOfFirst { it.id == ticketId }
        if (index == -1) return ApiResult.Error(message = UserMessages.Ticket.NOT_FOUND)

        val updated = MockDataSource.tickets[index].let { existing ->
            val newComments = if (!comment.isNullOrBlank()) {
                existing.comments + TicketComment(
                    id         = "CMT-${System.currentTimeMillis()}",
                    authorName = "Usuario Actual",
                    content    = comment,
                    createdAt  = LocalDateTime.now()
                )
            } else existing.comments
            existing.copy(
                status     = status,
                updatedAt  = LocalDateTime.now(),
                resolvedAt = if (status == TicketStatus.RESOLVED) LocalDateTime.now() else existing.resolvedAt,
                comments   = newComments
            )
        }
        MockDataSource.tickets[index] = updated
        _ticketEvents.emit(TicketEvent.StatusUpdated(updated))
        return ApiResult.Success(updated)
    }

    override suspend fun updateTicketPriority(
        ticketId: String,
        priority: TicketPriority,
        reason: String?
    ): ApiResult<Ticket> {
        delay(500)
        val index = MockDataSource.tickets.indexOfFirst { it.id == ticketId }
        if (index == -1) return ApiResult.Error(message = UserMessages.Ticket.NOT_FOUND)

        val updated = MockDataSource.tickets[index].copy(
            priority  = priority,
            updatedAt = LocalDateTime.now()
        )
        MockDataSource.tickets[index] = updated
        _ticketEvents.emit(TicketEvent.PriorityUpdated(updated))
        return ApiResult.Success(updated)
    }
}

// ─────────────────────────────────────────────────────────────
// Implementación Real (para cuando exista backend)
// ─────────────────────────────────────────────────────────────

class RemoteTicketRepository(
    private val apiService: ApiService
) : TicketRepository {

    private val _ticketEvents = MutableSharedFlow<TicketEvent>(extraBufferCapacity = 10)
    override val ticketEvents: SharedFlow<TicketEvent> = _ticketEvents

    override suspend fun getTickets(): ApiResult<List<Ticket>> {
        return try {
            val response = apiService.getTickets()
            if (response.isSuccessful && response.body() != null) {
                val tickets = response.body()!!.tickets
                    .map { it.toDomain() }
                    .sortedBy { it.priority.order }
                ApiResult.Success(tickets)
            } else {
                ApiResult.Error(message = UserMessages.Ticket.LOAD_FAILED, statusCode = response.code())
            }
        } catch (e: Exception) {
            ApiResult.Error(message = UserMessages.Network.COULD_NOT_CONNECT)
        }
    }

    override suspend fun getTicketById(ticketId: String): ApiResult<Ticket> {
        return try {
            val response = apiService.getTicketById(ticketId)
            if (response.isSuccessful && response.body() != null) {
                ApiResult.Success(response.body()!!.toDomain())
            } else {
                ApiResult.Error(message = UserMessages.Ticket.NOT_FOUND, statusCode = response.code())
            }
        } catch (e: Exception) {
            ApiResult.Error(message = UserMessages.Network.COULD_NOT_CONNECT)
        }
    }

    override suspend fun createTicket(
        title: String,
        description: String,
        priority: TicketPriority,
        category: TicketCategory,
        supplierId: String,
        supplierName: String,
        affectedRegion: String
    ): ApiResult<Ticket> {
        return try {
            val response = apiService.createTicket(
                CreateTicketRequest(title, description, priority.name, category.name, supplierId, affectedRegion)
            )
            if (response.isSuccessful && response.body() != null) {
                val ticket = response.body()!!.toDomain()
                _ticketEvents.emit(TicketEvent.TicketCreated(ticket))
                ApiResult.Success(ticket)
            } else {
                ApiResult.Error(message = UserMessages.Ticket.CREATE_FAILED, statusCode = response.code())
            }
        } catch (e: Exception) {
            ApiResult.Error(message = UserMessages.Network.COULD_NOT_CONNECT)
        }
    }

    override suspend fun updateTicketStatus(
        ticketId: String,
        status: TicketStatus,
        comment: String?
    ): ApiResult<Ticket> {
        return try {
            val response = apiService.updateTicketStatus(ticketId, UpdateTicketStatusRequest(status.name, comment))
            if (response.isSuccessful && response.body() != null) {
                val ticket = response.body()!!.toDomain()
                _ticketEvents.emit(TicketEvent.StatusUpdated(ticket))
                ApiResult.Success(ticket)
            } else {
                ApiResult.Error(message = UserMessages.Ticket.STATUS_UPDATE_FAIL, statusCode = response.code())
            }
        } catch (e: Exception) {
            ApiResult.Error(message = UserMessages.Network.COULD_NOT_CONNECT)
        }
    }

    override suspend fun updateTicketPriority(
        ticketId: String,
        priority: TicketPriority,
        reason: String?
    ): ApiResult<Ticket> {
        return try {
            val response = apiService.updateTicketPriority(ticketId, UpdateTicketPriorityRequest(priority.name, reason))
            if (response.isSuccessful && response.body() != null) {
                val ticket = response.body()!!.toDomain()
                _ticketEvents.emit(TicketEvent.PriorityUpdated(ticket))
                ApiResult.Success(ticket)
            } else {
                ApiResult.Error(message = UserMessages.Ticket.PRIORITY_UPDATE_FAIL, statusCode = response.code())
            }
        } catch (e: Exception) {
            ApiResult.Error(message = UserMessages.Network.COULD_NOT_CONNECT)
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Extensión privada: TicketDto → Ticket (dominio)
// ─────────────────────────────────────────────────────────────

private fun TicketDto.toDomain(): Ticket {
    val fmt = java.time.format.DateTimeFormatter.ISO_DATE_TIME
    return Ticket(
        id             = id,
        title          = title,
        description    = description,
        status         = TicketStatus.valueOf(status),
        priority       = TicketPriority.valueOf(priority),
        category       = TicketCategory.valueOf(category),
        supplierId     = supplierId,
        supplierName   = supplierName,
        affectedRegion = affectedRegion,
        createdBy      = createdBy,
        assignedTo     = assignedTo,
        createdAt      = java.time.LocalDateTime.parse(createdAt, fmt),
        updatedAt      = java.time.LocalDateTime.parse(updatedAt, fmt),
        resolvedAt     = resolvedAt?.let { java.time.LocalDateTime.parse(it, fmt) },
        comments       = comments.map { c ->
            TicketComment(
                id         = c.id,
                authorName = c.authorName,
                content    = c.content,
                createdAt  = java.time.LocalDateTime.parse(c.createdAt, fmt)
            )
        }
    )
}