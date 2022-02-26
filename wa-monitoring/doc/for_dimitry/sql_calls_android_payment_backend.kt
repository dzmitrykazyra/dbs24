package com.payment.android.backend

import java.sql.SQLException
import java.util.HashMap
import java.util.concurrent.ConcurrentHashMap

fun getAppUsersCall() =
    OracleHikariDataSource.dataSource.connection.use{
        val usersMap = ConcurrentHashMap<String, Int>()
        var sqlQuery = "select /*+ parallel(appuser 4) */ id, auth_token from appuser"
        it.prepareStatement(sqlQuery).use{
            it.fetchSize = 50000
            it.executeQuery().use{rs ->
                while(rs.next())
                    usersMap[rs.getString((2))]=rs.getInt(1)
            }
        }
        usersMap
    }

fun addNewPaymentCall(userID: Int, durationDays: Int, profilesAmount: Int, price: Int, paymentMethod: String, currencyCode: String, gpStrPrice: String? = null, gpOrderId: String? = null, gpPurchaseToken:String? = null, appName: String): Long? {
    OracleHikariDataSource.dataSource.connection.use {
        var sqlQuery =
            """insert into payment (user_id, fulfil_time, valid_until, pay_type, subs_amount, price, cur_code, gp_str_price, gp_order_id, gp_purchase_token, app_name)
                values (?,cast(sys_extract_utc(systimestamp) as date),
                 (select greatest(current_date,(select max(valid_until) from payment where user_id = ?))+? from dual),
                 ?,?,?,?,?,?,?,?)
            """.trimMargin()
        var generatedColumns = arrayOf("valid_until")
        it.prepareStatement(sqlQuery, generatedColumns).use {
            it.setInt(1, userID)
            it.setInt(2, userID)
            it.setInt(3, durationDays)
            it.setString(4, paymentMethod)
            it.setInt(5, profilesAmount)
            it.setInt(6, price)
            it.setString(7,currencyCode.toLowerCase())
            it.setString(8,gpStrPrice)
            it.setString(9,gpOrderId)
            it.setString(10,gpPurchaseToken)
            it.setString(11,appName)
            it.executeUpdate()
            val rs = it.generatedKeys
            rs.next()
            return rs.getTimestamp(1)?.time?.div(1000)
        }
    }
}

fun addNewPaymentWithExpiryCall(userID: Int, expiryTimeMillis: Long, profilesAmount: Int, price: Int, paymentMethod: String, currencyCode: String, gpStrPrice: String? = null, gpOrderId: String? = null, gpPurchaseToken:String? = null, appName: String): Long? {
    OracleHikariDataSource.dataSource.connection.use {
        var sqlQuery =
            """insert into payment (user_id, fulfil_time, valid_until, pay_type, subs_amount, price, cur_code, gp_str_price, gp_order_id, gp_purchase_token, app_name)
                values (?,cast(sys_extract_utc(systimestamp) as date), to_date('1970-01-01 00','yyyy-mm-dd hh24') + ?/1000/60/60/24,
                ?,?,?,?,?,?,?,?)
            """.trimMargin()
        var generatedColumns = arrayOf("valid_until")
        it.prepareStatement(sqlQuery, generatedColumns).use {
            it.setInt(1, userID)
            it.setLong(2, expiryTimeMillis)
            it.setString(3, paymentMethod)
            it.setInt(4, profilesAmount)
            it.setInt(5, price)
            it.setString(6,currencyCode.toLowerCase())
            it.setString(7,gpStrPrice)
            it.setString(8,gpOrderId)
            it.setString(9,gpPurchaseToken)
            it.setString(10,appName)
            it.executeUpdate()
            val rs = it.generatedKeys
            rs.next()
            return rs.getTimestamp(1)?.time?.div(1000)
        }
    }
}

fun getLast3DayPaymentsCall(): ArrayList<GooglePayment> {
    OracleHikariDataSource.dataSource.connection.use{
        val paymentList = ArrayList<GooglePayment>()
        val sqlQuery =
            "select fulfil_time, valid_until, GP_ORDER_ID, GP_PURCHASE_TOKEN, subs_amount, id, user_id, app_name from payment where PAY_TYPE='google pay' and gp_str_price!='-1' and fulfil_time between cast(sys_extract_utc(systimestamp) as date)-3 and cast(sys_extract_utc(systimestamp) as date)"
        it.prepareStatement(sqlQuery).use{
            it.executeQuery().use{rs ->
                while(rs.next()) {
                    paymentList.add(
                        GooglePayment(
                            rs.getTimestamp(1).time,
                            rs.getTimestamp(2).time,
                            rs.getString(3),
                            rs.getString(4),
                            rs.getInt(5),
                            rs.getLong(6),
                            rs.getLong(7),
                            rs.getString(8)
                        )
                    )
                }
            }
        }
        return paymentList
    }
}

fun getGPaymentsAboutToExpire(): ArrayList<GooglePayment> {
    OracleHikariDataSource.dataSource.connection.use{
        val paymentList = ArrayList<GooglePayment>()
        val sqlQuery =
            """select *
            from
                (select max(valid_until) as max_valid_until, GP_PURCHASE_TOKEN, subs_amount, user_id, app_name
                from payment
                where PAY_TYPE='google pay' and GP_ORDER_ID is not null and GP_PURCHASE_TOKEN is not null
                group by GP_PURCHASE_TOKEN, subs_amount, user_id, app_name)
            where max_valid_until<cast(sys_extract_utc(systimestamp) as date)+3""".trimMargin() // the same 36 hours as it has been when adding
        it.prepareStatement(sqlQuery).use{
            it.fetchSize = 500
            it.executeQuery().use{rs ->
                while(rs.next()) {
                    paymentList.add(
                        GooglePayment(
                            rs.getTimestamp(1).time,
                            rs.getString(2),
                            rs.getInt(3),
                            rs.getLong(4),
                            rs.getString(5)
                        )
                    )
                }
            }
        }
        return paymentList
    }
}

fun nullifySubscription(paymentId: Long) {
    OracleHikariDataSource.dataSource.connection.use {
        var sqlQuery = "update payment set valid_until=fulfil_time, gp_str_price='-1' where id=?"
        it.prepareStatement(sqlQuery).use {
            it.setLong(1, paymentId)
            it.executeUpdate()
        }
    }
}

fun checkPaymentExistsCall(userID: Int, expiryTimeMillis: Long, gpPurchaseToken: String)=
    OracleHikariDataSource.dataSource.connection.use{
        val sqlQuery =
            "select * from payment where user_id=? and gp_purchase_token=? and to_date('1970-01-01 00','yyyy-mm-dd hh24') + ?/1000/60/60/24 between valid_until-1 and valid_until+1"
        it.prepareStatement(sqlQuery).use{
            it.setInt(1, userID)
            it.setString(2, gpPurchaseToken)
            it.setLong(3, expiryTimeMillis)
            it.executeQuery().use{rs ->
                rs.next()
            }
        }
    }
