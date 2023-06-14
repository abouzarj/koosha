package com.example.kishservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class MySMSBroadcastReceiver extends BroadcastReceiver {
    SMSBroadcastListener smsBroadcastListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch(status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    Intent smsIntent=extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                    smsBroadcastListener.onSuccess(smsIntent);
                    Log.i("onReceive", "success");
                    break;
                case CommonStatusCodes.TIMEOUT:
                    smsBroadcastListener.onError();
                    Log.i("onReceive", "fail");
                    break;
            }
        }
    }

    public interface SMSBroadcastListener{
        void onSuccess(Intent intent);
        void onError();
    }
}
