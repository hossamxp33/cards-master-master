package com.codesroots.mac.firstkotlon.DataLayer.Repo

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.codesroots.mac.cards.DataLayer.helper.PreferenceHelper
import com.codesroots.mac.cards.models.*
import com.codesroots.mac.firstkotlon.DataLayer.ApiService.APIServices

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class  DataRepo {


    @SuppressLint("CheckResult")


    fun Login(username:String,password:String,livedata: MutableLiveData<LogData>?) {

        APIServices.create().Login(PreferenceHelper.getToken(),username,password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { data -> data }
            .subscribe(
                { books ->
                    livedata?.postValue(books)
                },
                { error ->

                }
            )
    }
    //////Login
    fun ResetPWord(auth:String ,un:String ,opw : String,npw: String ,livedata: MutableLiveData<CompanyDatum>?) {

        APIServices.create().ResetPassWord(auth,opw,npw)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { data -> data }
            .subscribe(
                { books ->
                    livedata?.postValue(books)
                },
                { error ->

                }
            )
    }

    fun LoginFirstTime(username:String,password:String,livedata: MutableLiveData<LogData>?) {

        APIServices.create().LoginFirstTime(username,password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { data -> data }
            .subscribe(
                { books ->
                    livedata?.postValue(books)
                },
                { error ->

                }
            )
    }
    @SuppressLint("CheckResult")
    fun GetData(livedata: MutableLiveData<List<CompanyDatum>>?) {

        APIServices.create().GetCompanyData(PreferenceHelper.getAuthId())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { data -> data }

            .subscribe(
                { success ->
                    livedata?.postValue(success)
                },
                { error ->

                }
            )
    }


    @SuppressLint("CheckResult")

    fun GetPackageDetails(id:String,livedata: MutableLiveData<List<CompanyDatum>>?) {

        APIServices.create().GetPackageDetails(PreferenceHelper.getAuthId(),id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { data -> data }
            .subscribe(
                { success ->
                    livedata?.postValue(success)
                },
                { error ->

                }
            )
    }

    @SuppressLint("CheckResult")

    fun BuyPackage(id:String,auth:String,amount:String,livedata: MutableLiveData<Buypackge>?,compiste: CompositeDisposable) {

        compiste .add(   APIServices.create().BuyPackage(auth,id,amount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .subscribe(
                { books ->
                    livedata?.postValue(books)
                },
                { error ->
                    print(error)
                }
            ))
    }
    @SuppressLint("CheckResult")

    fun RequestBalance(auth:String,livedata: MutableLiveData<RequestBalance>?) {

(   APIServices.create().RequestBalance(auth)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .subscribe(
                { books ->
                    livedata?.postValue(books)
                },
                { error ->
                    print(error)
                }
            ))
    }
    @SuppressLint("CheckResult")

    fun getBanksData(livedata: MutableLiveData<List<BankDatum>>?) {

        APIServices.create().GetMyBanksData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { data -> data }
            .subscribe(
                { books ->
                    livedata?.postValue(books)
                },
                { error ->

                }
            )
    }

    @SuppressLint("CheckResult")

    fun GetMyBalance(livedata: MutableLiveData<MyBalance>?) {

        APIServices.create().GetMyBlanceData(PreferenceHelper.getAuthId())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { data -> data }
            .subscribe(
                { books ->
                    livedata?.postValue(books)
                },
                { error ->

                }
            )
    }


    @SuppressLint("CheckResult")

    fun PostToken(token:String,livedata: MutableLiveData<Buypackge>?) {

        APIServices.create().PostToken(PreferenceHelper.getAuthId(),token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { data -> data }
            .subscribe(
                { books ->
                    livedata?.postValue(books)
                },
                { error ->

                }
            )
    }
    @SuppressLint("CheckResult")
    fun GetMyDeialyReport(auth:String,livedata: MutableLiveData<List<ReportDaily>>?) {

        APIServices.create().GetMyDeialyReport(auth)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { data -> data }
            .subscribe(
                { books ->
                    livedata?.postValue(books)
                },
                { error ->
                    print(error)
                }
            )
    }
    @SuppressLint("CheckResult")
    fun GetMyMonthReport(auth:String,from:String,to:String,livedata: MutableLiveData<List<ReportDaily>>?) {

        APIServices.create().GetMyDeialyReport(auth,from+","+to)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { data -> data }
            .subscribe(
                { books ->
                    livedata?.postValue(books)
                },
                { error ->
                    print(error)
                }
            )
    }

    @SuppressLint("CheckResult")
    fun GetTermsData(livedata: MutableLiveData<Terms>?) {

        APIServices.create().GetTerms()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { data -> data }
            .subscribe(
                { books ->
                    livedata?.postValue(books)
                },
                { error ->
                    print(error)
                }
            )
    }

    @SuppressLint("CheckResult")
    fun GetContactData(livedata: MutableLiveData<Terms>?) {

        APIServices.create().GetContactData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { data -> data }
            .subscribe(
                { books ->
                    livedata?.postValue(books)
                },
                { error ->
                    print(error)
                }
            )
    }
    @SuppressLint("CheckResult")
    fun GetPartnersData(livedata: MutableLiveData<List<PartnersModel>>?) {

        APIServices.create().GetPartnersData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { data -> data }
            .subscribe(
                { books ->
                    livedata?.postValue(books)
                },
                { error ->
                    print(error)
                }
            )
    }
    @SuppressLint("CheckResult")

    fun PrintReport(oopo:String,auth:String,livedata: MutableLiveData<Buypackge>?) {

        APIServices.create().PrintReport(auth,oopo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { data -> data }
            .subscribe(
                { books ->
                    livedata?.postValue(books)
                },
                { error ->
                    print(error)
                }
            )
    }

    @SuppressLint("CheckResult")

    fun GetMyImages(auth:String,livedata: MutableLiveData<List<SliderElement>>?) {

        APIServices.create().SliderData(auth)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { data -> data }
            .subscribe(
                { books ->
                    livedata?.postValue(books)
                },
                { error ->
                    print(error)
                }
            )
    }

    @SuppressLint("CheckResult")

    fun PrintOrder(livedata: MutableLiveData<MyBalance>?,ID: String) {

        APIServices.create().PrintOrder(PreferenceHelper.getAuthId(),ID.toString(),"1")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { data -> data }
            .subscribe(
                { books ->
                    livedata?.postValue(books)
                },
                { error ->

                }
            )
    }
    @SuppressLint("CheckResult")

    fun GetNonPrinted(auth:String,livedata: MutableLiveData<List<ReportDaily>>?) {

        APIServices.create().GetNonPrinted(auth)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { data -> data }
            .subscribe(
                { books ->
                    livedata?.postValue(books.result)
                },
                { error ->
                    print(error)
                }
            )
    }
}

