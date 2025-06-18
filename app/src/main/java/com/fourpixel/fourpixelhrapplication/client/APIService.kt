package com.fourpixel.fourpixelhrapplication.client

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Header
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import com.google.gson.JsonElement

//Login
data class LoginRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: UserData

)

data class UserData(
    @SerializedName("token") val token: String,
    @SerializedName("user") val user: User
)

//User Data
data class User(
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("roles") val roles: List<Role>?
)

data class Role(
    @SerializedName("name") val name: String,
    @SerializedName("display_name") val displayName: String
)

//Project Data
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

data class Task(
    @SerializedName("id") val id: Int,
    @SerializedName("heading") val heading: String,
    @SerializedName("start_date") val startDate: String?,
    @SerializedName("due_date") val dueDate: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("status") val status: String,
    @SerializedName("project") val project: Project?,
    @SerializedName("users") val users: List<User>?,
    @SerializedName("subtasks") val subtasks: List<Subtask>?
)


data class Subtask(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("start_date") val startDate: String?,
    @SerializedName("due_date") val dueDate: String?,
    @SerializedName("status") val status: String,
    @SerializedName("assigned_to") val assignedTo: Any?
)

data class TaskResponse(
    @SerializedName("data") val data: List<Task>,
    @SerializedName("meta") val meta: Meta
)

//Leaves
data class LeaveResponse(
    @SerializedName("data") val data: List<Leave>,
    @SerializedName("meta") val meta: Meta
)

data class Leave(
    @SerializedName("id") val id: Int,
    @SerializedName("leave_type_id") val leaveTypeId: Int,
    @SerializedName("leave_date") val leaveDate: String,
    @SerializedName("reason") val reason: String,
    @SerializedName("status") val status: String,
    @SerializedName("duration") val duration: String,
    @SerializedName("half_day_type") val halfDayType: String?,
    @SerializedName("unique_id") val uniqueId: String
)

//Notices
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

// ClockIN
data class ClockInRequest(
    @SerializedName("work_from_type") val workFromType: String,
    @SerializedName("working_from") val workingFrom: String,
    @SerializedName("currentLatitude") val currentLatitude: Double?,
    @SerializedName("currentLongitude") val currentLongitude: Double?
)

data class ClockInResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ClockInSuccessData?
)


data class ClockInSuccessData(
    @SerializedName("time") val time: String
)

//Attendance and Clock out classes
data class TodayAttendanceResponse(
    @SerializedName("data") val data: TodayAttendanceRootData?
)

data class TodayAttendanceData(
    @SerializedName("id") val id: Int,
    @SerializedName("clock_in_time") val clockInTime: String,
    @SerializedName("clock_out_time") val clockOutTime: String?,
    @SerializedName("work_from_type") val workFromType: String?,
    @SerializedName("working_from") val workingFrom: String?,
    @SerializedName("currentLatitude") val currentLatitude: String?,
    @SerializedName("currentLongitude") val currentLongitude: String?
)

data class TodayAttendanceRootData(
    @SerializedName("attendance")
    val attendanceRecord: AttendanceRecord?,

    @SerializedName("office_hours_passed")
    val officeHoursPassed: Boolean?,
    val time: String?,
    @SerializedName("ip_address")
    val ipAddress: String?,
    @SerializedName("remaining_clock_in")
    val remainingClockIn: Int?
)

data class AttendanceRecord(
    val id: Int,
    @SerializedName("company_id")
    val companyId: Int?,
    @SerializedName("location_id")
    val locationId: Int?,
    @SerializedName("clock_in_time")
    val clockInTime: String?,
    @SerializedName("clock_out_time")
    val clockOutTime: String?,
    @SerializedName("auto_clock_out")
    val autoClockOut: Int?,
    @SerializedName("clock_in_ip")
    val clockInIp: String?,
    @SerializedName("clock_out_ip")
    val clockOutIp: String?,
    @SerializedName("working_from")
    val workingFrom: String?,
    val late: String?,
    @SerializedName("half_day")
    val halfDay: String?,
    @SerializedName("half_day_type")
    val halfDayType: String?,
    @SerializedName("added_by")
    val addedBy: Int?,
    @SerializedName("last_updated_by")
    val lastUpdatedBy: Int?,
    val latitude: String?,
    val longitude: String?,
    @SerializedName("shift_start_time")
    val shiftStartTime: String?,
    @SerializedName("shift_end_time")
    val shiftEndTime: String?,
    @SerializedName("employee_shift_id")
    val employeeShiftId: Int?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("work_from_type")
    val workFromType: String?,
    @SerializedName("overwrite_attendance")
    val overwriteAttendance: String?,
    @SerializedName("clock_in_date")
    val clockInDate: String?,
    val company: CompanyDetails?
)

data class CompanyDetails(
    val id: Int?,
    @SerializedName("logo_url")
    val logoUrl: String?,
    @SerializedName("login_background_url")
    val loginBackgroundUrl: String?,
    @SerializedName("moment_date_format")
    val momentDateFormat: String?,
    @SerializedName("favicon_url")
    val faviconUrl: String?
)

data class GenericResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Any?
)

//Apply Leave
data class LeaveItemUser(
    @SerializedName("id") val id: Int?
)

data class LeaveItemType(
    @SerializedName("id") val id: Int?
)

data class LeaveItem(
    @SerializedName("leave_date") val leaveDate: String?,
    @SerializedName("user") val user: LeaveItemUser?,
    @SerializedName("type") val type: LeaveItemType?,
    @SerializedName("reason") val reason: String?,
    @SerializedName("duration") val duration: String,
    @SerializedName("status") val status: String
)

data class AddLeaveRequest(
    @SerializedName("item") val item: LeaveItem,
    @SerializedName("users") val users: List<Any>,
    @SerializedName("types") val types: List<Any>,
    @SerializedName("status") val status: String,
    @SerializedName("error") val error: JsonElement?,
    @SerializedName("push") val push: JsonElement?
)


interface ApiService {
    @POST("api/v1/auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @GET("api/v1/project")
    suspend fun getProjects(
        @Header("Authorization") token: String
    ): Response<ProjectResponse>

    @GET("api/v1/task/me?order=id desc&limit=1000&filters=board_column_id ne \"4\" and task_user_id eq \"2\"&fields=id,heading,due_date,status,is_private,project{id,project_name},users{id,name,image,image_url},board_column{id,column_name,slug,label_color},category{id,category_name}")
    suspend fun getMyTasks(
        @Header("Authorization") token: String
    ): Response<TaskResponse>

    @GET("api/v1/notice-board")
    suspend fun getNotices(
        @Header("Authorization") token: String
    ): Response<NoticeResponse>

    @GET("api/v1/leave")
    suspend fun getLeaves(
        @Header("Authorization") token: String
    ): Response<LeaveResponse>

    @POST("api/v1/attendance/clock-in")
    suspend fun clockIn(
        @Header("Authorization") token: String,
        @Body body: ClockInRequest
    ): Response<ClockInResponse>

    @GET("api/v1/attendance/today")
    suspend fun getTodayAttendance(
        @Header("Authorization") token: String
    ): Response<TodayAttendanceResponse>

    @POST("api/v1/attendance/clock-out")
    suspend fun clockOut(
        @Header("Authorization") token: String
    ): Response<GenericResponse>




}

