package com.codesroots.mac.cards.presentaion.payment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.codesroots.mac.cards.R
import com.codesroots.mac.cards.databinding.ActivityPaymentBinding
import com.codesroots.mac.cards.databinding.PaymentNewBinding
import com.codesroots.mac.cards.models.Buypackge
import com.codesroots.mac.cards.presentaion.MainActivity
import com.codesroots.mac.cards.presentaion.Printer.AidlUtil
import com.codesroots.mac.cards.presentaion.Printer.ESCUtil
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.payment_new.*
import woyou.aidlservice.jiuiv5.IWoyouService
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class Payment : AppCompatActivity() {

    private var woyouService: IWoyouService? = null

    var dataa: Buypackge? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        val binding = DataBindingUtil.setContentView<PaymentNewBinding>(this, R.layout.payment_new)

        var extras = intent.extras
        val value = extras?.getParcelable<Buypackge>("myobj")
        if (!value!!.pencode.isNullOrEmpty()) {

            value!!.pencode!!.forEach {

                numbervalue.append(it.pencode)
                numbervalue.append("\n")
                numbervalue.text.toString()
                serialvalue.append(it.serial)
                serialvalue.append("\n")
                serialvalue.text.toString()
            }

        }
        binding.buy = value
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        val homeIntent = Intent(this, MainActivity::class.java)
        startActivity(homeIntent)
    }
}