package eu.mobilebear.cryptomarketplace.data.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import timber.log.Timber
import javax.inject.Inject

class RemoteConfigManager @Inject constructor(private val remoteConfig: FirebaseRemoteConfig) {

    private val _effect = Channel<Unit>()
    val effect = _effect.receiveAsFlow()

    val isAnalyticsEnabled: Boolean
        get() = remoteConfig.getBoolean(ANALYTICS)

    fun fetchAndActivate() {
        try {
            remoteConfig.fetchAndActivate().addOnCompleteListener { _effect.trySend(Unit) }
        } catch (throwable: Throwable) {
            Timber.e("Firebase remote Config fetch Error")
            _effect.trySend(Unit)
        }
    }

    companion object {
        private const val ANALYTICS = "analytics"
    }
}