package com.panini.support.core

object UserMessages {

    object Auth {
        const val INVALID_CREDENTIALS = "Correo o contraseña incorrectos. Intente nuevamente."
        const val LOGIN_FAILED        = "No se pudo iniciar sesión. Intente más tarde."
        const val LOGIN_JSON_MISMATCH = "Respuesta inesperada del servidor."
        const val SESSION_EXPIRED     = "Su sesión ha expirado. Inicie sesión nuevamente."
    }

    object Network {
        const val COULD_NOT_CONNECT = "No se pudo conectar con el servidor. Verifique su conexión."
        const val TIMEOUT           = "La solicitud tardó demasiado. Intente nuevamente."
        const val UNKNOWN_ERROR     = "Ocurrió un error inesperado."
    }

    object Ticket {
        const val CREATE_SUCCESS      = "Ticket creado exitosamente."
        const val CREATE_FAILED       = "No se pudo crear el ticket. Intente nuevamente."
        const val STATUS_UPDATE_OK    = "Estado actualizado correctamente."
        const val STATUS_UPDATE_FAIL  = "No se pudo actualizar el estado."
        const val PRIORITY_UPDATE_OK  = "Prioridad actualizada correctamente."
        const val PRIORITY_UPDATE_FAIL = "No se pudo actualizar la prioridad."
        const val NOT_FOUND           = "Ticket no encontrado."
        const val LOAD_FAILED         = "No se pudieron cargar los tickets."
    }
}
