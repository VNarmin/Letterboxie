package com.example.letterboxie.data.remote

import com.example.letterboxie.utilities.Constants.API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class KeyInterceptor : Interceptor {
    override fun intercept(chain : Interceptor.Chain) : Response {
        val request = chain.request()
        val requestURL = request.url.newBuilder().addQueryParameter("api_key", API_KEY).build()
        val updatedRequest = request.newBuilder().url(requestURL).build()
        return chain.proceed(updatedRequest)
    }
}