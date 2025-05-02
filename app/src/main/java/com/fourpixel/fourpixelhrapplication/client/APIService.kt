package com.fourpixel.fourpixelhrapplication.client

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Header

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
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("roles") val roles: List<Role>? // ← Add this line
)

data class Role(
    @SerializedName("name") val name: String,
    @SerializedName("display_name") val displayName: String
)

//Project
data class Project(
    @SerializedName("id") val id: Int,
    @SerializedName("project_name") val projectName: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("deadline") val deadline: String?,
    @SerializedName("status") val status: String,
    @SerializedName("company_id") val companyId: Int
)

data class Paging(
    @SerializedName("total") val total: Int
)

data class Meta(
    @SerializedName("paging") val paging: Paging
)

data class ProjectResponse(
    @SerializedName("data") val data: List<Project>,
    @SerializedName("meta") val meta: Meta
)

//Task
data class Task(
    @SerializedName("id") val id: Int,
    @SerializedName("heading") val heading: String,
    @SerializedName("due_date") val dueDate: String?,
    @SerializedName("status") val status: String,
    @SerializedName("assignedUser") val assignedUser: String

)

data class TaskResponse(
    @SerializedName("data") val data: List<Task>,
    @SerializedName("meta") val meta: Meta
)

data class Notice(
    @SerializedName("id") val id: Int,
    @SerializedName("heading") val heading: String,
    @SerializedName("description") val description: String,
    @SerializedName("to") val to: String
)

data class NoticeResponse(
    @SerializedName("data") val data: List<Notice>,
    @SerializedName("meta") val meta: Meta
)


interface ApiService {
    @POST("api/v1/auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @retrofit2.http.GET("api/v1/project")
    suspend fun getProjects(
        @retrofit2.http.Header("Authorization") token: String
    ): Response<ProjectResponse>

    @GET("api/v1/task/me?order=id desc&limit=1000&filters=board_column_id ne \"4\" and task_user_id eq \"2\"&fields=id,heading,due_date,status,is_private,project{id,project_name},users{id,name,image,image_url},board_column{id,column_name,slug,label_color},category{id,category_name}")
    suspend fun getMyTasks(
        @Header("Authorization") token: String
    ): Response<TaskResponse>

    @GET("api/v1/notice-board")
    suspend fun getNotices(
        @Header("Authorization") token: String
    ): Response<NoticeResponse>


}

