package iqro.mobil.valyutakurslari.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class CurrencyDbManager(val context: Context) {
    private lateinit var currencySqLiteHelper: CurrencySqLiteHelper
    private lateinit var database: SQLiteDatabase


    fun onCreate() {
        currencySqLiteHelper = CurrencySqLiteHelper(context)
        database = currencySqLiteHelper.writableDatabase
    }

    fun insert(id:Int,Ccy:String,CcyNm_Uz:String,Date:String,Diff:String,Rate:String,Normal:String){
        val contentValues = ContentValues()
        contentValues.put(CurrencySqLiteHelper.Id, id)
        contentValues.put(CurrencySqLiteHelper.Ccy, Ccy)
        contentValues.put(CurrencySqLiteHelper.CcyNm_UZ,CcyNm_Uz)
        contentValues.put(CurrencySqLiteHelper.Date,Date)
        contentValues.put(CurrencySqLiteHelper.Diff,Diff)
        contentValues.put(CurrencySqLiteHelper.Rate,Rate)
        contentValues.put(CurrencySqLiteHelper.Normal,Normal)
        database.insert(CurrencySqLiteHelper.Currency, null, contentValues)
    }

    fun fitch(): Cursor? {
        val cursor = database.query(
            CurrencySqLiteHelper.Currency,
            arrayOf( CurrencySqLiteHelper.Id, CurrencySqLiteHelper.Ccy,CurrencySqLiteHelper.CcyNm_UZ,CurrencySqLiteHelper.Date,CurrencySqLiteHelper.Diff,CurrencySqLiteHelper.Rate,CurrencySqLiteHelper.Normal),
            null,
            null,
            null,
            null,
            null,
            null,

            )
        return if (cursor.moveToFirst()) {
            cursor
        } else {
            null
        }
    }

//    fun update(id:Int,Ccy:String,CcyNm_Uz:String,Date:String,Diff:String,Rate:String,Normal:String): Int {
//        val contentValues = ContentValues()
//        contentValues.put(CurrencySqLiteHelper.Id, id)
//        contentValues.put(CurrencySqLiteHelper.Ccy, Ccy)
//        contentValues.put(CurrencySqLiteHelper.CcyNm_UZ,CcyNm_Uz)
//        contentValues.put(CurrencySqLiteHelper.Date,Date)
//        contentValues.put(CurrencySqLiteHelper.Diff,Diff)
//        contentValues.put(CurrencySqLiteHelper.Rate,Rate)
//        contentValues.put(CurrencySqLiteHelper.Normal,Normal)
//
//        return database.update(
//            CurrencySqLiteHelper.Currency,
//            contentValues,
//            "${CurrencySqLiteHelper.Id}=$id",
//            arrayOf( CurrencySqLiteHelper.Id, CurrencySqLiteHelper.Ccy,CurrencySqLiteHelper.CcyNm_UZ,CurrencySqLiteHelper.Date,CurrencySqLiteHelper.Diff,CurrencySqLiteHelper.Rate,CurrencySqLiteHelper.Normal),
//        )
//    }


    fun delete(){
        database.delete(CurrencySqLiteHelper.Currency,CurrencySqLiteHelper.Id,null)
        database.delete(CurrencySqLiteHelper.Currency,CurrencySqLiteHelper.Ccy,null)
        database.delete(CurrencySqLiteHelper.Currency,CurrencySqLiteHelper.CcyNm_UZ,null)
        database.delete(CurrencySqLiteHelper.Currency,CurrencySqLiteHelper.Date,null)
        database.delete(CurrencySqLiteHelper.Currency,CurrencySqLiteHelper.Diff,null)
        database.delete(CurrencySqLiteHelper.Currency,CurrencySqLiteHelper.Rate,null)
        database.delete(CurrencySqLiteHelper.Currency,CurrencySqLiteHelper.Normal,null)
    }

}