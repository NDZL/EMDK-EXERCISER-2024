package com.zebra.emdk_barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.ProfileManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.StringReader;

import com.zebra.emdk2024.R;

public class MainActivity extends AppCompatActivity {

    String TAG = "EMDK2024BARCODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvOut = findViewById(R.id.txtFingerprint);
        TextView tvFinger = findViewById(R.id.txtBuildId);
        tvFinger.setText(Build.FINGERPRINT);


        new ZebraEMDKBarcode().createInstance(getApplicationContext(), tvOut);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}

//START u0 {act=android.intent.action.MAIN cat=[android.intent.category.HOME] flg=0x14000000 cmp=com.qualcomm.qti.launcher/.MainActivity}
//WS50: BUTTON_L1 REMAPPED OK