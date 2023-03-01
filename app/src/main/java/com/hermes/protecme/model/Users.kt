package com.hermes.protecme.model

data class Users(
    val id:String,
    val nama:String,
    val email:String,
    val no_hp:String,
    val jenis_kelamin:String,
    val tgl_lahir:String,
    val alamat:String,
    val url_img:String,
){
    constructor():this("","","","","","","","",)
}
