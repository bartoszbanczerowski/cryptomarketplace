package eu.mobilebear.cryptomarketplace.domain.analytics

interface AnalyticsLogger {
    fun logEvent(name: String, params: Map<String, String> = hashMapOf())
    fun logException(throwable: Throwable, params: Map<String, String> = hashMapOf())
    fun setDeviceId(deviceId: String)
}