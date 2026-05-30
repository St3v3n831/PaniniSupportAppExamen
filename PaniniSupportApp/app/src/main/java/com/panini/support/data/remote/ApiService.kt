package com.panini.support.data.remote

import com.panini.support.core.AppConstants
import com.panini.support.data.remote.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST(AppConstants.Api.Paths.AUTH_LOGIN)
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST(AppConstants.Api.Paths.AUTH_LOGOUT)
    suspend fun logout(): Response<Unit>

    @GET(AppConstants.Api.Paths.TICKETS)
    suspend fun getTickets(
        @Query("status")   status: String?   = null,
        @Query("priority") priority: String? = null,
        @Query("category") category: String? = null,
        @Query("page")     page: Int         = 1,
        @Query("size")     size: Int         = 20
    ): Response<TicketListResponse>

    @GET(AppConstants.Api.Paths.TICKET_BY_ID)
    suspend fun getTicketById(
        @Path("ticketId") ticketId: String
    ): Response<TicketDto>

    @POST(AppConstants.Api.Paths.TICKETS)
    suspend fun createTicket(
        @Body request: CreateTicketRequest
    ): Response<TicketDto>

    @PATCH(AppConstants.Api.Paths.TICKET_STATUS)
    suspend fun updateTicketStatus(
        @Path("ticketId") ticketId: String,
        @Body request: UpdateTicketStatusRequest
    ): Response<TicketDto>

    @PATCH(AppConstants.Api.Paths.TICKET_PRIORITY)
    suspend fun updateTicketPriority(
        @Path("ticketId") ticketId: String,
        @Body request: UpdateTicketPriorityRequest
    ): Response<TicketDto>
}
