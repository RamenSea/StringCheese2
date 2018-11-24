package com.ramensea.stringcheese2.process.expect

import kotlin.js.Date

actual fun getTimeStamp(): String {
    return "$TIME_STAMP_PREFIX${Date().toISOString()}"
}