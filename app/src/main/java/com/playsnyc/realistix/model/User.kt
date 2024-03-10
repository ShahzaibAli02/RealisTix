package com.playsnyc.realistix.model
data class User(
    var uid:String="",
    var name:String="",
    var email:String="",
    var password:String="",
    var occupation:String="",
    var organization:String="",
    var nationality:String="",
    var age:Int=0,
    var image:String="",
    var fvrtTypes:List<String> = emptyList(),
    var socialMedia:List<SocialMediaItem> = emptyList(),
)