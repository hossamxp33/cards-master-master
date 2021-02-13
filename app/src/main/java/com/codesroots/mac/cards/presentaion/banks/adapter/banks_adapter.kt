package com.codesroots.mac.cards.presentaion.banks.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.codesroots.mac.cards.R
import com.codesroots.mac.cards.databinding.BanksBinding
import com.codesroots.mac.cards.models.BankDatum
import com.codesroots.mac.cards.presentaion.ClickHandler
import com.codesroots.mac.cards.presentaion.banks.BankActitvity
import com.codesroots.mac.cards.presentaion.mainfragment.viewmodel.MainViewModel
class banksAdapter  ( var viewModel: MainViewModel,var context :Context?,var data:List<BankDatum>) : RecyclerView.Adapter<CustomViewHolders>() {
    override fun getItemCount(): Int {

        return  data.size

    }

    override fun onBindViewHolder(p0:CustomViewHolders, p1: Int) {
        p0.bind(viewModel,context,data.get(p1),viewModel)

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolders {
//        val layoutInflater  = LayoutInflater.from(p0.context);
//        val cellforrow = layoutInflater.inflate(R.layout.main_adapter,p0,false);

//        val layoutParams = cellforrow.getLayoutParams()
//        layoutParams.height = (p0.getHeight() /  2).toInt()
//        layoutParams.width = (p0.getWidth() /  2.5).toInt()
//        cellforrow.setLayoutParams(layoutParams)
        val  binding: BanksBinding = DataBindingUtil.inflate (
            LayoutInflater.from(p0.context),
            R.layout.banks,p0,false)

        return  CustomViewHolders(binding)
    }


}
class CustomViewHolders (
    private val binding: BanksBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        viewModel: MainViewModel,
        context: Context?,
        data: BankDatum,
        viewModels: MainViewModel
    ) {

        binding.listener = ClickHandler()
        binding.viewmodel = viewModels
        binding.data = data
        binding.context = context as BankActitvity
    }

}