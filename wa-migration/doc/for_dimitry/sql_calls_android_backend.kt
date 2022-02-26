package com.noname.android.wa.grpc

import com.noname.android.wa.grpc.proto.ProtobufApiService
import java.sql.Blob
import java.sql.Timestamp
import java.sql.Types
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

fun getSubscriptionsCall(userID: Int) =
    OracleHikariDataSource.dataSource.connection.use{
        var list = ArrayList<ProtobufApiService.SubscriptionData>()
        var sqlQuery = "select phone_num, assigned_name, notify from subscriptionphone where user_id = ?"
        it.prepareStatement(sqlQuery).use{
            it.setInt(1,userID)
            it.executeQuery().use{rs ->
                while(rs.next()){
                    list.add(
                        ProtobufApiService.SubscriptionData.newBuilder()
                            .setPhoneNum(rs.getLong(1))
                            .setAssignedName(rs.getString(2))
                            .setNotificationsEnabled(rs.getInt(3) == 1)
                            .build()
                    )
                }
            }
        }
        list
    }

fun getSessionsCall(userID: Int, tsBegin: Long?, tsEnd: Long?, num: Long): List<Pair<Long,Boolean>>{
    var list = ArrayList<Pair<Long,Boolean>>()
    var isOnline: Boolean
    var addTime: Long
    var localTsBegin = tsBegin?: Timestamp.valueOf(LocalDateTime.now().minusMonths(1)).time
    var localTsEnd = tsEnd?: Timestamp.valueOf(LocalDateTime.now()).time
    OracleHikariDataSource.dataSource.connection.use {
        var sqlQuery =
            "select visitnote.is_online, visitnote.add_time from subscriptionphone join visitnote on (visitnote.phone_id=subscriptionphone.id) " +
                    "where subscriptionphone.user_id=  ? and phone_num = ? and visitnote.add_time between ? and ? order by visitnote.add_time"
        it.prepareStatement(sqlQuery).use {
            it.setInt(1, userID)
            it.setLong(2, num)
            it.setTimestamp(3, Timestamp(localTsBegin))
            it.setTimestamp(4, Timestamp(localTsEnd))
            it.executeQuery().use { rs ->
                while (rs.next()) {
                    isOnline = rs.getBoolean(1)
                    addTime = rs.getTimestamp(2).time.div(1000)
                    list.add(Pair(addTime,isOnline))
                }
            }
        }
    }
    return list
}

fun getAppUsersCall() =
    OracleHikariDataSource.dataSource.connection.use{
        val usersMap = ConcurrentHashMap<String, Int>()
        var sqlQuery = "select id, auth_token from appuser"
        it.prepareStatement(sqlQuery).use{
            it.executeQuery().use{rs ->
                while(rs.next())
                    usersMap[rs.getString((2))]=rs.getInt(1)
            }
        }
        usersMap
    }

fun getNewUserDataCall(rowID: String) =
    OracleHikariDataSource.dataSource.connection.use{
        var sqlQuery = "select id, auth_token from appuser where rowid = ?"
        var result: Pair<String, Int>? = null
        it.prepareStatement(sqlQuery).use{
            it.setString(1, rowID)
            it.executeQuery().use{rs ->
                if(rs.next())
                    result = Pair(rs.getString(2), rs.getInt(1))
            }
        }
        result
    }

fun addNewUserCall(gcmToken: String, appName: String, appVersion: String, androidSecureID: String, deviceFingerprint: String, gsfId: String?) : Pair<String,Int>? {
    OracleHikariDataSource.dataSource.connection.use {
        it.autoCommit=false
        var gcmTokenLoc =
            if(gcmToken == null || gcmToken=="")
                "nulled"
            else
                gcmToken
        var sqlQuery =
            "insert into appuser (reg_time, auth_token, gcm_token, app_name, app_version, android_secure_id, device_fingerprint, gsf_id) values (cast(sys_extract_utc(systimestamp) as date), dbms_random.string('X',30), ?, ?, ?, ?, ?, ?)"
        var generatedColumns = arrayOf("id","auth_token")
        var newUser: Pair<String,Int>? = null
        try {
            it.prepareStatement(sqlQuery, generatedColumns).use {
                it.setString(1, gcmTokenLoc)
                it.setString(2, appName)
                it.setString(3, appVersion)
                it.setString(4, androidSecureID)
                it.setString(5, deviceFingerprint)
                it.setString(6, gsfId)
                it.executeUpdate()
                val rs = it.generatedKeys
                if (rs.next())
                    newUser = Pair(rs.getString(2), rs.getInt(1))
            }

            sqlQuery="insert into payment (user_id, fulfil_time, valid_until, pay_type, subs_amount, price) values (?, cast(sys_extract_utc(systimestamp) as date), cast(sys_extract_utc(systimestamp) as date)+1, 'trial', 1, 0)"
            it.prepareStatement(sqlQuery).use{
                it.setInt(1, newUser!!.second)
                it.executeUpdate()
            }

            it.commit()
        }
        catch(ex: Exception){
            it.rollback()
            throw ex
        }
        return newUser
    }
}

