package com.codesroots.mac.cards.presentaion

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.SystemClock
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.codesroots.mac.cards.DataLayer.helper.PreferenceHelper
import com.codesroots.mac.cards.R
import com.codesroots.mac.cards.db.CardDao
import com.codesroots.mac.cards.db.CardDatabase
import com.codesroots.mac.cards.models.Buypackge
import com.codesroots.mac.cards.presentaion.Printer.AidlUtil
import com.codesroots.mac.cards.presentaion.companydetails.fragment.CompanyDetails
import com.codesroots.mac.cards.presentaion.mainfragment.mainFragment
import com.codesroots.mac.cards.presentaion.mainfragment.viewmodel.MainViewModel
import com.codesroots.mac.cards.presentaion.menufragmen.MenuFragment
import com.codesroots.mac.cards.presentaion.payment.Payment
import com.codesroots.mac.cards.presentaion.portifliofragment.PortiflioFragment
import com.codesroots.mac.cards.presentaion.reportsFragment.ReportsFragment
import com.google.android.gms.analytics.internal.zzy.v
import kotlinx.android.synthetic.main.alert_add_reserve.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.runOnUiThread
import java.io.IOException

class ClickHandler {

    var  mLastClickTime: Long = 0

    fun SwitchToPackages( context: Context,comid :String) {

        val homeIntent = Intent(context,CompanyDetails()::class.java)
        val bundle = Bundle()
        homeIntent.putExtra("packageId" , comid)
        (context as MainActivity).startActivity(homeIntent)


    }
    fun SwitchToReports( context: Context,comid :String) {

        val bundle = Bundle()
        //  bundle.putParcelable("cliObj" ,clients[position] )
        val frag = ReportsFragment()
        frag.arguments =bundle
        bundle.putString("packageId" , comid)
        ( context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, frag).addToBackStack(null).commit()
    }

    fun SwitchToHome( context: Context) {

        val bundle = Bundle()
        //  bundle.putParcelable("cliObj" ,clients[position] )
        val frag = mainFragment()
        frag.arguments =bundle
        ( context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, frag).addToBackStack(null).commit()
    }
    fun SwitchToReports( context: Context) {

        val bundle = Bundle()
        //  bundle.putParcelable("cliObj" ,clients[position] )
        val frag = ReportsFragment()
        frag.arguments =bundle
        ( context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, frag).addToBackStack(null).commit()
    }

    fun SwitchToMore( context: Context) {

        val bundle = Bundle()
        //  bundle.putParcelable("cliObj" ,clients[position] )
        val frag = MenuFragment()
        frag.arguments = bundle
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, frag).addToBackStack(null).commit()

    }

    fun SwitchToWallet( context: Context) {

        val bundle = Bundle()
        //  bundle.putParcelable("cliObj" ,clients[position] )
        val frag = PortiflioFragment()
        frag.arguments = bundle
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, frag).addToBackStack(null).commit()

    }
    fun SwitchToPayment(context: Context,viewmodel:MainViewModel) {
        var MyData = context as CompanyDetails

        val auth = PreferenceHelper.getAuthId()
        viewmodel.BuyPackage(MyData.Company_id.toString(),auth!!,MyData.minteger.toString())

        if (viewmodel.BuyPackageResponseLD?.hasObservers() == false) {
            viewmodel.BuyPackageResponseLD?.observe(context as CompanyDetails, Observer {
                if (it.err != null) {
                 //   it.err!!.snack((context as CompanyDetails).window.decorView.rootView)
                    //      dialogView.err.isGone = false

                    //      dialogView.err.text = it.err
                } else {
                    if (!it!!.pencode.isNullOrEmpty()) {

                        Glide.with(context as CompanyDetails)
                            .asBitmap()
                            .load("http://across-cities.com/"+it.src)
                            .into(object : SimpleTarget<Bitmap>(100, 100) {
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                    try {
                                        AidlUtil.getInstance().printRecipte(it,resource);
                                        val homeIntent = Intent(context, Payment::class.java)
                                        homeIntent.putExtra("myobj", it)
                                        (context as CompanyDetails).startActivity(homeIntent)
                                    }
                                    catch (e: IOException) {
                                        // handle exception
                                    }
                                }

                                override fun onLoadFailed(errorDrawable: Drawable?) {
                                    // handle exception
                                }
                            })
                    }

                }


            })

        }
// You Can Customise your Title here


