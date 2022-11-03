package com.gq97a6.firewall

import com.gq97a6.firewall.Firewall.Companion.plugin
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.*

object DB {
    private val conProps = Properties().apply {
        put("user", "plugin")
        put("password", "xsT48lyW5Js74sNc")
    }

    private val connection
        get() = try {
            DriverManager.getConnection("jdbc:mysql://db:3306/firewall:hub", conProps)
        } catch (e: Exception) {
            plugin.logger.info("#|0| $e")
            null
        }

    init {
        Class.forName("com.mysql.cj.jdbc.Driver")
        DriverManager.setLoginTimeout(1)
    }

    fun initialize() {
        connection?.apply {
            try {
                prepareStatement("CREATE TABLE `firewall`.`codes` ( `id` INT NOT NULL AUTO_INCREMENT , `ip` VARCHAR(15) NOT NULL , `username` VARCHAR(16) NOT NULL , `code` VARCHAR(5) NOT NULL , `mc_uuid` VARCHAR(36) NOT NULL , `added` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`id`)) ENGINE = InnoDB;").execute()
                prepareStatement("CREATE TABLE `firewall`.`links` ( `id` INT NOT NULL AUTO_INCREMENT , `ip` VARCHAR(15) NOT NULL , `username` VARCHAR(16) NOT NULL , `dc_uuid` VARCHAR(20) NOT NULL , `mc_uuid` VARCHAR(36) NOT NULL , `added` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`)) ENGINE = InnoDB;").execute()
            } catch (e: Exception) {
                plugin.logger.info("|1| $e")
            }
        }
    }

    fun <T> runAction(action: Connection.() -> T) = connection?.let {
        try {
            action(it)
        } catch (e: Exception) {
            plugin.logger.info("|2| $e")
            null
        }
    }

    fun execute(query: String) = connection?.let {
        try {
            it.execute(query)
        } catch (e: Exception) {
            plugin.logger.info("|3| $e")
            null
        }
    }

    fun executeQuery(query: String) = connection?.let {
        try {
            it.executeQuery(query)
        } catch (e: Exception) {
            plugin.logger.info("|4| $e")
            null
        }
    }

    fun Connection.executeQuery(query: String): ResultSet? = this.prepareStatement(query).executeQuery()
    fun Connection.execute(query: String) = this.prepareStatement(query).execute()
}