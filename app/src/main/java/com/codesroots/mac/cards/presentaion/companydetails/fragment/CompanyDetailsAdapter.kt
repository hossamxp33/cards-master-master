package com.codesroots.mac.cards.presentaion.reportsFragment.adapters


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.codesroots.mac.cards.R
import com.codesroots.mac.cards.databinding.CompanyDetailsItemBinding
import com.codesroots.mac.cards.databinding.MainAdapterBinding
import com.codesroots.mac.cards.databinding.ReportItemBinding
import com.codesroots.mac.cards.models.CompanyDatum
import com.codesroots.mac.cards.models.MyLocationUseCase
import com.codesroots.mac.cards.presentaion.ClickHandler
import com.codesroots.mac.cards.presentaion.MainActivity
import com.codesroots.mac.cards.presentaion.companydetails.fragment.CompanyDetails
import com.codesroots.mac.cards.presentaion.mainfragment.viewmodel.MainViewModel
import java.lang.Exception

class CompanyDetailsAdapter ( var viewModel: MainViewModel,var context :Context?,var data:List<CompanyDatum>, val listener: ContentListener) : RecyclerView.Adapter<CustomViewHolders>() {
    var  companyDetails : CompanyDetails? = null
    var row_index : Int ? = -1
var process : Int ? = -1

    override fun getItemCount(): Int {

        return  data.size

    }
    init {
        data
    }



    override fun onBindViewHolder(p0: CustomViewHolders, p1: Int) {
        p0.bind(viewModel,context,data.get(p1),viewModel)

        viewModel.SeekBarNumber.observe(p0.binding.categoryPrice.context as LifecycleOwner, Observer {

            process = it
         try {
             if (it >= 0 && it <= 9) {

                 listener.onItemClicked(data.get(0))
                 p0.binding.categoryPrice.setSelected(row_index == 0);
                 row_index = p0.layoutPosition;
                 //  notifyItemChanged(row_index!!);
             } else if (it >= 10 && it <= 19) {
                 listener.onItemClicked(data.get(1))
                 p0.binding.categoryPrice.setSelected(row_index == 1);
                 row_index = p0.layoutPosition
             } else if (it >= 20 && it <= 29)
                 listener.onItemClicked(data.get(2))
             p0.binding.categoryPrice.setSelected(row_index == 2);
             row_index = p0.layoutPosition
         }catch (e:Exception){

         }
        })




//        listener.onItemClicked(data.get(p1))
//
//        p0.binding.categoryPrice.setSelected(row_index == p1);
//
//        p0.binding.categoryPrice?.setOnClickListener {
//            row_index = p0.layoutPosition;
//          //  notifyItemChanged(row_index!!);
//            notifyItemChanged(row_index!!);
//
//            //notifyDataSetChanged()
//        }


    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolders {
        val  binding: CompanyDetailsItemBinding = DataBindingUtil.inflate (LayoutInflater.from(p0.context),
            com.codesroots.mac.cards.R.layout.company_details_item,p0,false)



        return  CustomViewHolders(binding)
    }


}
class CustomViewHolders (
    public var binding:CompanyDetailsItemBinding
) : RecyclerView.ViewHolder(binding.root){

    fun bind(viewModel:MainViewModel,context: Context?,data:CompanyDatum,viewModels: MainViewModel) {

        binding.listener = ClickHandler()
        binding.viewmodel = viewModels
        binding.data = data
        binding.context = context as CompanyDetails?


    }

}
public interface ContentListener {
    fun onItemClicked(item: CompanyDatum)
}
