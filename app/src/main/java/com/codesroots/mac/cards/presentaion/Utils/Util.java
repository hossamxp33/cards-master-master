package com.codesroots.mac.cards.presentaion.Utils;

import android.content.Context;

import com.codesroots.mac.cards.R;
import com.codesroots.mac.cards.presentaion.bluetooth.Global;

public class Util {

    public static void InitGlobalString(Context context) {
        Global.toast_success = context.getString(R.string.toast_success);
        Global.toast_fail = context.getString(R.string.toast_fail);
        Global.toast_notconnect = context.getString(R.string.toast_notconnect);
    }
}