fun addNewSubscriptionCall(userID: Int, phoneNum: Long, assignedName: String) =
    OracleHikariDataSource.dataSource.connection.use {
        var sqlQuery =
            "begin ? :=wa_tracking.add_new_subscription(?,?,?); end;"
        it.prepareCall(sqlQuery).use {
            it.registerOutParameter(1, Types.INTEGER)
            it.setLong(2, phoneNum)
            it.setString(3, assignedName)
            it.setInt(4, userID)
            it.execute()
            it.getInt(1)
        }
    }

fun getSubscriptionID(userID: Int, phoneNum: Long) =
    OracleHikariDataSource.dataSource.connection.use{
        var sqlQuery = "select max(id) from subscriptionphone where phone_num = ? and user_id = ?"
        var id: Int? = null
        it.prepareStatement(sqlQuery).use{
            it.setLong(1, phoneNum)
            it.setInt(2, userID)
            it.executeQuery().use{rs ->
                if(rs.next())
                    id = rs.getInt(1)
            }
        }
        id!!
    }


// last parameter is almost the same as first but is used to define 3 states. is used since version 1.2
data class SubscriptionState(val isValid: Boolean, val avatarID: Long, val tsLastSessionStart: Long?, val tsLastSessionEnd: Long?, val isValidStr: String?)

fun getSubscriptionNumberStateCall(phoneNum: Long, userID: Int) : SubscriptionState? {
    OracleHikariDataSource.dataSource.connection.use {
        var state: SubscriptionState? = null
        var sqlQuery =
            """with subscription_id as (select max(id) from subscriptionphone where phone_num = ? and user_id = ?)
                select * from
                    (select is_valid,
                        case
                            when is_valid = 1 then 'valid'
                            when is_valid = 0 then 'invalid'
                            else 'undefined'
                        end as is_valid_str,
                        avatar_id from subscriptionphone where id = (select * from subscription_id)),
                    (select
                        max(case when rn = 1 then add_time else null end) as last_session_start,
                        max(case when rn = 2 then add_time else null end) as last_session_finish
                    from(
                        select add_time, row_number() over(order by id asc) as rn
                        from(
                            select add_time,id
                            from visitnote
                            where id >= (select max(id)
                                    from visitnote
                                    where phone_id = (select * from subscription_id) and is_online = 1)
                            and phone_id = (select * from subscription_id))
                        )
                    )"""
        it.prepareStatement(sqlQuery).use {
            it.setLong(1, phoneNum)
            it.setInt(2, userID)
            it.executeQuery().use { rs ->
                if (rs.next())
                    state =
                        SubscriptionState(rs.getBoolean(1), rs.getLong(3),
                            rs.getTimestamp(4)?.time?.div(1000), rs.getTimestamp(5)?.time?.div(1000), rs.getString(2))
            }
        }
        return state
    }
}

fun getAvatarCall(phoneNum: Long, userID: Int) =
    OracleHikariDataSource.dataSource.connection.use{
        var sqlQuery = "select avatar_id, avatar from subscriptionphone where phone_num = ? and user_id = ?"
        var result: Pair<Long,Blob>? = null
        it.prepareStatement(sqlQuery).use{
            it.setLong(1, phoneNum)
            it.setInt(2, userID)
            it.executeQuery().use{rs ->
                if(rs.next())
                    result = Pair(rs.getLong(1),rs.getBlob(2))
            }
        }
        result
    }

fun unsubscribeCall(phoneNum: Long, userID: Int) =
    OracleHikariDataSource.dataSource.connection.use{
        val sql = """declare
                        subscription_id number;
                        result number;
                    begin
                        begin
                            select id into subscription_id from subscriptionphone where phone_num = ? and user_id = ?;
                            select authkey.phone_num into result from authkey join subscriptionphone on (authkey.id=subscriptionphone.key_id) where subscriptionphone.id=subscription_id;
                        exception when no_data_found then
                            select -1 into result from dual;
                        end;

                        wa_tracking.update_subscription_state(subscription_id,-1);
                        select result into ? from dual;
                    end;"""
        it.prepareCall(sql).use { cstmt ->
            cstmt.setLong(1, phoneNum)
            cstmt.setInt(2, userID)
            cstmt.registerOutParameter(3, Types.NUMERIC)
            cstmt.execute()
            cstmt.getLong(3)
        }
    }

