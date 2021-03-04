package com.codesroots.mac.cards.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Entity(tableName = "Buypackge")
@Parcelize
data class Buypackge (

    var opno: Int? = null,
    var odate: String? = null,

    var salor: String? = null,
    var time: String? = null,
    var device: String? = null,
    var name: String? = null,
    var src: String? = null,
    var price: String? = null,
    var notes: String? = null,
    var count: String? = null,
    var err: String? = null,
    var notesimg :String? = null,

    var serial:String? = null,
    @Ignore
    var pencode:List<Pencode>? = null

):Parcelable {

        @PrimaryKey(autoGenerate = true)
        var id : Int = 0

}
data class NonPrintedData(
    val result: List<ReportDaily>


)
data class LogData(
    val auth: String,
    val err: Int,
    val taken:String? = null


)
data class RequestBalance (
    val err: String? = null,
    val result: String? = null
)
data class MyBalance (
    val account: String? = null,
    val commession: String? = null
)
data class ReportDaily (
    val opid: Long,
    val opno: String,
    val opdate: String,
    var odate: String? = null,

    val strcase: String,
    val amount: String,
    val err: String? = null,
    val cardname: String? = null,
    val logo:String? = null
    )

@Entity(tableName = "pencode")
@Parcelize
data class Pencode (
    val pencode: String? = null,
    val expdate: String? = null,
    val serial: String? = null,
    var buypackageid: Int

    ):Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}

data class Terms (
    val headline: String,
    val details: String,
    val mobile: String,
    val email: String,
    val fb: String
)