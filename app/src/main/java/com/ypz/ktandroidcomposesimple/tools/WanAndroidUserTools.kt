package com.ypz.ktandroidcomposesimple.tools

import com.ypz.ktandroidcomposesimple.net.wanAndroid.bean.UserInfo

object WanAndroidUserTools {

    private const val LOGGED_FLAG = "logged_flag"
    private const val USER_INFO = "user_info"
    var isLogged: Boolean
        get() = DataStoreTools.readBooleanData(LOGGED_FLAG, false)
        set(value) = DataStoreTools.saveSyncBooleanData(LOGGED_FLAG, value = value)

    var userInfo: UserInfo?
        get() = DataStoreTools.readStringData(USER_INFO).fromJson()
        set(value) = DataStoreTools.saveSyncStringData(USER_INFO, value = value?.toJson() ?: "")

    fun onLogin(userInfo: UserInfo) {
        isLogged = true
        this.userInfo = userInfo
    }

    fun onLogOut() {
        isLogged = false
        this.userInfo = null
    }
}