//latest 4 params introduced in version 1.2; should be replaced to non null in following
fun updateGCMTokenCall(newToken: String, userID: Int, appVersion: String?= null, androidSecureID: String?= null, deviceFingerprint: String?= null, gsfId: String?= null) {
    OracleHikariDataSource.dataSource.connection.use {
        var gcmTokenLoc =
            if(newToken == null || newToken=="")
                "nulled"
            else
                newToken
        var sqlQuery =
            "update appuser set gcm_token = ?, app_version = ?, android_secure_id = ?, device_fingerprint = ?, gsf_id = ? where id = ?"
        it.prepareStatement(sqlQuery).use {
            it.setString(1, gcmTokenLoc)
            it.setString(2, appVersion)
            it.setString(3, androidSecureID)
            it.setString(4, deviceFingerprint)
            it.setString(5, gsfId)
            it.setInt(6, userID)
            it.executeUpdate()
        }
    }
}

fun updateNotificationDeliveryCall(phoneNum: Long, enabled: Boolean, userID: Int){
    OracleHikariDataSource.dataSource.connection.use {
        val newVal = if(enabled) 1 else 0
        var sqlQuery =
            "update subscriptionphone set notify=? where phone_num=? and user_id=?"
        it.prepareStatement(sqlQuery).use {
            it.setInt(1, newVal)
            it.setLong(2, phoneNum)
            it.setInt(3, userID)
            it.executeUpdate()
        }
    }
}

fun getNotificationDeliveryData(userID: Int, phoneNum: Long) =
    OracleHikariDataSource.dataSource.connection.use{
        var sqlQuery = "select gcm_token, assigned_name, app_name from subscriptionphone join appuser on (appuser.id=subscriptionphone.user_id) where user_id=? and phone_num=?"
        var triple: Triple<String,String,String>? = null
        it.prepareStatement(sqlQuery).use{
            it.setInt(1,userID)
            it.setLong(2,phoneNum)
            it.executeQuery().use{rs ->
                if(rs.next())
                    triple = Triple(rs.getString(1),rs.getString(2), rs.getString(3)?:"com.peanutbutter.wastat")
            }
            triple!!
        }
    }

fun checkUserSubscriptionValidityCall(userID: Int)=
    OracleHikariDataSource.dataSource.connection.use {
        var sqlQuery =
            "begin ? :=wa_tracking.check_user_subscription_valid(?); end;"
        it.prepareCall(sqlQuery).use {
            it.registerOutParameter(1, Types.TIMESTAMP)
            it.setInt(2, userID)
            it.execute()
            it.getTimestamp(1).time.div(1000)
        }
    }

fun getPhonesWhereTrackingNeeded(userID: Int): List<Pair<Long,Int>>{
    OracleHikariDataSource.dataSource.connection.use {
        var sqlQuery = "select phone_num, subscriptionphone.id from subscriptionphone join payment on (payment.user_id=subscriptionphone.user_id) where payment.user_id = ? and valid_until>cast(sys_extract_utc(systimestamp) as date) and is_valid=1 and key_id is null"
        var list = ArrayList<Pair<Long,Int>>()
        it.prepareStatement(sqlQuery).use{
            it.setInt(1,userID)
            it.executeQuery().use{rs ->
                while(rs.next())
                    list.add(Pair(rs.getLong(1), rs.getInt(2)))
            }
        }
        return list
    }
}

fun syncContactsCall(contactsList: List<Long>, userID: Int)=
    OracleHikariDataSource.dataSource.connection.use {
        val sqlQuery =
            "insert into retrievedcontacts (user_id, phone_number, add_time) values (?,?,cast(sys_extract_utc(systimestamp) as date))"
        it.prepareStatement(sqlQuery).use {
            for(item in contactsList) {
                it.setInt(1, userID)
                it.setLong(2,item)
                it.addBatch()
            }
            it.executeBatch()
        }
    }

fun getCurrentDatabaseTime(): Long =
    OracleHikariDataSource.dataSource.connection.use{
        var sqlQuery = "select cast(sys_extract_utc(systimestamp) as date) from dual"
        var serverTimeSec: Long? = null
        it.prepareStatement(sqlQuery).use{
            it.executeQuery().use{rs ->
                if(rs.next())
                    serverTimeSec = rs.getTimestamp(1).time.div(1000)
            }
        }
        serverTimeSec!!
    }
