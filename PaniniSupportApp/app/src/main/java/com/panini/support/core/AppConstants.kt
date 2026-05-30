package com.panini.support.core

object AppConstants {

    object Api {
        const val BASE_URL = "https://api.panini-support.com/v1/"

        object Paths {
            const val AUTH_LOGIN  = "auth/login"
            const val AUTH_LOGOUT = "auth/logout"

            const val TICKETS             = "tickets"
            const val TICKET_BY_ID        = "tickets/{ticketId}"
            const val TICKET_STATUS       = "tickets/{ticketId}/status"
            const val TICKET_PRIORITY     = "tickets/{ticketId}/priority"
        }
    }

    object Session {
        const val KEY_TOKEN = "session_token"
        const val KEY_USER  = "session_user"
    }
}
