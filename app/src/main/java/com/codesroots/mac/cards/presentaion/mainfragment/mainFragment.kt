package com.codesroots.mac.cards.presentaion.mainfragment

import android.accessibilityservice.GestureDescription
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.codesroots.mac.cards.DataLayer.helper.PreferenceHelper
import com.codesroots.mac.cards.DataLayer.usecases.openNewTabWindow
import com.codesroots.mac.cards.R
import com.codesroots.mac.cards.databinding.MainFragmentBinding
import com.codesroots.mac.cards.models.CompanyDatum
import com.codesroots.mac.cards.presentaion.ClickHandler
import com.codesroots.mac.cards.presentaion.MainActivity
import com.codesroots.mac.cards.presentaion.mainfragment.Adapter.MainAdapter
import com.codesroots.mac.cards.presentaion.mainfragment.Adapter.SliderAdapter
import com.codesroots.mac.cards.presentaion.mainfragment.viewmodel.MainViewModel
import com.codesroots.mac.cards.presentaion.mainfragment.viewmodel.setImageResource
import com.codesroots.mac.cards.presentaion.reportsFragment.adapters.CompanyDetailsAdapter
import com.codesroots.mac.cards.presentaion.reportsFragment.adapters.ContentListener
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printer.Printer
import com.mazenrashed.printooth.utilities.Printing
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum
import com.nightonke.boommenu.BoomMenuButton
import com.nightonke.boommenu.ButtonEnum
import com.nightonke.boommenu.Piece.PiecePlaceEnum
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_custom_view.view.*
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.view.*
import kotlinx.android.synthetic.main.main_fragment.view.recyler
import java.util.*

class mainFragment  : Fragment(), ContentListener {
    override fun onItemClicked(item: CompanyDatum) {

    }

    lateinit var CompanyAdapter: CompanyDetailsAdapter
    lateinit var MainAdapter: MainAdapter
    lateinit var viewModel: MainViewModel
    private var currentPage = 0
    private var NUM_PAGES = 0
    var pager: ViewPager? = null
    var data : List<CompanyDatum>? = null
    var  bmb: BoomMenuButton? = null;
    private var printing : Printing? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view:MainFragmentBinding =
            DataBindingUtil.inflate(inflater,R.layout.main_fragment, container,false)
        val typeface = Typeface.createFromAsset(getContext()!!.assets, "fonts/DroidKufi_Regular.ttf")

        view.listener = ClickHandler()
        view.context = context as MainActivity
        view.textView12.text = ( if (Printooth.hasPairedPrinter()) "Un-pair ${Printooth.getPairedPrinter()?.name}" else "Pair with printer")

        pager = view.pager
        viewModel =   ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getcompanyData()
        viewModel.getMyBalance()
        viewModel.GetMyImages(PreferenceHelper.getAuthId())
        view.viewModel = viewModel

        viewModel.CompanyResponseLD?.observe(this , Observer {

            MainAdapter = MainAdapter(viewModel,context,it)
            view.recyler.layoutManager = GridLayoutManager(context, 3)
            data = it
          

            view.recyler.adapter = MainAdapter;
            view.textView11.typeface = typeface
            view.textView5.typeface = typeface
            view.lastvalue.typeface = typeface

            view.value.typeface = typeface

        })
        viewModel.MyBalanceResponseLD?.observe(this , Observer {

            view.myBalance = it
            view.value.text = it.commession
        })
        viewModel.SliderDataResponseLD?.observe(this , Observer {

            view.pager.adapter = it?.let { it1 -> SliderAdapter(activity!!, it1) }
            indicator.setViewPager(view.pager)

            it?.size?.let { it1 -> init(it1) }
            stoploading()

        })

        view.naz2.setOnClickListener (){
            val packageId = arguments?.getString("packageId")
            openNewTabWindow("https://al-fateh-iq.com/",context as MainActivity)
        }

        view.bottomNavigation1.setOnClickListener (){
            val packageId = arguments?.getString("packageId")
            openNewTabWindow("https://play.google.com/store/apps/details?id=com.codesroots.agwad",context as MainActivity)
        }
        view.tajnaz.setOnClickListener (){
            val packageId = arguments?.getString("packageId")
            openNewTabWindow("https://play.google.com/store/apps/details?id=com.codesroots.mac.Tajnaz",context as MainActivity)
        }
        return view.root;
    }
    private lateinit var alertDialog: AlertDialog
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)

    fun showCustomDialog() {

        val inflater: LayoutInflater = this.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.dialog_custom_view, null)

        viewModel.CompanyResponseLD?.observe(this, Observer {
            CompanyAdapter = CompanyDetailsAdapter(viewModel,activity, it,this)
            dialogView.recyler.layoutManager = GridLayoutManager(context,3)
            dialogView.recyler.adapter = CompanyAdapter;
            data = it

        })
        val logoicon = dialogView.logoo
        setImageResource( logoicon ,data!!.get(0).src)


        val header_txt = dialogView.findViewById<TextView>(R.id.header)
        header_txt.text = "الشراء السريع"
        val custom_button: Button = dialogView.findViewById(R.id.customBtn)

        custom_button.setOnClickListener {
            // Dismiss the popup window
            alertDialog.dismiss()

        }

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context!!,R.style.yourCustomDialog)

        dialogBuilder.setOnDismissListener(object : DialogInterface.OnDismissListener {
            override fun onDismiss(arg0: DialogInterface) {
            }
        })
        dialogBuilder.setView(dialogView)
        alertDialog = dialogBuilder.create();
        alertDialog.show()

    }

    private fun animation(){
        val ttb = AnimationUtils.loadAnimation(context, R.anim.ttb)
        pager!!.animation = ttb

    }
    private fun init(size: Int) {
        val density = getResources().getDisplayMetrics().density
        indicator.setRadius(4 * density)
        NUM_PAGES = size
        val handler = Handler()
        val Update:Runnable =Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            pager?.setCurrentItem(currentPage++, true)
        }
        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 4000, 10000)
        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                currentPage = position
            }

            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(pos: Int) {}
        })
    }
    override fun onResume() {
        super.onResume()
        shimmer_view_container2.startShimmerAnimation()

    }
    override fun onPause() {
        shimmer_view_container2?.stopShimmerAnimation()

        super.onPause()
    }

    fun stoploading() {

        shimmer_view_container2?.stopShimmerAnimation()

        shimmer_view_container2?.setVisibility(View.GONE)

    }
}