package com.example.backend.registration

data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T, message: String = "Operation successful") =
            ApiResponse(true, message, data)

        fun <T> error(message: String) = ApiResponse<T>(false, message) // Updated to use T
    }
}
