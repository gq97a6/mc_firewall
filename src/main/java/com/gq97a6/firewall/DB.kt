package com.gq97a6.firewall

import com.gq97a6.firewall.Firewall.Companion.dbPassword
import com.gq97a6.firewall.Firewall.Companion.dbURl
import com.gq97a6.firewall.Firewall.Companion.dbUser
import com.gq97a6.firewall.Firewall.Companion.plugin
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.util.*

object DB {
    private val conProps = Properties().apply {
        if (dbUser.isNotEmpty()) {
            put("user", dbUser)
            put("password", dbPassword)
        }
    }

    private val connection
        get() = try {
            DriverManager.getConnection(dbURl, conProps)
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
                prepareStatement("CREATE TABLE  if not exists `codes` ( `id` INT NOT NULL AUTO_INCREMENT , `ip` VARCHAR(15) NOT NULL , `username` VARCHAR(16) NOT NULL , `code` VARCHAR(5) NOT NULL , `mc_uuid` VARCHAR(36) NOT NULL , `added` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (`id`)) ENGINE = InnoDB;").execute()
                prepareStatement("CREATE TABLE  if not exists `links` ( `id` INT NOT NULL AUTO_INCREMENT , `ip` VARCHAR(15) NOT NULL , `username` VARCHAR(16) NOT NULL , `dc_uuid` VARCHAR(20) NOT NULL , `mc_uuid` VARCHAR(36) NOT NULL , `added` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`)) ENGINE = InnoDB;").execute()
                prepareStatement("CREATE TABLE  if not exists `bans` ( `id` INT NOT NULL AUTO_INCREMENT , `ip` VARCHAR(15) DEFAULT NULL , `username` VARCHAR(16) DEFAULT NULL , `dc_uuid` VARCHAR(20) DEFAULT NULL , `mc_uuid` VARCHAR(36) DEFAULT NULL , `added` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP , PRIMARY KEY (`id`)) ENGINE = InnoDB;").execute()
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

    fun Connection.executeQuery(query: String): ResultSet? = this.prepareStatement(query).executeQuery()
    fun Connection.execute(query: String) = this.prepareStatement(query).execute()
}