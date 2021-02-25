package com.codesroots.mac.cards.presentaion.menufragmen

import android.content.Intent
import com.codesroots.mac.cards.R
import com.codesroots.mac.cards.presentaion.MainActivity
import com.codesroots.mac.cards.presentaion.login.LoginActivity
import com.codesroots.mac.cards.presentaion.payment.Payment
import kotlinx.android.synthetic.main.menu_fragment.view.*


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.codesroots.mac.cards.DataLayer.helper.PreferenceHelper
import com.codesroots.mac.cards.DataLayer.usecases.checkUserLogin
import com.codesroots.mac.cards.presentaion.changepassword.changePassWord
import com.codesroots.mac.cards.presentaion.mainfragment.mainFragment


class MenuFragment : Fragment() {

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,  savedInstanceState: Bundle? ): View? {

        val view= inflater.inflate(R.layout.menu_fragment, container, false)

//        view.login.setOnClickListener {
//            val homeIntent = Intent(context, LoginActivity::class.java)
//            ( context as MainActivity).startActivity(homeIntent)
//        }
        view.partners.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_frame,Partners())?.addToBackStack("login")?.commit()
        }
        view.back_btn.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_frame,
                mainFragment()
            )?.addToBackStack("home")?.commit()
        }
//
        view.profile.setOnClickListener {
            if (checkUserLogin(context!!))
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_frame,ContactFragment())?.addToBackStack("login")?.commit()
        }
//
        view.favoffers.setOnClickListener {
            if (checkUserLogin(context!!))
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_frame,TermsFragment())?.addToBackStack(null)?.commit()
        }

        view.logout.setOnClickListener {
            if (checkUserLogin(context!!)) {
                PreferenceHelper.setAuthId("0",context)
                Toast.makeText(context, "تم تسجيل خروجك", Toast.LENGTH_SHORT).show()
                val homeIntent = Intent(context, LoginActivity::class.java)
                ( context as MainActivity).startActivity(homeIntent)
            }
        }
        view.change_pw.setOnClickListener {

          //      PreferenceHelper.setAuthId("0",context)
       //         Toast.makeText(context, "تم تسجيل خروجك", Toast.LENGTH_SHORT).show()
                val homeIntent = Intent(context, changePassWord::class.java)
                ( context as MainActivity).startActivity(homeIntent)

        }
        return view
    }

}
