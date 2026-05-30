package com.panini.support.navigation

sealed class Screen(val route: String) {
    object Login         : Screen("login")
    object TicketList    : Screen("ticket_list")
    object TicketDetail  : Screen("ticket_detail/{ticketId}") {
        fun createRoute(ticketId: String) = "ticket_detail/$ticketId"
    }
    object CreateTicket  : Screen("create_ticket")
    object FeatureFlags  : Screen("feature_flags")
}
