package com.codesroots.mac.cards.presentaion.banks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.codesroots.mac.cards.R
import com.codesroots.mac.cards.presentaion.banks.adapter.banksAdapter
import com.codesroots.mac.cards.presentaion.mainfragment.viewmodel.MainViewModel
import com.codesroots.mac.cards.presentaion.reportsFragment.adapters.CompanyDetailsAdapter
import kotlinx.android.synthetic.main.bank_activity.*
import kotlinx.android.synthetic.main.company_details.view.*

class BankActitvity: AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var MainAdapter: banksAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bank_activity)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getBanksData()

        viewModel.BankResponseLD?.observe(this, Observer {

            MainAdapter = banksAdapter(viewModel, this, it)
            recyler.layoutManager = LinearLayoutManager(this)
            recyler.adapter = MainAdapter;
        })

    }

}