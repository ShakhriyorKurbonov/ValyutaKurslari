package iqro.mobil.valyutakurslari

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.DialogInterface.OnDismissListener
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract.Calendars
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2.PageTransformer
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import iqro.mobil.valyutakurslari.database.CurrencyDbManager
import iqro.mobil.valyutakurslari.databinding.ActivityMainBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.log
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myDbManager: CurrencyDbManager
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar

    @SuppressLint("SuspiciousIndentation", "Range")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myDbManager = CurrencyDbManager(this)
        myDbManager.onCreate()


        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            )
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "SyncWork",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )

        createNotificationChannel()

        toolbar = findViewById<Toolbar>(R.id.toolbar)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        setSupportActionBar(toolbar)

        val navView = findViewById<NavigationView>(R.id.nav_view)


        if (isInternetAvailable(this@MainActivity)) {
            myDbManager.delete()
            val retrofit = Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://cbu.uz")
                .build()

            val api = retrofit.create(ApiInterface::class.java)
            lifecycleScope.launch {
                val data = api.getData()
                data.body()?.forEach {
                    myDbManager.insert(
                        it.id,
                        it.Ccy,
                        it.CcyNm_UZ,
                        it.Date,
                        it.Diff,
                        it.Rate,
                        it.Nominal
                    )
                }
                if (savedInstanceState == null) {
                    replaceFragment(CurrencyFragment())
                    navView.setCheckedItem(R.id.currency)
                }

            }
        } else {
            // Internet yo'q, foydalanuvchiga xabar berish kerak
            if (savedInstanceState == null) {
                replaceFragment(CurrencyFragment())
                navView.setCheckedItem(R.id.currency)
            }
        }




        navView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_naw,
            R.string.close_naw
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()





    }


    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.currency -> {
                toolbar.title = "Valyuta Kurslari"
                replaceFragment(CurrencyFragment())
            }

            R.id.currencyExchange -> {
                toolbar.title = "Konivertatsiya"
                replaceFragment(CurrencyExchange())
            }

            R.id.appInfo -> {
                replaceFragment(AppInfoFragment())
            }
            R.id.github->{
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://t.me/shaxriyor2949")
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "MyChannel"
            val descriptionText = "Channel for app notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("channel_id", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}