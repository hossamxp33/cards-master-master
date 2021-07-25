package com.codesroots.mac.cards.presentaion

import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.codesroots.mac.cards.DataLayer.helper.PreferenceHelper
import com.codesroots.mac.cards.DataLayer.usecases.checkUserLogin
import com.codesroots.mac.cards.R
import com.codesroots.mac.cards.databinding.ActivityMainBinding
import com.codesroots.mac.cards.databinding.ActivityPaymentBinding
import com.codesroots.mac.cards.databinding.MainAdapterBinding
import com.codesroots.mac.cards.db.CardDao
import com.codesroots.mac.cards.db.CardDatabase
import com.codesroots.mac.cards.models.Buypackge
import com.codesroots.mac.cards.presentaion.Printer.AidlUtil
import com.codesroots.mac.cards.presentaion.banks.BankActitvity
import com.codesroots.mac.cards.presentaion.bluetooth.WorkService

import com.codesroots.mac.cards.presentaion.companydetails.fragment.CompanyDetails
import com.codesroots.mac.cards.presentaion.login.LoginActivity
import com.codesroots.mac.cards.presentaion.mainfragment.mainFragment
import com.codesroots.mac.cards.presentaion.portifliofragment.PortiflioFragment

import com.codesroots.mac.cards.presentaion.mainfragment.viewmodel.MainViewModel
import com.codesroots.mac.cards.presentaion.menufragmen.MenuFragment
import com.codesroots.mac.cards.presentaion.payment.Payment
import com.codesroots.mac.cards.presentaion.reportsFragment.ReportsFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseAppLifecycleListener
import com.google.firebase.FirebaseCommonRegistrar
import com.google.firebase.messaging.FirebaseMessaging
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.ui.ScanningActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alert_add_reserve.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.startActivity

import java.io.IOException
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var homeFragment: mainFragment
    lateinit var reportsFragment: ReportsFragment
    lateinit var moreFragment: MenuFragment
    lateinit var wallet: PortiflioFragment
    lateinit var viewModel: MainViewModel
    private val BLUETOOTH_REQUEST = 1
    var binding : ActivityMainBinding? = null

    lateinit var navigationView: NavigationView
    override fun onResume() {
        super.onResume()
        println("onressomes")
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding?.listener = ClickHandler()
        binding?.context = this
    //    val crashlytics = FirebaseCrashlytics.getInstance()
       // throw RuntimeException("Test Crash")
        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance()
        Printooth.init(this)

       // FirebaseMessaging.getInstance().subscribeToTopic(PreferenceHelper.getUserId().toString())
        FirebaseMessaging.getInstance().subscribeToTopic("10")
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorAccent)
        IPosPrinterTestDemo.getInstance().connectPrinterService(this)

    //    val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val typeface = Typeface.createFromAsset(this!!.assets, "fonts/DroidKufi_Regular.ttf")

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
//        binding!!.username.text = "اهلا بك  " + PreferenceHelper.getUsername()
        binding!!.btnMenu.setOnClickListener{ v ->
            (this).openCloseNavigationDrawer(v)
        }
//        viewModel.getMyBalance()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                //   Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

//            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("tken", token)
            viewModel.PostToken(token)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
        ///////// tool bar and drawerToggle
        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        actionBar?.title = ""
        val drawerToggle: ActionBarDrawerToggle =
            object : ActionBarDrawerToggle(this, drawerLayout, toolBar, (R.string.open), (R.string.close)) {

            }
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        homeFragment = mainFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()



        AidlUtil.getInstance().connectPrinterService(this)
        PreferenceHelper(this)
        animation()
        val adapter = BluetoothAdapter.getDefaultAdapter()

        if (!adapter.isEnabled) run {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, BLUETOOTH_REQUEST)
        }



        viewModel.MyBalanceResponseLD?.observe(this , Observer {

//            binding.myBalance = it
//            //binding.textView11.typeface = typeface
//            binding.textView5.typeface = typeface
//            binding.lastvalue.typeface = typeface

           // binding.value.typeface = typeface
        })
    }
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        when (menuItem.itemId) {
            R.id.home -> {
                homeFragment = mainFragment()
                supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.ttb, 0, 0,0)
                    .replace(R.id.main_frame, homeFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()


            }
            R.id.reports -> {
                reportsFragment = ReportsFragment()
                supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.ttb, 0, 0,0)
                    .replace(R.id.main_frame, reportsFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.banks -> {

                val homeIntent = Intent(this, BankActitvity::class.java)
                startActivity(homeIntent)
            }
            R.id.more -> {
                if (checkUserLogin(this)) {
                    PreferenceHelper.setAuthId("0",this)
                    Toast.makeText(this, "تم تسجيل خروجك", Toast.LENGTH_SHORT).show()

                    val homeIntent = Intent(this, LoginActivity::class.java)
                   startActivity(homeIntent)
                }
            }


        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    private fun animation(){
        val ttb = AnimationUtils.loadAnimation(this, R.anim.img)

    }
    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.isDrawerOpen(GravityCompat.START)
        }
        else{
            super.onBackPressed()
        }
    }
    fun openCloseNavigationDrawer(view: View) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

}



