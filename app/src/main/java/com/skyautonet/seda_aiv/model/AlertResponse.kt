package com.skyautonet.seda_aiv.model

class AlertResponse : ArrayList<AlertItem>()

data class AlertItem (
    var alert_mode: String = ""
)