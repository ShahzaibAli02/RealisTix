package com.playsnyc.realistix.data.model

import com.google.firebase.Timestamp
import java.util.Date

data class Event(var uid:String,
                 var docId:String,
                 var name:String,
                 var organizer:String,
                 var type:String,
                 var description:String,
                 var locationType:String,
                 var address:String,
                 var date:Long,
                 var startTime:Long,
                 var endTime:Long,
                 var fee:Double,
                 var currency:String="$",
                 var capacity:Int,
                 var images:List<String>,
                 var event_created_date:Timestamp,
                 var attandesUid:List<String>,
)
{
    constructor():this(
            uid="",
            docId="",
            name = "",
            organizer = "",
            type = "",
            description = "",
            locationType = "",
            address = "",
            date = Date().time,
            startTime = Date().time,
            endTime = Date().time,
            fee = 0.0,
            currency="$",
            capacity=0,
            emptyList(),
            event_created_date= Timestamp.now(),
            emptyList()

    )
}