//            dialogView.saveToRoom.setOnClickListener { v: View? ->
//            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
//                return@setOnClickListener
//            }
//            v!!.isGone = true
//            mLastClickTime = SystemClock.elapsedRealtime();
//            val auth = PreferenceHelper.getAuthId()
//            viewmodel.BuyPackage(id,auth!!,dialogView.from.text.toString())
//
//            if (viewmodel.BuyPackageResponseLD?.hasObservers() == false) {
//                viewmodel.BuyPackageResponseLD?.observe(context, Observer {
//
//
//                    if (it.err != null) {
//                        it.err!!.snack((context as MainActivity).window.decorView.rootView)
//                        dialogView.err.text = it.err
//                        dialogView.err.isGone = false
//                    } else {
//                        if (!it!!.pencode.isNullOrEmpty()) {
//        Thread {
//            val db = Room.databaseBuilder(
//                context,
//                CardDatabase::class.java, "card-database"
//            ).build()
//            insertUserWithPet(it, db.getCardDao())
//            "تم الحفظ بالمحفظة بنجاح".snack((context).window.decorView.rootView)
//        }.start()
//
//                   GlobalScope.launch {
//                       val db = Room.databaseBuilder(
//                          context,
//                           CardDatabase::class.java, "card-database"
//                       ).build()
//                       db.getCardDao().GetAllData()
//                  "تم الحفظ بالمحفظة بنجاح".snack((context).window.decorView.rootView)
//                   }
//
//
//                        }
//
//                    }
//
//                })
//            }
//        }
    }

    fun insertUserWithPet(user: Buypackge, cardDao: CardDao) {
        val pets = user.pencode!!
        for (i in pets.indices) {
            pets.get(i).buypackageid = user.opno!!
        }
        cardDao.insertPetList(pets)
        cardDao. insertPackege(user)
    }

    fun  deletedata(context: MainActivity,its:Buypackge) {
        GlobalScope.launch {
            let {
                val db = Room.databaseBuilder(context ,
                    CardDatabase::class.java, "card-database"
                ).build()
                db.getCardDao().deleteCard(its.opno!!)

            }}

    }
    suspend fun getUserWithPets(id: Int, room: CardDao): Buypackge {
        val user = room.getUser(id)
        val pets = room.getPetList(id)
        user.pencode = pets
        return user
    }
    fun ShowReport(context: Context,id:String) {

        lateinit var viewModel: MainViewModel
        val auth = PreferenceHelper.getAuthId()
        viewModel =   ViewModelProviders.of(( context as MainActivity)).get(MainViewModel::class.java)
        viewModel.PrintReport(id,auth!!)
        if (viewModel.BuyPackageResponseLD?.hasObservers() == false) {

            viewModel.BuyPackageResponseLD?.observe(context, Observer {
                if (it.err != null) {
                    it.err!!.snack((context as MainActivity).window.decorView.rootView)


                } else {
                    if (!it!!.pencode.isNullOrEmpty()) {
                        Glide.with(context as MainActivity)
                            .asBitmap()
                            .load("http://across-cities.com/" + it.src)
                            .into(object : SimpleTarget<Bitmap>(100, 100) {
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                    try {

                                        val homeIntent = Intent(context, Payment::class.java)
                                        homeIntent.putExtra("myobj", it)
                                        (context as MainActivity).startActivity(homeIntent)

                                    } catch (e: IOException) {
                                        // handle exception
                                    }
                                }

                                override fun onLoadFailed(errorDrawable: Drawable?) {
                                    // handle exception
                                }
                            })
                    }
                }
            })
        }

    }

    fun PrintReport(context: Context,id:String) {

        lateinit var viewModel: MainViewModel

        val auth = PreferenceHelper.getAuthId()
        viewModel =   ViewModelProviders.of(( context as MainActivity)).get(MainViewModel::class.java)
        viewModel.PrintReport(id,auth!!)
        if (viewModel.BuyPackageResponseLD?.hasObservers() == false) {

            viewModel.BuyPackageResponseLD?.observe(context, Observer {
                if (it.err != null) {
                    it.err!!.snack((context as MainActivity).window.decorView.rootView)


                } else {
                    if (!it!!.pencode.isNullOrEmpty()) {


                        Glide.with(context as MainActivity)
                            .asBitmap()
                            .load("http://across-cities.com/" + it.src)
                            .into(object : SimpleTarget<Bitmap>(100, 100) {
                                override fun onResourceReady(resourcee: Bitmap, transition: Transition<in Bitmap>?) {
                                    try {
                                        AidlUtil.getInstance().printRecipte(it, resourcee);
                                        Glide.with(context as MainActivity)
                                            .asBitmap()
                                            .load("http://across-cities.com/"+it.notesimg)
                                            .into(object : SimpleTarget<Bitmap>(100, 100) {
                                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                                    try {

                                                        IPosPrinterTestDemo.getInstance().printText(it,resourcee,resource);

                                                        val homeIntent = Intent(context, Payment::class.java)

                                                        homeIntent.putExtra("myobj", it)

                                                        (context as MainActivity).startActivity(homeIntent)

                                                    } catch (e: IOException) {
                                                        // handle exception
                                                    }


                                                }})


                                    } catch (e: IOException) {
                                        // handle exception
                                    }
                                }

                                override fun onLoadFailed(errorDrawable: Drawable?) {
                                    // handle exception
                                }
                            })
                    }
                }
            })
        }

    }

    fun PrintReportFromPortifolio(context: Context,its:Buypackge) {
        var card: Buypackge? = null

        GlobalScope.launch {
            context.let {
                val db = Room.databaseBuilder(
                    it,
                    CardDatabase::class.java, "card-database"
                ).build()
                var data = getUserWithPets(its.opno!!, db.getCardDao())
                card = data
                if (!card!!.pencode.isNullOrEmpty()) {
                    Thread {
                        context!!.runOnUiThread {
                            Glide.with(context as MainActivity)
                                .asBitmap()
                                .load("http://across-cities.com/" + card!!.src)
                                .into(object : SimpleTarget<Bitmap>(100, 100) {
                                    override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap>?
                                    ) {
                                        try {
                                            AidlUtil.getInstance().printRecipte(card, resource);
                                            deletedata(context, card!!)


                                        } catch (e: IOException) {
                                            // handle exception
                                        }
                                    }

                                    override fun onLoadFailed(errorDrawable: Drawable?) {
                                        // handle exception
                                    }
                                })
                        }
                    }.start()
                }
            }
        }
    }
//thread {
//
//}.start()














}