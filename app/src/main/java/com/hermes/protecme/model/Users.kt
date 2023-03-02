package com.hermes.protecme.model

data class Users(
    var id:String,
    var nama:String,
    var email:String,
    var no_hp:String,
    var jenis_kelamin:String,
    var tgl_lahir:String,
    var alamat:String,
    var url_img:String,
){
    constructor():this("","","","","","","","",)
}
