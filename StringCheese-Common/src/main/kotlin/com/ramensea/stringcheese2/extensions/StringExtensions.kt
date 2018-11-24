package com.ramensea.stringcheese2.extensions


fun String.lowerFirstCharacter(): String {
    if (this.length < 1) return this.toLowerCase()
    return this[0].toLowerCase() + this.removeRange(0,1)
}
fun String.upperFirstCharacter(): String {
    if (this.length < 1) return this.toUpperCase()
    return this[0].toUpperCase() + this.removeRange(0,1)
}