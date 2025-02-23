package eu.mobilebear.cryptomarketplace.data.analytics

import android.os.Bundle
import eu.mobilebear.cryptomarketplace.domain.analytics.AnalyticsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.CustomKeysAndValues
import com.google.firebase.crashlytics.FirebaseCrashlytics
import eu.mobilebear.cryptomarketplace.data.TimeProvider
import eu.mobilebear.cryptomarketplace.data.remoteconfig.RemoteConfigManager

class AnalyticsLoggerImpl(
    remoteConfigManager: RemoteConfigManager,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val fireBaseCrashlytics: FirebaseCrashlytics,
    private val timeProvider: TimeProvider
) : AnalyticsLogger {

    private val defaultEventProperties: MutableMap<String, String> = hashMapOf(
        SYSTEM to ANDROID,
        DEVICE to android.os.Build.DEVICE,
        MANUFACTURER to android.os.Build.MANUFACTURER,
        SDK_VERSION to android.os.Build.VERSION.CODENAME,
        SDK_VERSION_CODE to android.os.Build.VERSION.SDK_INT.toString(),
        ANALYTICS to remoteConfigManager.isAnalyticsEnabled.toString()
    )

    override fun logEvent(name: String, params: Map<String, String>) {
        val paramsWithTimeStamp = params.toMutableMap()
        paramsWithTimeStamp[TIMESTAMP] = timeProvider.getDateTime().toString()
        firebaseAnalytics.logEvent(name, paramsWithTimeStamp.toBundle())
        logCrashlyticsEvent(name, paramsWithTimeStamp)
    }

    override fun logException(throwable: Throwable, params: Map<String, String>) {
        val builder = CustomKeysAndValues.Builder()
        params.forEach { builder.putString(it.key, it.value) }
        fireBaseCrashlytics.setCustomKeys(builder.build())
        fireBaseCrashlytics.recordException(throwable)
    }

    override fun setDeviceId(deviceId: String) {
        defaultEventProperties[DEVICE_ID] = deviceId
        setUpFirebaseAnalytics()
        setUpFirebaseCrashlytics()
    }

    private fun setUpFirebaseAnalytics() {
        firebaseAnalytics.setUserId(defaultEventProperties[DEVICE_ID])
        firebaseAnalytics.setDefaultEventParameters(defaultEventProperties.toBundle())
        defaultEventProperties.setUserProperties()
    }

    private fun setUpFirebaseCrashlytics() {
        fireBaseCrashlytics.setUserId(defaultEventProperties[DEVICE_ID].orEmpty())
        val builder = CustomKeysAndValues.Builder()
        defaultEventProperties.forEach { builder.putString(it.key, it.value) }
        fireBaseCrashlytics.setCustomKeys(builder.build())
    }

    private fun logCrashlyticsEvent(name: String, params: Map<String, String>) {
        val builder = CustomKeysAndValues.Builder()
        params.forEach { builder.putString(it.key, it.value) }
        val customKeysAndValues = builder.build()
        fireBaseCrashlytics.setCustomKeys(customKeysAndValues)
        fireBaseCrashlytics.log(name)
    }

    private fun Map<String, String>.toBundle(): Bundle = Bundle().apply { forEach { putString(it.key, it.value) } }

    private fun Map<String, String>.setUserProperties() =
        forEach { firebaseAnalytics.setUserProperty(it.key, it.value) }

    companion object {
        private const val SYSTEM = "SYSTEM"
        private const val ANDROID = "ANDROID"
        private const val SDK_VERSION = "SDK_VERSION"
        private const val SDK_VERSION_CODE = "SDK_VERSION_CODE"
        private const val MANUFACTURER = "MANUFACTURER"
        private const val DEVICE = "DEVICE"
        private const val DEVICE_ID = "DEVICE_ID"
        private const val TIMESTAMP = "TIMESTAMP"
        private const val ANALYTICS = "ANALYTICS"
    }
}