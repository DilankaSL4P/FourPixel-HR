package com.fourpixel.fourpixelhrapplication.client

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: UserData // Fix: Match the API structure

)

data class UserData(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: User
)

data class User(
    @SerializedName("name") val name: String, // Fix: Map correctly to "name" inside "user"
    @SerializedName("email") val email: String,
    @SerializedName("image_url") val imageUrl: String?
)

interface ApiService {
    @POST("api/v1/auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse> // Updated return type
}
