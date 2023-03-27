package com.HomeApp.util

import android.util.Log
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject

/** The Api Connector has all the functions for talking to the API,
 *  each function will return an onRespond Callback that has an parameter of
 *  Api Result
 *  */
object ApiConnector {

    // Init HTTP Client
    private var client: OkHttpClient = OkHttpClient()


    /** Api call that requires email and password,
     * in the result there is an token that can be saved for authenticated calls * */
    fun login(
        email: String,
        Password: String,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val urlPath = "/api/users/login"

        val formBody: RequestBody = FormBody.Builder()
            .add("email", email)
            .add("password", Password)
            .build()

        val request: Request = Request.Builder()
            .url(DB_ADDR + urlPath)
            .post(formBody)
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
        val urlPath = "/api/users/signin"

        val formBody: RequestBody = FormBody.Builder()
            .add("name", name)
            .add("email", email)
            .add("password", password)
            .build()

        val request: Request = Request.Builder()
            .url(DB_ADDR + urlPath)
            .post(formBody)
            .build()

        onRespond(callAPI(request))
    }

    /** Api call to request token for change password */
    fun requestChangePassword(
        email: String,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val urlPath = "/api/users/reset_request"

        val formBody: RequestBody = FormBody.Builder()
            .add("email", email)
            .build()

        val request: Request = Request.Builder()
            .url(DB_ADDR + urlPath)
            .post(formBody)
            .build()
        onRespond(callAPI(request))
    }

    /** Api call to change password */
    fun changePassword(
        token: String,
        password: String,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val urlPath = "/api/users/reset"
        val formBody: RequestBody = FormBody.Builder()
            .add("password", password)
            .build()

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .put(formBody)
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
        val urlPath = "/api/user/remove"
        val formBody: RequestBody = FormBody.Builder()
            .add("email", email)
            .build()

        val request: Request = Request.Builder()
            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .delete(formBody)
            .build()
        onRespond(callAPI(request))
    }

    /** Api call for device actions */
    fun action(
//        token: String,
        id: String,
        state: String,
        type: String,
        onRespond: (result: ApiResult) -> Unit
    ) {
        val urlPath = "/api/devices/actions"
        val formBody: RequestBody = FormBody.Builder()
            .add("id", id)
            .add("state", state)
            .add("type", type)
            .build()

        val request: Request = Request.Builder()
//            .header(AUTH_TOKEN_NAME, token)
            .url(DB_ADDR + urlPath)
            .put(formBody)
            .build()

        onRespond(ApiConnector.callAPI(request))
    }

    private fun callAPI(request: Request): ApiResult {
        return try {
            Log.d("callAPI", request.url.toString())
            val apiResult = client.newCall(request).execute()
            Log.d("callAPI", apiResult.message)
            val jsonData = apiResult.body?.string() ?: "{}"
            ApiResult(jsonData, apiResult.code)
        } catch (e: java.lang.Exception) {
            Log.d("callAPI", "Crash")
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
            200 -> {
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
