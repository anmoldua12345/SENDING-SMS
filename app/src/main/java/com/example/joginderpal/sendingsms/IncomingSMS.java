package com.example.joginderpal.sendingsms;

import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by joginderpal on 01-12-2016.
 */
public class IncomingSMS extends BroadcastReceiver {

    TextToSpeech t2;
    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            final Object[] pdusobject = (Object[]) bundle.get("pdus");
            for (int i = 0; i < pdusobject.length; i++) {
                SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusobject[i]);
                final String phno = currentMessage.getDisplayOriginatingAddress();
                String message = currentMessage.getDisplayMessageBody();
                Toast.makeText(context, "MESSAGE FROM ->>>Sender Number : " + phno + "Message : " + message, Toast.LENGTH_LONG).show();
                Intent inte=new Intent(context, TTL.class);
                inte.putExtra("string",phno);
                context.startService(inte);
            }
        }

    }



}


