package com.anjuutspam.livenessfeature.api

import com.anjuutspam.livenessfeature.model.LivenessResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST("/v1/credenza/faces/liveness")
    suspend fun detectLiveness(
        @Part image: MultipartBody.Part,
        @Part ("description") description: RequestBody
    ): Response<LivenessResponse>

}
