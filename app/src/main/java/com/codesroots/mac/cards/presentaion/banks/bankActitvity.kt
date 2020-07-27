package com.codesroots.mac.cards.presentaion.banks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.codesroots.mac.cards.R
import com.codesroots.mac.cards.presentaion.mainfragment.viewmodel.MainViewModel

class bankActitvity: AppCompatActivity() {

    lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        viewModel =   ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getMyBalance()

    }

}