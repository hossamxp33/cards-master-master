package com.codesroots.mac.cards.presentaion.companydetails.fragment

import android.annotation.SuppressLint
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
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.codesroots.mac.cards.R
import com.codesroots.mac.cards.databinding.CompanyDetailsBinding
import com.codesroots.mac.cards.databinding.CompanyDetailsNewBinding
import com.codesroots.mac.cards.models.CompanyDatum
import com.codesroots.mac.cards.presentaion.ClickHandler
import com.codesroots.mac.cards.presentaion.MainActivity
import com.codesroots.mac.cards.presentaion.Printer.AidlUtil
import com.codesroots.mac.cards.presentaion.mainfragment.viewmodel.MainViewModel
import com.codesroots.mac.cards.presentaion.mainfragment.viewmodel.setImageResourcee
import com.codesroots.mac.cards.presentaion.reportsFragment.adapters.CompanyDetailsAdapter
import com.codesroots.mac.cards.presentaion.reportsFragment.adapters.ContentListener
import com.mazenrashed.printooth.Printooth
import kotlinx.android.synthetic.main.company_details.*
import kotlinx.android.synthetic.main.company_details.logo
import kotlinx.android.synthetic.main.company_details.progressBar
import kotlinx.android.synthetic.main.company_details.progressBar2
import kotlinx.android.synthetic.main.company_details.recyler
import kotlinx.android.synthetic.main.company_details.view.*
import kotlinx.android.synthetic.main.company_details_new.*
import kotlinx.android.synthetic.main.dialog_custom_view.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


public class CompanyDetails  : AppCompatActivity() , ContentListener {

var item :CompanyDatum? = null
    override fun onItemClicked(item: CompanyDatum) {
        Company_id = item.id
        progress_number = item.sprice!!
        this.item = item
            totalvalue = item.sprice
            totalvalue?.let { displaytext(it,item.rprice!!,item.sprice!!) }


    }
    lateinit var MainAdapter: CompanyDetailsAdapter
    lateinit var viewModel: MainViewModel
    var data : List<CompanyDatum>? = null
    var  binding : CompanyDetailsNewBinding? = null
    var minteger = 1
    var Company_id : String? = null
    var progress_number : String ?=null

    var NUM_PAGES = 0
    var currentPage = 0
      var totalvalue : String ? = null
    var radioGroup: RadioGroup? = null
    lateinit var radioButton: RadioButton
    @SuppressLint("ResourceType")
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.company_details_new)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        var extras = intent.extras
        val packageId = extras?.getString("packageId")

        binding!!.context = this
        binding!!.listener = ClickHandler()
        binding!!.viewmodel = viewModel

        // Set a SeekBar change listener
        binding!!.opacitySeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                // Display the current progress of SeekBar
              binding!!.textView.text = "$progress_number لقد اخترت : "

                viewModel.getSeekBarNumber(i)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Do something
              //  Toast.makeText(applicationContext,"start tracking",Toast.LENGTH_SHORT).show()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Do something
          //      Toast.makeText(applicationContext,"stop tracking",Toast.LENGTH_SHORT).show()
            }
        })

        // Assigning id of RadioGroup
        radioGroup = findViewById(R.id.radioGroup1)
        // Getting the checked radio button id
        // from the radio group
       // minteger = radioGroup!!.checkedRadioButtonId

        // Assigning id of the checked radio button
       // radioButton = findViewById(minteger)
          radioButton1.id = 0
          radioButton2.id = 1
          radioButton3.id = 2
          radioButton4.id = 3


        radioGroup!!.setOnCheckedChangeListener { group, checkedId ->

            minteger = getString(
                when (checkedId) {
                    0 -> { R.string.radio1 }
                    1 -> { R.string.radio2 }
                    2 -> { R.string.radio3 }
                    else -> { R.string.radio4 }
                }
            ).toInt()
            display(minteger)
            Toast.makeText(applicationContext, minteger.toString(), Toast.LENGTH_SHORT).show()
        }



       // minteger   = Integer.parseInt(integer_number.getText().toString());

///*** using for company details xml design

//        decrease.setOnClickListener {
//            if (minteger > 1) {
//                decreaseInteger (decrease)
//            }else{
//                Log.d("src", "Value can't be less than 0");
//            }
//        }
//        increase.setOnClickListener {
//            if (minteger >=1) {
//                increaseInteger (increase)
//            }else{
//                Log.d("src", "Value can't be less than 0");
//            }
//        }
//

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getPackageDetails(packageId!!)

        viewModel.CompanyResponseLD?.observe(this, Observer {

            MainAdapter = CompanyDetailsAdapter(viewModel,this, it,this)
            recyler.layoutManager = GridLayoutManager(this,1,GridLayoutManager.VERTICAL, false)
            recyler.adapter = MainAdapter;

            data = it


            setImageResourcee(logo,data!!.get(0).src)
            progressBar.setVisibility(View.GONE)
            progressBar2.setVisibility(View.GONE)

        })




        binding!!.total.text = totalvalue.toString()


    }

///*** using for company details xml design

    fun display(number: Int) {
        if (number <= 2) {
//            val displayInteger = findViewById<View>(R.id.integer_number) as TextView
            val totalInteger = findViewById<View>(R.id.totalvalue) as TextView
            val Discount = findViewById<View>(R.id.discountvalue) as TextView
            val TotalValue = findViewById<View>(R.id.total) as TextView
            Discount.text = "" + (number * item!!.rprice!!.toInt())
            TotalValue.text = "" + (number * totalvalue!!.replace(
                " IQD",
                ""
            )!!.toInt() + item!!.rprice!!.toInt()) + "IQD"

            totalInteger.text = "" + (number * totalvalue!!.replace(" IQD", "")!!.toInt()) + "IQD"
         //   displayInteger.text = "" + number
        }else {
            var pDialog : SweetAlertDialog? = null;
            val alertDialogBuilder = AlertDialog.Builder(this)
            pDialog =  SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText("يوجد خطأ!")
            pDialog.setContentText("لا يمكن شراء اكثر من بطاقتين")
            pDialog.setConfirmText("OK")
            pDialog.show()

        }
    }
    fun displaytext(number: String,discount: String,total: String) {
        val displayInteger = findViewById<View>(R.id.totalvalue) as TextView
        val Discount = findViewById<View>(R.id.discountvalue) as TextView
        val TotalValue = findViewById<View>(R.id.total) as TextView

        displayInteger.text = "" + number
        Discount.text = "" + discount
        TotalValue.text = "" + (total!!.replace(" IQD", "")!!.toInt() + item!!.rprice!!.toInt()) + "IQD"
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