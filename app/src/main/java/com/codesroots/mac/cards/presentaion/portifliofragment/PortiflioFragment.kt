package com.codesroots.mac.cards.presentaion.portifliofragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.codesroots.mac.cards.DataLayer.helper.PreferenceHelper
import com.codesroots.mac.cards.R
import com.codesroots.mac.cards.databinding.PortiflioFragmentBinding
import com.codesroots.mac.cards.databinding.PortiflioFragmentBindingImpl
import com.codesroots.mac.cards.databinding.ReportFragmentBinding
import com.codesroots.mac.cards.db.CardDatabase
import com.codesroots.mac.cards.db.toast
import com.codesroots.mac.cards.models.Buypackge
import com.codesroots.mac.cards.presentaion.MainActivity
import com.codesroots.mac.cards.presentaion.mainfragment.viewmodel.MainViewModel
import com.codesroots.mac.cards.presentaion.reportsFragment.adapters.CompanyDetailsAdapter
import com.codesroots.mac.cards.presentaion.reportsFragment.adapters.report_adapters
import kotlinx.android.synthetic.main.main_fragment.view.*
import kotlinx.android.synthetic.main.portiflio_fragment.view.*
import kotlinx.android.synthetic.main.saved_layout.*
import kotlinx.android.synthetic.main.saved_layout.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PortiflioFragment : Fragment() {
    lateinit var Adapterr : report_adapters
    lateinit var viewModel: MainViewModel
       val card: Buypackge? = null
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,  savedInstanceState: Bundle? ): View? {
        var view: PortiflioFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.portiflio_fragment,container,false )

        view.recyclePortiflio.layoutManager = LinearLayoutManager(activity!!.applicationContext);
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        view.context = context as MainActivity
viewModel.GetNonPrinted(PreferenceHelper.getAuthId());

       // if (viewModel.GetNonPrinted?.hasObservers() == false) {

            viewModel.GetNonPrinted?.observe(this, Observer {

                Adapterr = report_adapters(viewModel,context, it)
                view.recyclePortiflio.layoutManager = LinearLayoutManager(context)
                view.recyclePortiflio.adapter = Adapterr;
            })

      //  }
       // fetchdata(view)


        return view.root

    }


 private   fun fetchdata(view: View) {
     GlobalScope.launch {
         activity!!.applicationContext.let {

             val db = Room.databaseBuilder(
                 activity!!.applicationContext,
                 CardDatabase::class.java, "card-database"
             ).build()
          var data =   db.getCardDao().GetAllData()
          println(data)
             Thread {
                 activity!!.runOnUiThread {
                     view.recycle_portiflio.adapter = portifolioAdapter(viewModel,context,data)
                 }
             }.start()
         }

                   }
}



}
