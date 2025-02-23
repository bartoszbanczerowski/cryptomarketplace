package eu.mobilebear.cryptomarketplace.data

import java.time.LocalDateTime
import javax.inject.Inject

class TimeProvider @Inject constructor() {
    fun getDateTime(): LocalDateTime = LocalDateTime.now()
}