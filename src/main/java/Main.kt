package org.example

import apiMysql.ConMysql


fun main() {

        try {
                ConMysql("jdbc:mysql://192.168.1.100:3306/javaProject", "root", "123456")
                        .use { conMysql ->
                        conMysql.executeQuery("username", arrayListOf("name_id"))
                }
        } catch (e: Exception) {
                e.printStackTrace()
        }

}