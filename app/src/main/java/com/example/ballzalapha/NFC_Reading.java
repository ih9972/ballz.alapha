package com.example.ballzalapha;

import static android.app.ProgressDialog.show;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;

public class NFC_Reading extends AppCompatActivity {
    TextView tV;
    AlertDialog ad;
    NfcAdapter nfcAdapter;
    IntentFilter intentFilter1;
    Context context;
    IntentFilter[] readFilter;
    PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_reading);
        tV = findViewById(R.id.albert);
        context = this;
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            intentFilter1 = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED, "text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException(e);
        }
        readFilter = new IntentFilter[]{intentFilter1};
    }

    @Override
    protected void onResume() {
        super.onResume();
        disableRead();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processNFC(intent);
        ad.dismiss();
    }
    @SuppressLint("NewApi")
    public void processNFC(Intent intent) {
        Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (messages != null) {
            for (Parcelable message : messages) {
                NdefMessage ndefMessage1 = (NdefMessage) message;
                for (NdefRecord record : ndefMessage1.getRecords()) {
                    switch (record.getTnf()) {
                        case NdefRecord.TNF_WELL_KNOWN:
                            if (Arrays.equals(record.getType(), NdefRecord.RTD_TEXT)) {
                                tV.setText("  the text: "+ new String(record.getPayload()));
                            }
                    }
                }
            }
        }
    }

    private void enableRead() {
        nfcAdapter.enableForegroundDispatch((Activity) context, pendingIntent, null, null);
    }

    private void disableRead() {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch((Activity) context);
        }
    }

    public void readnfc(View view) {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setMessage("Waiting for NFC tag...");
        ad = adb.create();
        enableRead();
        ad.show();    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.AddUser){
            Intent si = new Intent(this,MainActivity.class);
            startActivity(si);
        }
        else if(id == R.id.PicCamera){
            Intent si = new Intent(this,PicFromCamera.class);
            startActivity(si);
        }
        else if(id == R.id.Location){
            Intent si = new Intent(this,Locition_Tracking.class);
            startActivity(si);
        }
        else if(id == R.id.PicGallery){
            Intent si = new Intent(this,PicFromGallery.class);
            startActivity(si);
        }

        return true;
    }
}