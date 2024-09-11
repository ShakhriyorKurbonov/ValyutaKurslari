package iqro.mobil.valyutakurslari

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service.NOTIFICATION_SERVICE
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import iqro.mobil.valyutakurslari.database.CurrencyDbManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SyncWorker(val appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    private lateinit var myDbManager: CurrencyDbManager
    @SuppressLint("Range")
    override fun doWork(): Result {
        myDbManager=CurrencyDbManager(appContext)
        myDbManager.onCreate()
        val dataList= mutableListOf<CurrencyDataItem>()
        if (isInternetAvailable(appContext)){
            val cursor=myDbManager.fitch()
            var date="0"
            if (cursor!=null){
                date=cursor.getString(cursor.getColumnIndex("Date"))
            }

            val retrofit = Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://cbu.uz")
                .build()

            val api = retrofit.create(ApiInterface::class.java)
            GlobalScope.launch {
                val data = api.getData()
                data.body()?.forEach {
                    dataList.add(CurrencyDataItem(it.Ccy,null,null,it.CcyNm_UZ,null,null,it.Date,it.Diff,it.Nominal,it.Rate,it.id))
                }
                if (date!=dataList.first().Date){
                    dataList.forEach { myDbManager.insert(it.id,it.Ccy,it.CcyNm_UZ,it.Date,it.Diff,it.Rate,it.Nominal) }
                    sendNotification("${dataList.first().Ccy} kursi ${dataList.first().Rate} ga teng")
                }

            }
        }


        return Result.success()
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(message: String) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val channel=   NotificationChannel("CHANNEL_ID","ChannelName",NotificationManager.IMPORTANCE_DEFAULT)
        channel.lightColor= Color.BLACK
        channel.lockscreenVisibility= Notification.VISIBILITY_PRIVATE
        (applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(applicationContext, "channel_id")
            .setContentTitle("Valyuta kursi yangilandi")
            .setContentText(message)
            .setSmallIcon(R.drawable.baseline_stacked_line_chart_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(1, notification)
    }


    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


//    private fun sendNotification(message: String) {
//        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val notification = NotificationCompat.Builder(applicationContext, "channel_id")
//            .setContentTitle("Yangilanish")
//            .setContentText(message)
//            .setSmallIcon(R.drawable.baseline_stacked_line_chart_24)
//            .build()
//        notificationManager.notify(1, notification)
//    }



}
