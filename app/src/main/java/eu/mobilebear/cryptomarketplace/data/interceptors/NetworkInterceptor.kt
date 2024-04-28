package eu.mobilebear.cryptomarketplace.data.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .build()
        return chain.proceed(request)
    }
}