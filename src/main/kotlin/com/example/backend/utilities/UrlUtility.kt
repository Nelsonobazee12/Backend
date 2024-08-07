package com.example.backend.utilities

import jakarta.servlet.http.HttpServletRequest


object UrlUtility {

    fun getApplicationUrl(request: HttpServletRequest): String {
        val appUrl = request.requestURI
        return appUrl.replace(request.servletPath, "")
    }

    fun getApplicationUrl(host: String, port: Int): String {
        return "https://$host:$port"
    }
}
