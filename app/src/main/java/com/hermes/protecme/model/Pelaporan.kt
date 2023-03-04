package com.hermes.protecme.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
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
):Parcelable{
    constructor():this("","","","","","","","","","","")
}
