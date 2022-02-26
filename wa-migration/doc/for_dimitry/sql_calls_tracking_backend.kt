package com.noname.whatsapp.tracker

import java.sql.Types
import java.util.concurrent.ConcurrentHashMap


fun insertPresenceNoteBulkCall(listPhoneDBIdOnIsOnline: ArrayList<Pair<Int, Boolean>>) {
    try {
        OracleHikariDataSource.dataSource.connection.use {
            val sqlQuery =
                "insert into visitnote (phone_id, is_online, add_time) values (?, ?, cast(sys_extract_utc(systimestamp) as date)) LOG ERRORS REJECT LIMIT UNLIMITED"
            it.prepareStatement(sqlQuery).use { ps ->
                for (pair in listPhoneDBIdOnIsOnline) {
                    ps.setInt(1, pair.first)
                    ps.setBoolean(2, pair.second)
                    ps.addBatch()
                }
                ps.executeBatch()
            }
        }
    } catch (ex: Exception) {
        DBManager.log.error("error bulk inserting visitnotes in DB ${ex.message}")
    }
}

fun getAuthkeysByGroup(groupName: String) =
    OracleHikariDataSource.dataSource.connection.use{
        var sqlQuery = "select authkey.phone_num, authkey.payload from authkey join botgroup on (authkey.group_id=botgroup.id) where botgroup.name = ?"
        it.prepareStatement(sqlQuery).use{ps ->
            ps.setString(1, groupName)
            ps.executeQuery().use {rs ->
                generateSequence {
                    if (rs.next()) Pair(rs.getLong(1), rs.getString(2)) else null
                }
                    .filterNotNull()
                    .map {
                        Authkey(it.first, it.second)
                    }
                    .toList()
            }
        }
    }

fun replaceBannedAuthkey(phoneNum: Long, botType: BotType): String?=
    try {
        OracleHikariDataSource.dataSource.connection.use {
            val isTracker = botType == BotType.TRACKER
            it.prepareCall("begin ? := wa_tracking.ban_bot(?,?); end;").use { cstmt ->
                cstmt.registerOutParameter(1, Types.VARCHAR)
                cstmt.setLong(2, phoneNum)
                cstmt.setBoolean(3, isTracker)
                cstmt.execute()
                cstmt.getString(1)
            }
        }
    }
    catch(ex: Exception){
        DBManager.log.error("error replacing banned authkey", ex)
        null
    }

fun replaceQuarantinedAuthkey(phoneNum: Long, botType: BotType): String?=
    try {
        OracleHikariDataSource.dataSource.connection.use {
            val isTracker = botType == BotType.TRACKER
            it.prepareCall("begin ? := wa_tracking.quarantine_bot(?,?); end;").use { cstmt ->
                cstmt.registerOutParameter(1, Types.VARCHAR)
                cstmt.setLong(2, phoneNum)
                cstmt.setBoolean(3, isTracker)
                cstmt.execute()
                cstmt.getString(1)
            }
        }
    }
    catch(ex: Exception){
        DBManager.log.error("error replacing quarantined authkey", ex)
        null
    }

data class Quadruple<out A,out B, out C, out D>(val first:A, val second:B, val third:C, val fourth:D)
fun getSubscriptionNumbersForBot(phoneNum: Long, considerBotFreshHours: Int) :AuthkeyConfig?=
    try {
        OracleHikariDataSource.dataSource.connection.use {
            var sqlQuery =
                """select authkey.phone_num,
|                       subscriptionphone.phone_num,
|                       subscriptionphone.id,
|                       case when cast(sys_extract_utc(systimestamp) as date)-subscriptionphone.add_time<?/24 then 1 else 0 end as isFresh,
|                       case when cast(sys_extract_utc(systimestamp) as date)-authkey.last_change_time<?/24 then 1 else 0 end as botIsFresh
|                  from authkey join subscriptionphone on (subscriptionphone.key_id=authkey.id) where authkey.phone_num = ?""".trimMargin()
            it.prepareStatement(sqlQuery).use { ps ->
                ps.setInt(1,considerBotFreshHours)
                ps.setInt(2,considerBotFreshHours)
                ps.setLong(3, phoneNum)
                ps.executeQuery().use { rs ->
                    generateSequence {
                        if (rs.next()) Quadruple(rs.getLong(1), rs.getLong(2), rs.getInt(3), rs.getBoolean(4) or rs.getBoolean(5)) else null
                    }
                        .filterNotNull()
                        .groupBy { it.first }
                        .mapValues {
                            ConcurrentHashMap<Long, Pair<Int,Boolean>>().apply {
                                for (listItem in it.value)
                                    put(listItem.second, Pair(listItem.third,listItem.fourth))
                            }
                        }
                        .map {
                            AuthkeyConfig(it.key, it.value)
                        }
                        .firstOrNull()
                }
            }
        }
    }
    catch(ex: Exception){
        DBManager.log.error("error getting subscription numbers for bot", ex)
        null
    }

fun updateSubscriptionNumberState(subscriptionPhoneId: Int, isValid: Boolean) =
    try {
            OracleHikariDataSource.dataSource.connection.use {
            it.prepareCall("call wa_tracking.update_subscription_state(?,?)").use { cstmt ->
                cstmt.setInt(1, subscriptionPhoneId)
                cstmt.setBoolean(2, isValid)
                cstmt.executeUpdate()
            }
        }
    }
    catch(ex: Exception){
        DBManager.log.error("error updating subscriptions state", ex)
    }


