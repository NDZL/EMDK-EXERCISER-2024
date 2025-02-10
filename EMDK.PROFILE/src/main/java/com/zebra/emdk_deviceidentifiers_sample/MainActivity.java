package com.zebra.emdk_deviceidentifiers_sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.ProfileManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity  implements EMDKManager.EMDKListener {

    String TAG = "EMDK2024";


    //Assign the profile name used in EMDKConfig.xml
    //private String profileName = "WS50";
    private String profileName = "fooFileMgr";

    //Declare a variable to store ProfileManager object
    private ProfileManager  profileManager = null;

    //Declare a variable to store EMDKManager object
    private EMDKManager emdkManager = null;
    EditText txtServiceIdentifier = null;
    EditText txtPackageName = null;
    // Provides the error type for characteristic-error
    private String errorType = "";
    // Provides the parm name for parm-error
    private String parmName = "";
    // Provides error description
    private String errorDescription = "";
    // Provides error string with type/name + description
    private String errorString = "";

    private String mToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EMDKResults results = EMDKManager.getEMDKManager(getApplicationContext(), this);
        if(results.statusCode == EMDKResults.STATUS_CODE.SUCCESS) {
            //EMDKManager object creation success
        }else {
            //EMDKManager object creation failed
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //This callback will be issued when the EMDK closes unexpectedly.
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }

    String getTargetSDK(){
        int version = 0;
        String app_username="";
        PackageManager pm = getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = pm.getApplicationInfo(getPackageName() , 0);
        } catch (PackageManager.NameNotFoundException e) {}
        if (applicationInfo != null) {
            version = applicationInfo.targetSdkVersion;
            app_username = AndroidFileSysInfo.getNameForId( applicationInfo.uid );
        }
        return  "APP_TARGET_API:"+version+" APP_USER:"+app_username;
    }

    @Override
    public void onOpened(EMDKManager emdkManager) {
        this.emdkManager = emdkManager;

        //Get the ProfileManager object to process the profiles
        profileManager = (ProfileManager) emdkManager.getInstance(EMDKManager.FEATURE_TYPE.PROFILE);
        new ProcessProfileTask().execute("");
    }

    @Override
    public void onClosed() {

        //This callback will be issued when the EMDK closes unexpectedly.
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager = null;
        }

    }


    private class ProcessProfileTask extends AsyncTask<String, Void, EMDKResults> {
        @Override
        protected EMDKResults doInBackground(String... params) {

            parmName = "";
            errorDescription = "";
            errorType = "";
            mToken = "";

            EMDKResults resultsReset = profileManager.processProfile(profileName, ProfileManager.PROFILE_FLAG.RESET, params);

            //UNCOMMENT TO SELF GRANT PERMISSION
            EMDKResults results = profileManager.processProfile(profileName, ProfileManager.PROFILE_FLAG.SET, params);
            return results;


            //return resultsReset;
        }

        @Override
        protected  void onPostExecute(EMDKResults results) {

            super.onPostExecute(results);

            String resultString = "";

            //Check the return status of processProfile
            if(results.statusCode == EMDKResults.STATUS_CODE.CHECK_XML) {

                // Get XML response as a String
                String statusXMLResponse = results.getStatusString();

                try {
                    // Create instance of XML Pull Parser to parse the response
                    XmlPullParser parser = Xml.newPullParser();
                    // Provide the string response to the String Reader that reads
                    // for the parser
                    parser.setInput(new StringReader(statusXMLResponse));
                    // Call method to parse the response
                    //parseXML(parser);

                    if (TextUtils.isEmpty(parmName) && TextUtils.isEmpty(errorType) && TextUtils.isEmpty(errorDescription) ) {

                        resultString = "Profile update success.";
                        if(!TextUtils.isEmpty(mToken))
                        {
                            resultString += "\nToken: " + mToken;
                            txtPackageName.setText(mToken);
                        }
                        Log.i("EMDK2024-1", resultString);
                    }
                    else {

                        resultString = "Profile update failed." + errorString;
                        Log.e("EMDK2024-2", resultString);
                    }

                } catch (XmlPullParserException e) {
                    resultString =  e.getMessage();
                    Log.e("EMDK2024-3", resultString);
                }
            }


        }
    }
}

//START u0 {act=android.intent.action.MAIN cat=[android.intent.category.HOME] flg=0x14000000 cmp=com.qualcomm.qti.launcher/.MainActivity}
//WS50: BUTTON_L1 REMAPPED OK