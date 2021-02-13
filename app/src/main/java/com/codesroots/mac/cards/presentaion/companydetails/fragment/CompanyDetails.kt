package com.codesroots.mac.cards.presentaion.companydetails.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.codesroots.mac.cards.R
import com.codesroots.mac.cards.models.CompanyDatum
import com.codesroots.mac.cards.presentaion.Printer.AidlUtil
import com.codesroots.mac.cards.presentaion.mainfragment.viewmodel.MainViewModel
import com.codesroots.mac.cards.presentaion.reportsFragment.adapters.CompanyDetailsAdapter
import kotlinx.android.synthetic.main.company_details.*
import kotlinx.android.synthetic.main.company_details.view.*
import kotlinx.android.synthetic.main.dialog_custom_view.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.runOnUiThread
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class CompanyDetails  : Fragment() {

    lateinit var MainAdapter: CompanyDetailsAdapter
    lateinit var viewModel: MainViewModel
var data :List<CompanyDatum>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.company_details, container, false)
        viewModel = ViewModelProviders.of(this).get(
            MainViewModel::
            class.java
        )
        if (arguments != null) {
            AidlUtil.getInstance().initPrinter()
            Toast.makeText(context, AidlUtil.getInstance().isConnect().toString(), Toast.LENGTH_LONG).show()

            val packageId = arguments?.getString("packageId")
            viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
            viewModel.getPackageDetails(packageId!!)
            viewModel.CompanyResponseLD?.observe(this, Observer {
                data = it
                MainAdapter = CompanyDetailsAdapter(viewModel, context, it)
                view.recyler.layoutManager = LinearLayoutManager(context)
                view.recyler.adapter = MainAdapter;
            })
        }
        view.usageway.setOnClickListener{ showCustomDialog() }

        return  view
    }
    private lateinit var alertDialog: AlertDialog
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun showCustomDialog() {
        val inflater: LayoutInflater = this.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.dialog_custom_view, null)

        val details_image = dialogView.details
        Glide.with(context!!).load(data!!.first().detailimg).into(details_image)

        val custom_button: Button = dialogView.findViewById(R.id.customBtn)
        custom_button.setOnClickListener {
            // Dismiss the popup window
            alertDialog.dismiss()        }
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context!!,R.style.yourCustomDialog)

        dialogBuilder.setOnDismissListener(object : DialogInterface.OnDismissListener {
            override fun onDismiss(arg0: DialogInterface) {
            }
        })
        dialogBuilder.setView(dialogView)
        alertDialog = dialogBuilder.create();
        alertDialog.show()

    }
}