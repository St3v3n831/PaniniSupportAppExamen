package com.panini.support.data.remote.model

import com.panini.support.domain.model.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME

fun UserDto.toDomain() = User(
    id    = id,
    name  = name,
    email = email,
    role  = role
)

fun TicketDto.toDomain() = Ticket(
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
    createdAt      = LocalDateTime.parse(createdAt, ISO_FORMATTER),
    updatedAt      = LocalDateTime.parse(updatedAt, ISO_FORMATTER),
    resolvedAt     = resolvedAt?.let { LocalDateTime.parse(it, ISO_FORMATTER) },
    comments       = comments.map { it.toDomain() }
)

fun TicketCommentDto.toDomain() = TicketComment(
    id         = id,
    authorName = authorName,
    content    = content,
    createdAt  = LocalDateTime.parse(createdAt, ISO_FORMATTER)
)
