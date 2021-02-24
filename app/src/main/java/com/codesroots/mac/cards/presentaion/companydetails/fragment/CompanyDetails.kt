package com.codesroots.mac.cards.presentaion.companydetails.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.codesroots.mac.cards.R
import com.codesroots.mac.cards.databinding.CompanyDetailsBinding
import com.codesroots.mac.cards.models.CompanyDatum
import com.codesroots.mac.cards.presentaion.ClickHandler
import com.codesroots.mac.cards.presentaion.Printer.AidlUtil
import com.codesroots.mac.cards.presentaion.mainfragment.viewmodel.MainViewModel
import com.codesroots.mac.cards.presentaion.mainfragment.viewmodel.setImageResourcee
import com.codesroots.mac.cards.presentaion.reportsFragment.adapters.CompanyDetailsAdapter
import com.codesroots.mac.cards.presentaion.reportsFragment.adapters.ContentListener
import com.google.android.gms.analytics.internal.zzy.v
import kotlinx.android.synthetic.main.company_details.*
import kotlinx.android.synthetic.main.company_details.view.*
import kotlinx.android.synthetic.main.dialog_custom_view.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


public class CompanyDetails  : AppCompatActivity() , ContentListener {


    override fun onItemClicked(item: CompanyDatum) {
        Company_id = item.id

        fun value() {
            totalvalue = item.sprice
            totalvalue?.let { displaytext(it) }
        }
        value()



    }
    lateinit var MainAdapter: CompanyDetailsAdapter
    lateinit var viewModel: MainViewModel
    var data : List<CompanyDatum>? = null
    var  binding : CompanyDetailsBinding? = null
    var minteger = 1
    var Company_id : String? = null
    var NUM_PAGES = 0
    var currentPage = 0
      var totalvalue : String ? = null
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.company_details)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        var extras = intent.extras
        val packageId = extras?.getString("packageId")
        binding!!.context = this
        binding!!.listener = ClickHandler()
        binding!!.viewmodel = viewModel

        minteger   = Integer.parseInt(integer_number.getText().toString());


        decrease.setOnClickListener {
            if (minteger > 1) {
                decreaseInteger (decrease)
            }else{
                Log.d("src", "Value can't be less than 0");
            }
        }
        increase.setOnClickListener {
            if (minteger >=1) {
                increaseInteger (increase)
            }else{
                Log.d("src", "Value can't be less than 0");
            }
        }


        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getPackageDetails(packageId!!)

        viewModel.CompanyResponseLD?.observe(this, Observer {

            MainAdapter = CompanyDetailsAdapter(viewModel,this, it,this)
            recyler.layoutManager = GridLayoutManager(this,3,GridLayoutManager.VERTICAL, false)
            recyler.adapter = MainAdapter;

            data = it


            setImageResourcee(logo,data!!.get(0).src)
           progressBar.setVisibility(View.GONE)
            progressBar2.setVisibility(View.GONE)

        })




        binding!!.total.text = totalvalue.toString()


    }


    public fun display(number: Int) {
        val displayInteger = findViewById<View>(R.id.integer_number) as TextView

        displayInteger.text = "" + number
    }
    public fun displaytext(number: String) {
        val displayInteger = findViewById<View>(R.id.total) as TextView

        displayInteger.text = "" + number
    }
    fun increaseInteger(view: View) {

        minteger += 1
        display(minteger)

    }


    fun decreaseInteger(view: View) {

        minteger -= 1

        display(minteger)
    }


}