package com.hermes.protecme.model

data class Pelaporan(
    val id:String?,
    val id_user:String,
    val jenisPelecehan:String,
    val judulPelecehan:String,
    val pihakBersangkutan:String,
    val tglKejadian:String,
    val waktuKejadian:String,
    val kronologi:String,
    val lokasiKejadian:String,
    val fileBukti: String,
    val statusPelaksanaan:String,
){
    constructor():this("","","","","","","","","","","")
}
