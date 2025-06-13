package com.gq97a6.firewall.command

//command <placeholder>
annotation class RequiredParam(val placeholder: String, val description: String)

//command -t
annotation class ShortFlagParam(val tag: Char, val description: String)

//command --tag
annotation class LongFlagParam(val tag: String, val description: String)

//command -t <placeholder>
annotation class ShortOptionParam(val tag: Char, val placeholder: String,  val description: String)

//command --tag <placeholder>
annotation class LongOptionParam(val tag: String, val placeholder: String,  val description: String)


interface PluginCommandParams