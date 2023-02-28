package com.hermes.protecme.model

data class Sos(
    val id:String?,
    val id_user:String,
    var latitude:String,
    var longitude:String,
    var status:Boolean=false,
){
    constructor():this("","","","",false)
}
