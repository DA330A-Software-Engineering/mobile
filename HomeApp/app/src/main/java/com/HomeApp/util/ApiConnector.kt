package com.HomeApp.util

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

/** The Api Connector has all the functions for talking to the API,
 *  each function will return an onRespond Callback that has an parameter of
 *  Api Result
 *  */
object ApiConnector {

    // Init HTTP Client
    private var client: OkHttpClient = OkHttpClient()


    // get user data using token
    fun getUserData(token: String, onRespond: (result: ApiResult) -> Unit) {
        val urlPath = "/users"
        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .build()
        onRespond(callAPI(request))
    }

    /** Api call that requires email and password,
     * in the result there is an token that can be saved for authenticated calls * */
    fun login(
        email: String,
        password: String,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val obj = JSONObject()
        obj.put("email", email)
        obj.put("password", password)
        val requestForm = obj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)
        val urlPath = "/users/login"

        val request: Request = Request.Builder()
            .url(DB_ADDR + urlPath)
            .post(requestBody)
            .build()

        onRespond(callAPI(request))
    }

    /** Api Call for creating an account */
    fun createAccount(
        name: String,
        email: String,
        password: String,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val obj = JSONObject()
        obj.put("name", name)
        obj.put("email", email)
        obj.put("password", password)
        val requestForm = obj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)
        val urlPath = "/users/signin"

        val request: Request = Request.Builder()
            .url(DB_ADDR + urlPath)
            .post(requestBody)
            .build()

        onRespond(callAPI(request))
    }

    /** Api call to request token for change password */
    fun requestChangePassword(
        email: String,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val obj = JSONObject()
        obj.put("email", email)
        val requestForm = obj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)
        val urlPath = "/users/reset_request"

        val request: Request = Request.Builder()
            .url(DB_ADDR + urlPath)
            .post(requestBody)
            .build()
        onRespond(callAPI(request))
    }

    /** Api call to change password */
    fun changePassword(
        token: String,
        password: String,
        onRespond: (result: ApiResult) -> Unit
    ) {

        val obj = JSONObject()
        obj.put("password", password)
        val requestForm = obj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)
        val urlPath = "/users/reset"

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .put(requestBody)
            .build()
        onRespond(callAPI(request))
    }

    /** Retrieves the information about the users **/
    fun getUsersData(token: String, onRespond: (result: ApiResult) -> Unit) {
        val urlPath = "/users"
        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(firebaseConfig["databaseURL"] + urlPath)
            .build()
        onRespond(callAPI(request))
    }

    fun getAllUserData(token: String, onRespond: (result: ApiResult) -> Unit) {
        val urlPath = "/users"
        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .build()
        onRespond(callAPI(request))
    }

    /** Api call to delete an user */
    fun deleteUser(
        token: String,
        email: String,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val obj = JSONObject()
        obj.put("email", email)
        val requestForm = obj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)

        val urlPath = "/users/remove"

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .delete(requestBody)
            .build()
        onRespond(callAPI(request))
    }

    /** Api call for device actions */
    fun deviceAction(
        token: String,
        id: String,
        state: JSONObject,
        type: String,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val obj = JSONObject()
        obj.put("id", id)
        obj.put("state", state)
        obj.put("type", type)
        val requestForm = obj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)

        val urlPath = "/devices/actions"

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .put(requestBody)
            .build()
        onRespond(callAPI(request))
    }

    /** Api call for group actions */
    fun groupAction(
        token: String,
        id: String,
        state: JSONObject,
        type: String,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val obj = JSONObject()
        obj.put("id", id)
        obj.put("state", state)
        obj.put("type", type)
        val requestForm = obj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)

        val urlPath = "/groups/actions"

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .put(requestBody)
            .build()
        onRespond(callAPI(request))
    }

    /** Api call to update a device */
    fun updateDevice(
        token: String,
        id: String,
        name: String,
        description: String,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val obj = JSONObject()
        obj.put("name", name)
        obj.put("description", description)
        val requestForm = obj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)

        val urlPath = "/devices/$id"

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .put(requestBody)
            .build()
        onRespond(callAPI(request))
    }

    /** Api call to create a group */
    fun createGroup(
        token: String,
        name: String,
        description: String,
        devices: List<String>,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val obj = JSONObject()
        obj.put("name", name)
        obj.put("description", description)
        obj.put("devices", devices)
        val requestForm = obj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)

        val urlPath = "/groups"

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .post(requestBody)
            .build()
        onRespond(callAPI(request))
    }

    /** Api call to update a group */
    fun updateGroup(
        token: String,
        id: String,
        name: String,
        description: String,
        devices: List<String>,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val obj = JSONObject()
        obj.put("name", name)
        obj.put("description", description)
        obj.put("devices", devices)
        val requestForm = obj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)

        val urlPath = "/groups/$id"

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .put(requestBody)
            .build()
        onRespond(callAPI(request))
    }

    /** Api call to delete a group */
    fun deleteGroup(
        token: String,
        id: String,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val obj = JSONObject()
        val requestForm = obj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)

        val urlPath = "/groups/$id"

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .delete(requestBody)
            .build()
        onRespond(callAPI(request))
    }

    /** Api call to create a routine */
    fun createRoutine(
        token: String,
        name: String,
        description: String,
        schedule: String,
        enabled: Boolean,
        repeatable: Boolean,
        actions: JSONArray,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val obj = JSONObject()
        obj.put("name", name)
        obj.put("description", description)
        obj.put("schedule", schedule)
        obj.put("enabled", enabled)
        obj.put("repeatable", repeatable)
        obj.put("actions", actions)
        val requestForm = obj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)

        Log.d("Request Body", requestForm)

        val urlPath = "/routines"

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .post(requestBody)
            .build()
        onRespond(callAPI(request))
    }

    /** Api call to update a routine */
    fun updateRoutine(
        token: String,
        id: String,
        name: String,
        description: String,
        schedule: String,
        enabled: Boolean,
        repeatable: Boolean,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val obj = JSONObject()
        obj.put("name", name)
        obj.put("description", description)
        obj.put("schedule", schedule)
        obj.put("enabled", enabled)
        obj.put("repeatable", repeatable)
        val requestForm = obj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)

        val urlPath = "/routines/$id"

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .put(requestBody)
            .build()
        onRespond(callAPI(request))
    }

    /** Api call to delete a routine */
    fun deleteRoutine(
        token: String,
        id: String,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val obj = JSONObject()
        val requestForm = obj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)

        val urlPath = "/routines/$id"

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .delete(requestBody)
            .build()
        onRespond(callAPI(request))
    }

    private fun callAPI(request: Request): ApiResult {
        return try {
            Log.d("callAPI", request.url.toString())
            val call = client.newCall(request)
            Log.d("callAPI", call.request().toString())
            val apiResult = client.newCall(request).execute()
            Log.d("callAPI", apiResult.message)
            val jsonData = apiResult.body?.string() ?: "{}"
            ApiResult(jsonData, apiResult.code)
        } catch (e: java.lang.Exception) {
            Log.d("callAPI", e.toString())
            ApiResult("{msg: \"Connection timeout\"}")
        }
    }
}


/** Data class for easily manipulate the respond from the api */
data class ApiResult(
    private val data: String = "{}",
    private val code: Number = 500
) {
    fun status(): HttpStatus {
        return when (code) {
            in 100..299 -> {
                HttpStatus.SUCCESS
            }
            in 400..499 -> {
                HttpStatus.UNAUTHORIZED
            }
            else -> {
                HttpStatus.FAILED
            }
        }
    }

    fun data(): JSONObject {
        return JSONObject(data)
    }

    fun dataArray(): JSONArray {
        return JSONArray(data)
    }
}

/** Http status enum */
enum class HttpStatus {
    SUCCESS,
    UNAUTHORIZED,
    FAILED,
}
