package com.HomeApp.util

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.cronutils.model.Cron
import com.cronutils.model.CronType
import com.cronutils.model.definition.CronDefinitionBuilder
import com.cronutils.parser.CronParser
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.TemporalAccessor

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
        val formObj = JSONObject()
        formObj.put("email", email)
        formObj.put("password", password)
        val requestForm = formObj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)
        val urlPath = "/api/users/login"

        val request: Request = Request.Builder()
            .url(DB_ADDR + urlPath)
            .header("Content-Type", "application/json")
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
        val formObj = JSONObject()
        formObj.put("name", name)
        formObj.put("email", email)
        formObj.put("password", password)
        val requestForm = formObj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)
        val urlPath = "/api/users/signin"

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
        val formObj = JSONObject()
        formObj.put("email", email)
        val requestForm = formObj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)
        val urlPath = "/api/users/reset_request"

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

        val formObj = JSONObject()
        formObj.put("password", password)
        val requestForm = formObj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)
        val urlPath = "/api/users/reset"

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .put(requestBody)
            .build()
        onRespond(callAPI(request))
    }

    /** Retrieves the information about the users **/
    fun getUsersData(token: String, onRespond: (result: ApiResult) -> Unit) {
        val urlPath = "/api/user"
        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(firebaseConfig["databaseURL"] + urlPath)
            .build()
        onRespond(callAPI(request))
    }

    fun getAllUserData(token: String, onRespond: (result: ApiResult) -> Unit) {
        val urlPath = "/api/users"
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
        val formObj = JSONObject()
        formObj.put("email", email)
        val requestForm = formObj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)

        val urlPath = "/api/user/remove"

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .delete(requestBody)
            .build()
        onRespond(callAPI(request))
    }

    /** Api call for device actions */
    fun action(
//        token: String,
        id: String,
        state: JSONObject,
        type: String,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val formObj = JSONObject()
        formObj.put("id", id)
        formObj.put("state", state)
        formObj.put("type", type)
        Log.d("ACTION", "$formObj")
        val urlPath = "/devices/actions"
        val requestForm = formObj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)

        val token =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6IndlbHRlci50b21AaG90bWFpbC5jb20iLCJpYXQiOjE2ODAyODE4NjB9.X60oo5qZ0I6ZGjyVheDHpGLFkMErQi9r4GVSJJQ6mMc"

        val request: Request = Request.Builder()
//            .header(AUTH_TOKEN_NAME, token)
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .put(requestBody)
            .build()

        onRespond(callAPI(request))
    }

    fun updateDevice(
        token: String,
        id: String,
        name: String,
        description: String,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val formObj = JSONObject()
        formObj.put("name", name)
        formObj.put("description", description)
        val requestForm = formObj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)

        val urlPath = "/api/devices/$id"

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .put(requestBody)
            .build()
        onRespond(callAPI(request))
    }

    fun getGroup(
        token: String,
        id: String,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val urlPath = "/api/groups?id=$id"
        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .get()
            .build()
        onRespond(callAPI(request))
    }

    fun createGroup(
        token: String,
        name: String,
        description: String,
        devices: List<String>,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val formObj = JSONObject()
        formObj.put("name", name)
        formObj.put("description", description)
        formObj.put("devices", devices)
        val requestForm = formObj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)

        val urlPath = "/api/groups"

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .post(requestBody)
            .build()
        onRespond(callAPI(request))
    }

    fun updateGroup(
        token: String,
        id: String,
        name: String,
        description: String,
        devices: List<String>,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val formObj = JSONObject()
        formObj.put("name", name)
        formObj.put("description", description)
        formObj.put("devices", devices)
        val requestForm = formObj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)

        val urlPath = "/api/groups?id=$id"

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .put(requestBody)
            .build()
        onRespond(callAPI(request))
    }

    fun deleteGroup(
        token: String,
        id: String,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val formObj = JSONObject()
        formObj.put("id", id)
        val requestForm = formObj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)

        val urlPath = "/api/groups"

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .delete(requestBody)
            .build()
        onRespond(callAPI(request))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createRoutine(
        token: String,
        name: String,
        description: String,
        schedule: String,
        specific_time: String,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX)
        val cronParser = CronParser(cronDefinition)
        val cronJob: Cron

        try {
            cronJob = cronParser.parse(schedule)
            Log.i("Schedule Parser", cronJob.toString())
        } catch (e: IllegalArgumentException) {
            Log.e("Schedule Parser", schedule)
            return
        }

        val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val dateTime: TemporalAccessor
        try {
            dateTime = dateTimeFormatter.parse(specific_time)
            Log.i("DateTime Parser", dateTime.toString())
        } catch (e: DateTimeParseException) {
            Log.e("DateTime Parser", specific_time)
            return
        }

        val formObj = JSONObject()
        formObj.put("name", name)
        formObj.put("description", description)
        formObj.put("schedule", cronJob)
        formObj.put("specific_time", dateTime)
        val requestForm = formObj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)

        val urlPath = "/api/routines"

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .post(requestBody)
            .build()
        onRespond(callAPI(request))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateRoutine(
        token: String,
        id: String,
        name: String,
        description: String,
        schedule: String,
        specific_time: String,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX)
        val cronParser = CronParser(cronDefinition)
        val cronJob: Cron

        try {
            cronJob = cronParser.parse(schedule)
            Log.i("Schedule Parser", cronJob.toString())
        } catch (e: IllegalArgumentException) {
            Log.e("Schedule Parser", schedule)
            return
        }

        val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val dateTime: TemporalAccessor
        try {
            dateTime = dateTimeFormatter.parse(specific_time)
            Log.i("DateTime Parser", dateTime.toString())
        } catch (e: DateTimeParseException) {
            Log.e("DateTime Parser", specific_time)
            return
        }

        val formObj = JSONObject()
        formObj.put("name", name)
        formObj.put("description", description)
        formObj.put("schedule", cronJob)
        formObj.put("specific_time", dateTime)
        val requestForm = formObj.toString()
        val mediaType = "application/json".toMediaType()
        val requestBody = requestForm.toRequestBody(mediaType)

        val urlPath = "/api/routines?id=$id"

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .put(requestBody)
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
            in 400..500 -> {
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
