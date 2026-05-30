package com.panini.support.data.remote.model

import com.google.gson.annotations.SerializedName
data class LoginRequest(
    @SerializedName("email")    val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("user")  val user: UserDto
)

data class UserDto(
    @SerializedName("id")    val id: String,
    @SerializedName("name")  val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("role")  val role: String
)

// ─────────────────────────────────────────────────────────────
// Ticket DTOs
// ─────────────────────────────────────────────────────────────

data class TicketDto(
    @SerializedName("id")             val id: String,
    @SerializedName("title")          val title: String,
    @SerializedName("description")    val description: String,
    @SerializedName("status")         val status: String,
    @SerializedName("priority")       val priority: String,
    @SerializedName("category")       val category: String,
    @SerializedName("supplierId")     val supplierId: String,
    @SerializedName("supplierName")   val supplierName: String,
    @SerializedName("affectedRegion") val affectedRegion: String,
    @SerializedName("createdBy")      val createdBy: String,
    @SerializedName("assignedTo")     val assignedTo: String?,
    @SerializedName("createdAt")      val createdAt: String,
    @SerializedName("updatedAt")      val updatedAt: String,
    @SerializedName("resolvedAt")     val resolvedAt: String?,
    @SerializedName("comments")       val comments: List<TicketCommentDto> = emptyList()
)

data class TicketCommentDto(
    @SerializedName("id")         val id: String,
    @SerializedName("authorName") val authorName: String,
    @SerializedName("content")    val content: String,
    @SerializedName("createdAt")  val createdAt: String
)

data class TicketListResponse(
    @SerializedName("tickets")    val tickets: List<TicketDto>,
    @SerializedName("totalCount") val totalCount: Int,
    @SerializedName("page")       val page: Int,
    @SerializedName("size")       val size: Int
)

data class CreateTicketRequest(
    @SerializedName("title")          val title: String,
    @SerializedName("description")    val description: String,
    @SerializedName("priority")       val priority: String,
    @SerializedName("category")       val category: String,
    @SerializedName("supplierId")     val supplierId: String,
    @SerializedName("affectedRegion") val affectedRegion: String
)

data class UpdateTicketStatusRequest(
    @SerializedName("status")  val status: String,
    @SerializedName("comment") val comment: String? = null
)

data class UpdateTicketPriorityRequest(
    @SerializedName("priority") val priority: String,
    @SerializedName("reason")   val reason: String? = null
)
