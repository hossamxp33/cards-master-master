package com.codesroots.hossam.mandoobapp.presentation.login.ViewModel



import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


import com.codesroots.hossam.mandoobapp.presentation.login.repository.LoginRepository
import com.codesroots.mac.cards.models.CompanyDatum
import com.codesroots.mac.cards.models.LogData
import com.codesroots.mac.firstkotlon.DataLayer.Repo.DataRepo

import java.util.ArrayList

class ResetPWViewModel : ViewModel() {
     var loginRepository: LoginRepository = LoginRepository()


    var coderesponse = MutableLiveData<Int>()
    var DateRepoCompnay: DataRepo = DataRepo()

    private val error = MutableLiveData<String>()

    var ResetPassWordLD = MutableLiveData<CompanyDatum>()

    init {

        ResetPassWordLD  = MutableLiveData()

    }

    fun ResetPassWord(auth:String ,un:String ,opw : String,npw: String) {
        DateRepoCompnay.ResetPWord(auth,un,opw,npw,ResetPassWordLD)
    }


}