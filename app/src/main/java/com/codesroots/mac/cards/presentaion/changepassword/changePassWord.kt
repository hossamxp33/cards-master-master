package com.codesroots.mac.cards.presentaion.changepassword


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.codesroots.hossam.mandoobapp.presentation.login.ViewModel.LoginViewModel
import com.codesroots.hossam.mandoobapp.presentation.login.ViewModel.ResetPWViewModel
import com.codesroots.mac.cards.DataLayer.helper.PreferenceHelper
import com.codesroots.mac.cards.DataLayer.usecases.checkUserLogin
import com.codesroots.mac.cards.R
import com.codesroots.mac.cards.models.CompanyDatum
import com.codesroots.mac.cards.presentaion.MainActivity
import com.codesroots.mac.cards.presentaion.isInternetConnectionAvailable
import com.codesroots.mac.cards.presentaion.mainfragment.viewmodel.MainViewModel
import com.codesroots.mac.cards.presentaion.snack
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_login.username
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_signin.btnLogin
import kotlinx.android.synthetic.main.reset_pw.*
import org.jetbrains.anko.internals.AnkoInternals.getContext

class changePassWord : AppCompatActivity() {
    var data : CompanyDatum ? =null
    var network_enabled = false
     lateinit  var viewModel: ResetPWViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      setContentView(R.layout.reset_pw)
        PreferenceHelper(this)
        viewModel =   ViewModelProviders.of(this).get(ResetPWViewModel::class.java)

        change_pw_btn.setOnClickListener {
                if (!isInternetConnectionAvailable(this)) "رجاء تأكد من اتصالك بالانترنت".snack(window.decorView.rootView)
                viewModel.ResetPassWord(PreferenceHelper.getAuthId(),reusername.text.toString(),repassword.text.toString(),confirm_password.text.toString())
                "انتظر".snack(window.decorView.rootView)
        }

        viewModel.ResetPassWordLD.observe(this , Observer {
            if (it == null){

                "كلمة المرور خاطئة".snack(window.decorView.rootView)
            }else {

                startActivity(Intent(this, MainActivity::class.java))
            }
        })

        viewModel.coderesponse.observe(this , Observer {
            if (it!=200) "Registration failed ".snack(window.decorView.rootView)
        })

    }

}
