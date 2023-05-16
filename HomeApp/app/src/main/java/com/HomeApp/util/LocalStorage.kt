package com.HomeApp.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.*

/** Data Class that defines the data */
class LocalStorageData : Serializable {
    @SerializedName("token")
    var token: String = ""

    @SerializedName("tokenEmail")
    var tokenEmail: String = ""
}

object LocalStorage {
    private var localStorageData: LocalStorageData = LocalStorageData()
    private const val FILENAME = "localData.json"

    /** Quick function to save a new token */
    fun saveToken(context: Context, token: String) {
        localStorageData.token = token
        saveData(context)
    }

    /** Quick function to save a new email token */
    fun saveTokenEmail(context: Context, emailToken: String) {
        localStorageData.tokenEmail = emailToken
        saveData(context)
    }

    /** Retries the authentication token */
    fun getToken(context: Context): String {
        loadData(context)
        return localStorageData.token
    }

    /** Retries the email token */
    fun getETokenEmail(context: Context): String {
        loadData(context)
        return localStorageData.tokenEmail
    }

    /** Returns all the saved data */
    fun getLocalStorageData(context: Context): LocalStorageData {
        loadData(context)
        return localStorageData
    }

    /** Set new saved data, tip: getLocalStorageData, change it then save it. */
    fun saveLocalStorageData(context: Context, newLocalStorageData: LocalStorageData) {
        localStorageData = newLocalStorageData
        saveData(context)
    }

    /** Saves the LocalStorageData class data in an json file */
    private fun saveData(context: Context) {
        val gson = Gson()
        val json = gson.toJson(localStorageData)
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE)
            fileOutputStream.write(json.toByteArray())
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** Loads the json file data into the LocalStorageData class */
    private fun loadData(context: Context) {
        val gson = Gson()
        var jsonString = ""
        try {
            var fileInputStream: FileInputStream? = null
            fileInputStream = context.openFileInput(FILENAME)
            val inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null

            while (run {
                    text = bufferedReader.readLine()
                    text
                } != null) {
                stringBuilder.append(text)
            }
            jsonString = stringBuilder.toString()
            localStorageData = gson.fromJson(jsonString, LocalStorageData::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** "Clears" the token by updating it to an empty string */
    fun clearToken(context: Context) {
        localStorageData.token = ""
        saveData(context)
    }
}
