package com.gq97a6.firewall.commands

class CommandArguments(args: Array<out String>?) {
    val flags = mutableListOf<Char>()
    val values = mutableMapOf<String, String>()
    val none = mutableListOf<String>()

    fun f(f: Char) = flags.contains(f)
    fun v(v: String): String? = values[v]
    fun n(i: Int): String = none[i]

    fun chk(f: Int = 0, v: Int = 0, n: Int = 0) =
        flags.size >= f && values.size >= v && none.size >= n

    init {
        args?.let { a ->
            var i = 0

            while (i < a.size) {
                if (a[i].startsWith("--") && i < a.lastIndex) {
                    values[a[i].substring(2)] = a[i + 1]
                    i += 1
                } else if (a[i].startsWith("-")) {
                    a[i].substring(1).forEach { flags.add(it) }
                } else none.add(a[i])

                i += 1
            }
        }
    }
}