fun updateAvatar(phoneNum: Long, avatarID: Long, rawImage: ByteArray) {
    try {
        OracleHikariDataSource.dataSource.connection.use {
            it.prepareStatement("update subscriptionphone set avatar_id = ?, avatar = ? where phone_num = ?")
                .use { stmt ->
                    stmt.setLong(1, avatarID)
                    stmt.setBytes(2, rawImage)
                    stmt.setLong(3, phoneNum)
                    stmt.executeUpdate()
                }
        }
    }
    catch(ex: Exception){
        DBManager.log.error("error updating avatar", ex)
    }
}

fun getNewSubscriptionData(rowID: String) =
    try {
        OracleHikariDataSource.dataSource.connection.use {
            it.prepareStatement("select subscriptionphone.phone_num, subscriptionphone.id, subscriptionphone.is_valid from subscriptionphone where subscriptionphone.rowid = ?")
                .use { stmt ->
                    stmt.setString(1, rowID)
                    stmt.executeQuery().use {
                        if (it.next())
                            Triple(it.getLong(1), it.getInt(2), it.getInt(3))
                        else
                            null
                    }
                }
        }
    }
    catch(ex: Exception){
        DBManager.log.error("error getting new subscription data", ex)
        null
    }


fun getNotificationMap() =
    OracleHikariDataSource.dataSource.connection.use {
        it.prepareStatement("select appuser.gcm_token, subscriptionphone.phone_num, subscriptionphone.assigned_name, appuser.app_name from appuser join subscriptionphone on (appuser.id=subscriptionphone.user_id) where notify=1 and key_id is not null and is_removed=0")
            .use { stmt ->
                val resultMap = HashMap<Long, MutableSet<Triple<String, String, String>>>()
                stmt.executeQuery().use {
                    while (it.next()) {
                        resultMap.getOrPut(it.getLong(2)) { mutableSetOf() }.add(
                            Triple(
                                it.getString(1),
                                it.getString(3),
                                it.getString(4) ?: "com.peanutbutter.wastat"
                            )
                        )
                    }
                    resultMap
                }
            }
    }

fun getNotifDeliveryStatus(rowSubscriptionPhoneID: String) =
    try {
        OracleHikariDataSource.dataSource.connection.use {
            it.prepareStatement("select notify, phone_num, gcm_token, assigned_name, app_name from subscriptionphone join appuser on (appuser.id=subscriptionphone.user_id) where subscriptionphone.rowid = ? and key_id is not null")
                .use { stmt ->
                    stmt.setString(1, rowSubscriptionPhoneID)
                    stmt.executeQuery().use {
                        if (it.next())
                            Triple(
                                it.getInt(1),
                                it.getLong(2),
                                Triple(it.getString(3), it.getString(4), it.getString(5) ?: "com.peanutbutter.wastat")
                            )
                        else null
                    }
                }
        }
    }
    catch(ex: Exception){
        DBManager.log.error("error getting notification delivery status", ex)
        null
    }


fun getNotifDeliveryStatusForUser(rowAppUserID: String) =
    try {
        OracleHikariDataSource.dataSource.connection.use {
            it.prepareStatement("select phone_num, gcm_token, assigned_name, app_name from subscriptionphone join appuser on (appuser.id=subscriptionphone.user_id) where appuser.rowid = ? and key_id is not null and is_removed=0")
                .use { stmt ->
                    val result = mutableListOf<Pair<Long, Triple<String, String, String>>>()
                    stmt.setString(1, rowAppUserID)
                    stmt.executeQuery().use {
                        while (it.next())
                            result.add(
                                Pair(
                                    it.getLong(1),
                                    Triple(
                                        it.getString(2),
                                        it.getString(3),
                                        it.getString(4) ?: "com.peanutbutter.wastat"
                                    )
                                )
                            )
                    }
                    result
                }
        }
    }
    catch(ex: Exception){
        DBManager.log.error("error getting notification delivery status for user", ex)
        mutableListOf<Pair<Long, Triple<String, String, String>>>()
    }

fun getUpdatedSubscriptionActiveState(rowID: String) =
    try {
        OracleHikariDataSource.dataSource.connection.use {conn ->
            var result =
                conn.prepareStatement("select authkey.phone_num, subscriptionphone.phone_num, subscriptionphone.id, subscriptionphone.is_removed from subscriptionphone join authkey on (subscriptionphone.key_id=authkey.id) where subscriptionphone.rowid = ?")
                    .use { stmt ->
                        stmt.setString(1, rowID)
                        stmt.executeQuery().use {
                            if (it.next()) {
                                var quad = Quadruple(it.getLong(1), it.getLong(2), it.getInt(3), it.getBoolean(4))
                                if (quad.fourth) {
                                    var sql = "update subscriptionphone set key_id=null where id=?"
                                    conn.prepareStatement(sql).use { ps ->
                                        ps.setInt(1, quad.third)
                                        ps.executeUpdate()
                                    }
                                }
                                quad
                            }
                            else null
                        }
                    }
            result
        }
    }
    catch(ex: Exception){
        DBManager.log.error("error getting updated subscription active state", ex)
        null
    }

fun getUnprocessedPhonesCall() =
    try {
        OracleHikariDataSource.dataSource.connection.use {
            it.prepareStatement("select phone_num, id from (select phone_num, id, add_time, cast(sys_extract_utc(systimestamp) as date) as cur_time from subscriptionphone where is_valid is null) where (cur_time-add_time)*24*60*60>120")
                .use { stmt ->
                    //stmt.fetchSize = 1000
                    val resultList = ArrayList<Triple<Long, Int, Int>>()
                    stmt.executeQuery().use {
                        while (it.next()) {
                            resultList.add(Triple<Long, Int, Int>(it.getLong(1), it.getInt(2), 0))
                        }
                        resultList
                    }
                }
        }
    }
    catch(ex: Exception){
        DBManager.log.error("error getting unprocessed phones", ex)
        ArrayList<Triple<Long, Int, Int>>()
    }
