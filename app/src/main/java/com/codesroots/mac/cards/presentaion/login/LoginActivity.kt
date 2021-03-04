package com.codesroots.mac.cards.presentaion.login


import android.app.AlertDialog
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
import cn.pedant.SweetAlert.SweetAlertDialog
import com.codesroots.hossam.mandoobapp.presentation.login.ViewModel.LoginViewModel
import com.codesroots.mac.cards.DataLayer.helper.PreferenceHelper
import com.codesroots.mac.cards.DataLayer.usecases.checkUserLogin
import com.codesroots.mac.cards.R
import com.codesroots.mac.cards.presentaion.MainActivity
import com.codesroots.mac.cards.presentaion.isInternetConnectionAvailable
import com.codesroots.mac.cards.presentaion.snack
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_signin.btnLogin
import org.jetbrains.anko.internals.AnkoInternals.getContext

class LoginActivity : AppCompatActivity() {
    var network_enabled = false
    var pDialog : SweetAlertDialog? = null;

    private val viewModel: LoginViewModel by lazy {
        ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

      setContentView(R.layout.activity_login)
        PreferenceHelper(this)

        if (checkUserLogin(this))
            startActivity(Intent(this  , MainActivity::class.java))


        Loginbtn.setOnClickListener {
            PreferenceHelper.setUsername(username.text.toString())
//PreferenceHelper.setToken("0",this)
            if (PreferenceHelper.getToken() != "0" ) {
            //   "تاني مرة".snack(window.decorView.rootView)
                if (!isInternetConnectionAvailable(this)) "رجاء تأكد من اتصالك بالانترنت".snack(window.decorView.rootView)
                viewModel.Login(username.text.toString(),password.text.toString())
            }else {
                if (!isInternetConnectionAvailable(this)) "رجاء تأكد من اتصالك بالانترنت".snack(window.decorView.rootView)
                viewModel.LoginFirstTime(username.text.toString(),password.text.toString())
               // "اول مرة".snack(window.decorView.rootView)
            }
        }

        viewModel.loginResponseLD?.observe(this , Observer {
         //   "سجل".snack(window.decorView.rootView)


    if (it.auth == null){
        val alertDialogBuilder = AlertDialog.Builder(this)
        pDialog =  SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        pDialog!!.setTitleText("يوجد خطأ!")
        pDialog!!.setContentText("كلمة المرور خاطئة")
        pDialog!!.setConfirmText("OK")
        pDialog!!.show()
    }else {
       PreferenceHelper.setToken(it.taken,this)
        PreferenceHelper.setAuthId(it.auth,this)

        startActivity(Intent(this, MainActivity::class.java))
    }
        })

        viewModel.coderesponse.observe(this , Observer {
            if (it!=200) "Registration failed ".snack(window.decorView.rootView)
        })

    }


    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }
        })
    }
}
