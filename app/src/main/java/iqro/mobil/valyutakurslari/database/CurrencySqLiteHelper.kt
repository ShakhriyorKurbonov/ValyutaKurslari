package iqro.mobil.valyutakurslari.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CurrencySqLiteHelper(context: Context):SQLiteOpenHelper(context,"currencyDb",null,1) {

    companion object{
        const val Currency="Currency"
        const val Ccy="Ccy"
        const val CcyNm_UZ="CcyNm_UZ"
        const val Date="Date"
        const val Diff="Diff"
        const val Rate="Rate"
        const val Normal="Normal"
        const val Id="Id"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("create table if not exists $Currency($Id Integer,$Ccy text,$CcyNm_UZ text,$Date text,$Diff text,$Rate text,$Normal text);")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("drop table if exists $Currency")
    }

}