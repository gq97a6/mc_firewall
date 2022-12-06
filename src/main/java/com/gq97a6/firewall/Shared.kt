package com.gq97a6.firewall

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

infix fun String?.ifNone(str: String) = if (this?.isBlank() == false) this else str
fun Array<out String>.param(key: String) = this.indexOf(key).let { if (it in 0..this.size - 2) this[it + 1] else null }

fun TextComponent.c(r: Int, g: Int, b: Int) = this.color(TextColor.color(r, g, b))
fun TextComponent.b() = this.decorate(TextDecoration.BOLD)
fun TextComponent.se(c: String) = this.clickEvent(ClickEvent.suggestCommand((c)))
fun TextComponent.he(c: String) = this.hoverEvent(HoverEvent.showText(Component.text(c)))


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