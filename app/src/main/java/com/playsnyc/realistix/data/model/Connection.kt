package com.playsnyc.realistix.data.model

import java.util.Calendar

data class Connection(val uid:String,val targetUid:String,val ids:List<String>,val connectionDateTime:Long=Calendar.getInstance().timeInMillis)
