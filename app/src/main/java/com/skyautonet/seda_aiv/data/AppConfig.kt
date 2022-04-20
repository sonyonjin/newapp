package com.skyautonet.seda_aiv.data

import com.skyautonet.seda_aiv.data.source.local.db.entity.AppManagedConfig

class AppConfig(managedConfig: AppManagedConfig) {

    private var _ssid: String? = managedConfig.ssid
    private var _password: String? = managedConfig.password
    private var _regNo: String? = managedConfig.regNo
    private var _rtspLink1: String? = managedConfig.rtspLink1
    private var _rtspLink2: String? = managedConfig.rtspLink2
    private var _rtspLink3: String? = managedConfig.rtspLink3
    private var _ipAddress: String? = managedConfig.ipAddress

    val ssid: String
        get() = _ssid.orEmpty()

    val password: String
        get() = _password.orEmpty()

    val regNo: String
        get() = _regNo.orEmpty()

    val rtspLink1: String
        get() = _rtspLink1.orEmpty()

    val rtspLink2: String
        get() = _rtspLink2.orEmpty()

    val rtspLink3: String
        get() = _rtspLink3.orEmpty()

    val ipAddress: String
        get() = _ipAddress.orEmpty()

}