package com.codesroots.mac.cards.presentaion

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
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
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isGone
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import cn.pedant.SweetAlert.SweetAlertDialog
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

import kotlinx.android.synthetic.main.alert_add_reserve.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.runOnUiThread
import java.io.IOException

class ClickHandler {
    var  mUserId =  ObservableField<Int>();

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

    fun RequestBalance( context: Context, viewmodel:MainViewModel) {
        var pDialog: SweetAlertDialog? = null;

        val auth = PreferenceHelper.getAuthId()
        viewmodel.RequestBalance(auth!!)
        mUserId.set(0)
        if (viewmodel.RequestBalanceLd?.hasObservers() == false) {
            viewmodel.RequestBalanceLd?.observe(context as MainActivity, Observer {
                if (it.result == "1") {

                    pDialog = SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                    pDialog!!.setTitleText("عملية ناجحية")
                    pDialog!!.setContentText("إضغط  لطباعة الطلب")
                    pDialog!!.setConfirmText("طباعة")
                    pDialog!!.confirmButtonBackgroundColor = R.color.blue

                    pDialog!!.show()
                }else {
                    val alertDialogBuilder = AlertDialog.Builder(context)
                    pDialog =  SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                    pDialog!!.setTitleText("يوجد خطأ!")
                    pDialog!!.setContentText(it.err!!)
                    pDialog!!.setConfirmText("OK")
                    pDialog!!.show()

                }
            })
        }
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
        ( context as MainActivity).supportFragmentManager.beginTransaction()    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_up)

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
        var pDialog : SweetAlertDialog? = null;

            val auth = PreferenceHelper.getAuthId()
            viewmodel.BuyPackage(MyData.Company_id.toString(),auth!!,MyData.minteger.toString())

            if (viewmodel.BuyPackageResponseLD?.hasObservers() == false) {
                viewmodel.BuyPackageResponseLD?.observe(context as CompanyDetails, Observer {
                    if (it.err != null) {
                        val alertDialogBuilder = AlertDialog.Builder(context)
                        pDialog =  SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
                        pDialog!!.setTitleText("يوجد خطأ!")
                        pDialog!!.setContentText(it.err!!)
                        pDialog!!.setConfirmText("OK")
                        pDialog!!.show()

                    } else {
                        var data = it
                        pDialog =  SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
                        pDialog!!.setTitleText("تم اضافة الطلب!")
                        pDialog!!.setContentText("إضغط  لطباعة الطلب")
                        pDialog!!.setConfirmText("طباعة")
                        pDialog!!.confirmButtonBackgroundColor = R.color.blue
                     
                        pDialog!!.show()

                        pDialog!!.setConfirmClickListener {


                            Glide.with(context as CompanyDetails)
                                .asBitmap()
                                .load("http://across-cities.com/"+data.src)
                                .into(object : SimpleTarget<Bitmap>(100, 100) {
                                    override fun onResourceReady(resourcee: Bitmap, transition: Transition<in Bitmap>?) {
                                        try {



                                            pDialog!!.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                            pDialog!!.show();
                                            AidlUtil.getInstance().printRecipte(data,resourcee,viewmodel);
                                            Glide.with(context as CompanyDetails)
                                                .asBitmap()
                                                .load("http://across-cities.com/"+data.notesimg)
                                                .into(object : SimpleTarget<Bitmap>(100, 100) {
                                                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                                        try {

                                                            IPosPrinterTestDemo.getInstance().printText(data,resourcee,resource, viewmodel);

                                                            val homeIntent = Intent(context, Payment::class.java)

                                                            homeIntent.putExtra("myobj", data)

                                                            (context as CompanyDetails).startActivity(homeIntent)

                                                        } catch (e: IOException) {
                                                            // handle exception
                                                        }


                                                    }})
                                            val homeIntent = Intent(context, Payment::class.java)
                                            homeIntent.putExtra("myobj", data)
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
                                        AidlUtil.getInstance().printRecipte(it, resourcee,viewModel);
                                        Glide.with(context as MainActivity)
                                            .asBitmap()
                                            .load("http://across-cities.com/"+it.notesimg)
                                            .into(object : SimpleTarget<Bitmap>(100, 100) {
                                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                                    try {

                                                        IPosPrinterTestDemo.getInstance().printText(it,resourcee,resource, viewModel);

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
                                            AidlUtil.getInstance().printRecipte(card, resource,null);
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