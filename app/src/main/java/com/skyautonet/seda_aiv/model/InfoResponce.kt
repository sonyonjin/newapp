package com.skyautonet.seda_aiv.model

class InfoResponce : ArrayList<InfoResponceItem>()

data class InfoResponceItem(
    val address: String,
    val battery: String,
    val heading: String,
    val latitude: String,
    val longitude: String,
    val plate_no: String,
    val speed: String,
    val total_distance: String,
    val total_time: String,
    val vehicle_id: String?,
    val gps_signal:String,
    val status:String,
    var toggle: Boolean,
    var model:String,
    var score:Int,
    var msg: String
)