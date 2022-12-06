package com.gq97a6.firewall

infix fun String?.ifNone(str: String) = if (this?.isBlank() == false) this else str
fun Array<out String>.param(key: String) = this.indexOf(key).let { if (it in 0..this.size - 2) this[it + 1] else null }
fun String.pad(len: Int = 8): String {
    var str: String
    (len - this.length).let {
        it.floorDiv(2).let { half ->
            str = this.padEnd(half + this.length, ' ')
            str = str.padStart(len, ' ')
        }
    }
    return str
}