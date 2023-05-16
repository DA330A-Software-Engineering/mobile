package com.HomeApp.util

import android.content.Context
import android.os.Build
import com.google.gson.Gson
import java.util.*

/** source: https://stackoverflow.com/questions/37695877/how-can-i-decode-jwt-token-in-android */
private fun decryptToken(token: String): Map<String, String> {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return mapOf("error" to "Requires SDK 26")
    val tokenParts = token.split(".")  // Will split token in header [0] and payload [1]
    val charset = charset("UTF-8")
    return try {
        val header = String(
            Base64.getUrlDecoder().decode(tokenParts[0].toByteArray(charset)),
            charset
        )
        val payload =
            String(
                Base64.getUrlDecoder().decode(tokenParts[1].toByteArray(charset)),
                charset
            )
        mapOf("header" to header, "payload" to payload)
    } catch (e: Exception) {
        mapOf("Error" to "Error parsing JWT: $e")
    }
}

fun getEmailFromToken(context: Context): String {
    val decryptedToken = decryptToken(LocalStorage.getToken(context))
    var token = ""
    try {
        token = decryptedToken["payload"]!!.split(",")[0].split(":")[1]
    } catch (e: java.lang.NullPointerException){
        // Token on local storage has expired
    }
    return token
}
