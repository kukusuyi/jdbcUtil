package org.example

import apiMysql.ConMysql


fun main() {

        try {
                ConMysql("jdbc:mysql://110.42.252.180:3306/javaProject", "root", "lqy00000")
                        .use { conMysql ->
                        conMysql.executeQuery("username", arrayListOf("name_id"))
                }
        } catch (e: Exception) {
                e.printStackTrace()
        }

}