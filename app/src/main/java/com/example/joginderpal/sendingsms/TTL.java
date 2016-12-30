package com.example.joginderpal.sendingsms;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by joginderpal on 28-12-2016.
 */
public class TTL extends Service  implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {
    private TextToSpeech mTts;
    private String spokenText;
    private String spokenText1;
    PowerManager.WakeLock wl;

    @Override
    public void onCreate() {
        mTts = new TextToSpeech(this, this);
        PowerManager mgr = (PowerManager)getSystemService(Context.POWER_SERVICE);
        wl = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wl.acquire();

        // This is a good place to set spokenText

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        spokenText=intent.getExtras().getString("string");
        spokenText1=getContactName(spokenText);
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.ENGLISH);
          // String name=getContactName(spokenText);
            String p="HELLO SIR !!!!!  YOU HAVE A MESSAGE from "+spokenText1;
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                mTts.speak(p,TextToSpeech.QUEUE_FLUSH, null);
                mTts.setPitch(4);
            }
        }
    }

    @Override
    public void onUtteranceCompleted(String uttId) {
        stopSelf();
    }

    @Override
    public void onDestroy() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
        wl.release();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private String getContactName(String phone){
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        String projection[] = new String[]{ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor.moveToFirst()){
            return cursor.getString(0);
        }else {
            return "unknown number";
        }
    }


}
