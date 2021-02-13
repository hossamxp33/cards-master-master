package com.codesroots.mac.cards.models


typealias BankData = ArrayList<BankDatum>

data class BankDatum (
    val bankname: String? = null,
    val accountname: String? = null,
    val accountno: String? = null,
    val logo: String? = null,
    val mycty: Long? = null
)
