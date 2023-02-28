package com.hermes.protecme.model

data class Pelaporan(
    val id:String,
    val id_user:String,
    val nama:String,
    val jenis_pelecehan:String,
    val pihak_bersangkutan:String,
    val tanggal_kejadian:String,
    val waktu_kejadian:String,
    val kronologi:String,
    val lokasi_kejadian:String,
    val file_bukti:String,
    val status_pelaksanaan:String,
)
